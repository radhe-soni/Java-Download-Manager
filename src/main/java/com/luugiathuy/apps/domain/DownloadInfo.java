package com.luugiathuy.apps.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadInfo {

	String rootUrl;
	String targetDirectory;
	List<DownloadFileInfo> artifacts;
}
