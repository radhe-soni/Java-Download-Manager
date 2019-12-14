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

package com.luugiathuy.apps.downloader;

import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.luugiathuy.apps.downloadmanager.DOWNLOAD_STATUS;
import com.luugiathuy.apps.downloadmanager.DownloadProps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HttpDownloader extends Downloader
{

	protected DownloadProps config;
	// Contants for block and buffer size
	protected int minDownloadSize;

	public HttpDownloader(URL url, String outputFolder, int numConnections, DownloadProps config)
	{
		super(url, outputFolder, numConnections);
		this.config = config;
		minDownloadSize = config.getBlockSize() * 100;
	}

	void error(String message)
	{
		log.error(message);
		setState(DOWNLOAD_STATUS.ERROR);
	}

	@Override
	public void run()
	{
		HttpURLConnection conn = null;
		try
		{
			// Open connection to URL
			conn = (HttpURLConnection) fileUrl.openConnection();
			conn.setConnectTimeout(10000);

			// Connect to server
			conn.connect();

			// Make sure the response code is in the 200 range.
			if (conn.getResponseCode() / 100 != 2)
			{
				error("Response from server " + conn.getResponseCode());
			}

			// Check for valid content length.
			int contentLength = conn.getContentLength();
			if (contentLength < 1)
			{
				error("Content is empty " + contentLength);
			}

			if (fileSize == -1)
			{
				fileSize = contentLength;
				stateChanged();
				log.info("File size: " + fileSize);
			}

			// if the state is DOWNLOADING (no error) -> start downloading
			if (DOWNLOAD_STATUS.DOWNLOADING.equals(state))
			{
				if (mListDownloadThread.isEmpty())
				{
					addDownloadThreads();
				}
				else
				{
					resumeDownloadingThreads();
				}

				// waiting for all threads to complete
				joinThreads();

				markDownloadComplete();
			}
		}
		catch (Exception e)
		{
			log.error("download failed ", e);
			error(e.getMessage());
		}
		finally
		{
			if (conn != null)
				conn.disconnect();
		}
	}

	private void markDownloadComplete()
	{
		// check the current state again
		if (DOWNLOAD_STATUS.DOWNLOADING.equals(state))
		{
			setState(DOWNLOAD_STATUS.COMPLETED);
		}
	}

	private void joinThreads() throws InterruptedException
	{
		for (int i = 0; i < mListDownloadThread.size(); ++i)
		{
			mListDownloadThread.get(i).waitFinish();
		}
	}

	private void resumeDownloadingThreads()
	{
		for (int i = 0; i < mListDownloadThread.size(); ++i)
		{
			if (!mListDownloadThread.get(i).isFinished())
				mListDownloadThread.get(i).download();
		}
	}

	private void addDownloadThreads()
	{
		int startByte = 0;
		int endByte = fileSize - 1;
		if (fileSize > minDownloadSize)
		{
			int partSize = getPartSize();

			// start/end Byte for each thread
			endByte = partSize - 1;
			HttpDownloadThread aThread = new HttpDownloadThread(this, fileUrl, mOutputFolder + mFileName, startByte,
					endByte);
			startDownloadThread(aThread);
			boolean breakLoop = false;
			while (endByte < fileSize && !breakLoop)
			{
				startByte = endByte + 1;
				endByte += partSize;
				if (endByte >= fileSize)
				{
					endByte = fileSize - 1;
					breakLoop = true;
				}
				aThread = new HttpDownloadThread(this, fileUrl, mOutputFolder + mFileName, startByte, endByte);
				startDownloadThread(aThread);
			}
		}
		else
		{
			HttpDownloadThread aThread = new HttpDownloadThread(this, fileUrl, mOutputFolder + mFileName, startByte,
					endByte);
			startDownloadThread(aThread);
		}
	}

	/**
	 * downloading size for each thread
	 * 
	 * @return
	 */
	private int getPartSize()
	{
		int partSize = Math.round(((float) fileSize / mNumConnections) / config.getBlockSize()) * config.getBlockSize();
		log.info("Part size: {}", partSize);
		log.info("No. of parts: {}", fileSize / partSize);
		return partSize;
	}
}
