package com.luugiathuy.apps.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.gson.Gson;
import com.luugiathuy.apps.domain.DownloadInfo;

@Service
public class DownloadInfoService {

	public DownloadInfo getDownloadInfo(String downloadInfoJsonFile) {
		String jsonData = readInfoFile(downloadInfoJsonFile);
		Gson jsonMapper = new Gson();
		DownloadInfo downloadInfo = jsonMapper.fromJson(jsonData, DownloadInfo.class);
		return downloadInfo;
	}

	private String readInfoFile(String downloadInfoJsonFile) {
		File file = new File(downloadInfoJsonFile);
		Assert.isTrue(file.exists(), "Info File does not exists.");
		Assert.isTrue(file.canRead(), "Info File is not readable.");
		try {
			byte[] readAllBytes = Files.readAllBytes(file.toPath());
			return new String(readAllBytes, Charset.defaultCharset());
		} catch (IOException e) {
			throw new DownloadManagerException("Reading download info failed", e);
		}
	}
}
