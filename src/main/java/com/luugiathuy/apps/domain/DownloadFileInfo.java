package com.luugiathuy.apps.domain;

import java.net.MalformedURLException;
import java.net.URL;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class DownloadFileInfo {

	String artifactId;
	String artifactVersion;
	
	public URL toArtifactRootUrl(URL rootUrl) {
		URL artifactRootUrl = null;
		try {
			return new URL(new URL(rootUrl, this.getArtifactId()), this.getArtifactVersion());
		} catch (MalformedURLException e) {
			log.error("URL for artifact {} could not constructed.", this.getArtifactId(), e);
		}
		return artifactRootUrl;
	}
}
