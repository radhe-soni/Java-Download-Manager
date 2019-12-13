package com.luugiathuy.apps.downloadmanager;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * Thread to download part of a file
 */
abstract class DownloadThread implements Runnable {
	protected URL mURL;
	protected String mOutputFile;
	protected int mStartByte;
	protected int mEndByte;
	protected boolean mIsFinished;
	protected CompletableFuture<Void> future;
	public DownloadThread(URL url, String outputFile, int startByte, int endByte) {
		mURL = url;
		mOutputFile = outputFile;
		mStartByte = startByte;
		mEndByte = endByte;
		mIsFinished = false;
		
		
	}
	public boolean isFinished() {
		return mIsFinished;
	}
	
	public void download() {
		future = CompletableFuture.runAsync(this);
	}
	
	/**
	 * Waiting for the thread to finish
	 * @throws InterruptedException
	 */
	public void waitFinish() throws InterruptedException {
		future.join();			
	}
	
}