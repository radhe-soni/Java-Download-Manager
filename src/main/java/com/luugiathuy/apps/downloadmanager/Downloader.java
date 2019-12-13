/**
Copyright (c) 2011-present - Luu Gia Thuy

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/

package com.luugiathuy.apps.downloadmanager;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class Downloader extends Observable implements Runnable
{

	protected URL fileUrl;

	protected String mOutputFolder;

	protected int mNumConnections;

	protected String mFileName;

	protected int fileSize;

	protected DOWNLOAD_STATUS state;

	protected int mDownloaded;

	protected List<DownloadThread> mListDownloadThread;

	protected Instant initTime;
	
	protected javax.swing.JButton startDownloadButton;
	/**
	 * Constructor
	 * 
	 * @param fileURL
	 * @param outputFolder
	 * @param numConnections
	 */
	protected Downloader(URL url, String outputFolder, int numConnections)
	{
		fileUrl = url;
		mOutputFolder = outputFolder;
		mNumConnections = numConnections;

		// Get the file name from url path
		String fileURL = url.getFile();
		mFileName = fileURL.substring(fileURL.lastIndexOf('/') + 1);
		log.info("File name: " + mFileName);
		fileSize = -1;
		state = DOWNLOAD_STATUS.DOWNLOADING;
		mDownloaded = 0;

		mListDownloadThread = new ArrayList<>();
		startDownloadButton = new javax.swing.JButton();
		startDownloadButton.setText("Add Download");
		startDownloadButton.addActionListener(event -> download());
	}

	public void pause()
	{
		setState(DOWNLOAD_STATUS.PAUSED);
	}

	public void resume()
	{
		setState(DOWNLOAD_STATUS.DOWNLOADING);
		download();
	}

	public void cancel()
	{
		setState(DOWNLOAD_STATUS.CANCELLED);
	}

	public String getURL()
	{
		return fileUrl.toString();
	}

	/**
	 * Get the current progress of the download
	 */
	public float getProgress()
	{
		
		return ((float) mDownloaded / fileSize) * 100;
	}
	
	public float getDownloadSpeed()
	{
		Duration duration = Duration.between(initTime, Instant.now());
		return ((float) mDownloaded)/(duration.getSeconds() * 1024);
	}

	/**
	 * Set the state of the downloader
	 */
	protected void setState(DOWNLOAD_STATUS value)
	{
		state = value;
		stateChanged();
	}

	/**
	 * Start or resume download
	 */
	protected void download()
	{
		initTime = Instant.now();
		CompletableFuture.runAsync(this);
	}

	protected void startDownloadThread(DownloadThread aThread)
	{
		aThread.download();
		mListDownloadThread.add(aThread);
	}

	/**
	 * Increase the downloaded size
	 */
	protected synchronized void downloaded(int value)
	{
		mDownloaded += value;
		stateChanged();
	}

	/**
	 * Set the state has changed and notify the observers
	 */
	protected void stateChanged()
	{
		setChanged();
		notifyObservers();
	}
}
