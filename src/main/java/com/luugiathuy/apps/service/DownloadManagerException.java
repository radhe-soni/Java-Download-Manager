package com.luugiathuy.apps.service;

public class DownloadManagerException extends RuntimeException {

	private static final long serialVersionUID = 953850767456625708L;

	public DownloadManagerException() {
		super();
	}

	public DownloadManagerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DownloadManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public DownloadManagerException(String message) {
		super(message);
	}

	public DownloadManagerException(Throwable cause) {
		super(cause);
	}

}
