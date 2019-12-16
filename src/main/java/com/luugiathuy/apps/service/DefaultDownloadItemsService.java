package com.luugiathuy.apps.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.luugiathuy.apps.domain.DownloadInfo;
import com.luugiathuy.apps.downloader.Downloader;
import com.luugiathuy.apps.downloadmanager.DownloadManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DefaultDownloadItemsService {

	@Autowired
	private DownloadInfoService downloadInfoService;
	@Autowired
	private DownloadManager downloadManager;

	public Stream<Downloader> getDownloaders(String downloadInfoJsonFile) {
		DownloadInfo downloadInfo = downloadInfoService.getDownloadInfo(downloadInfoJsonFile);
		Assert.notNull(downloadInfo.getRootUrl(), "Download info file does not have root url.");
		URL verifiedUrl = DownloadManager.verifyUrl(downloadInfo.getRootUrl());
		//Assert.isNull(verifiedUrl, "Root Url in download info file is not valid.");
		return getLatestArtifactsUrl(downloadInfo, verifiedUrl)
				.map(url -> downloadManager.createDownload(url, downloadInfo.getTargetDirectory()));
	}

	private Stream<URL> getLatestArtifactsUrl(DownloadInfo downloadInfo, URL verifiedUrl) {
		return getArtifactsRootUrl(downloadInfo, verifiedUrl)
				.map(artifactRootUrl -> getLatestArtifactUrl(artifactRootUrl)).filter(Objects::nonNull);
	}

	private Stream<URL> getArtifactsRootUrl(DownloadInfo downloadInfo, URL verifiedUrl) {
		return Optional.ofNullable(downloadInfo.getArtifacts()).stream().flatMap(e -> e.stream()).map(fileInfo -> fileInfo.toArtifactRootUrl(verifiedUrl));
	}

	private URL getLatestArtifactUrl(URL artifactRootUrl) {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(artifactRootUrl);
			List<String> urls = findLinks(document);
			List<URLConnection> connections = urls.stream().map(url -> toURLConnection(artifactRootUrl, url))
					.filter(Objects::nonNull).sorted(Comparator.comparing(con1 -> -con1.getDate()))
					.collect(Collectors.toList());
			URLConnection urlConnection = connections.remove(connections.size() - 1);
			connections.forEach(con -> ((HttpURLConnection) con).disconnect());
			URL url = urlConnection.getURL();
			((HttpURLConnection) urlConnection).disconnect();
			return url;

		} catch (DocumentException e) {
			log.error("Error fetching artifact.", e);
		}

		return null;
	}

	private URLConnection toURLConnection(URL artifactRootUrl, String url) {
		try {
			URLConnection conn = new URL(artifactRootUrl, url).openConnection();
			conn.setConnectTimeout(10000);
			conn.connect();
			return conn;
		} catch (IOException e) {
			log.error("Error fetching artifact.", e);
		}
		return null;
	}

	public List<String> findLinks(Document document) throws DocumentException {

		List<Node> list = document.selectNodes("//a/@href");
		List<String> urls = new ArrayList<>();
		for (Iterator<Node> iter = list.iterator(); iter.hasNext();) {
			Attribute attribute = (Attribute) iter.next();

			String url = attribute.getValue();
			if (url.endsWith("jar") || url.endsWith("war")) {
				urls.add(url);
			}

		}
		return urls;
	}
}
