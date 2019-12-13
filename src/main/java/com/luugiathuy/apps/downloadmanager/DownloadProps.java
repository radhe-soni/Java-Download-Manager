package com.luugiathuy.apps.downloadmanager;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.config")
public class DownloadProps
{
	private int blockSize;
	private int bufferSize;
	private int minDownloadSize = blockSize * 100;
}
