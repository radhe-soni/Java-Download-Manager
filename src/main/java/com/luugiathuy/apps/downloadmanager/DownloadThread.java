package com.luugiathuy.apps.downloadmanager;

import java.net.URL;

/**
 * Thread to download part of a file
 */
abstract class DownloadThread implements Runnable {
	protected URL mURL;
	protected String mOutputFile;
	protected int mStartByte;
	protected int mEndByte;
	protected boolean mIsFinished;
	protected Thread mThread;
	
	public DownloadThread(URL url, String outputFile, int startByte, int endByte) {
		mURL = url;
		mOutputFile = outputFile;
		mStartByte = startByte;
		mEndByte = endByte;
		mIsFinished = false;
		
		
	}
	
	/**
	 * Get whether the thread is finished download the part of file
	 */
	public boolean isFinished() {
		return mIsFinished;
	}
	
	/**
	 * Start or resume the download
	 */
	public void download() {
		mThread = new Thread(this);
		mThread.start();
	}
	
	/**
	 * Waiting for the thread to finish
	 * @throws InterruptedException
	 */
	public void waitFinish() throws InterruptedException {
		mThread.join();			
	}
	
}