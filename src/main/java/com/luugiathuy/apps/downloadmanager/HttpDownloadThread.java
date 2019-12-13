package com.luugiathuy.apps.downloadmanager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;

/**
 * Thread using Http protocol to download a part of file
 */
@Slf4j
public class HttpDownloadThread extends DownloadThread
{

	/**
	 * 
	 */
	private final HttpDownloader httpDownloader;

	public HttpDownloadThread(HttpDownloader httpDownloader, URL url, String outputFile, int startByte, int endByte)
	{
		super(url, outputFile, startByte, endByte);
		this.httpDownloader = httpDownloader;
	}

	@Override
	public void run()
	{
		BufferedInputStream in = null;
		RandomAccessFile raf = null;

		try
		{
			// open Http connection to URL
			HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();

			// set the range of byte to download
			String byteRange = mStartByte + "-" + mEndByte;
			conn.setRequestProperty("Range", "bytes=" + byteRange);
			log.info("bytes=" + byteRange);

			// connect to server
			conn.connect();

			// Make sure the response code is in the 200 range.
			if (conn.getResponseCode() / 100 != 2)
			{
				this.httpDownloader.error("Response from server " + conn.getResponseCode());
			}

			// get the input stream
			in = new BufferedInputStream(conn.getInputStream());

			// open the output file and seek to the start location
			raf = new RandomAccessFile(mOutputFile, "rw");
			raf.seek(mStartByte);

			byte data[] = new byte[this.httpDownloader.config.getBufferSize()];
			int numRead;
			while ((DOWNLOAD_STATUS.DOWNLOADING.equals(this.httpDownloader.state))
					&& ((numRead = in.read(data, 0, this.httpDownloader.config.getBufferSize())) != -1))
			{
				// write to buffer
				raf.write(data, 0, numRead);
				// increase the startByte for resume later
				mStartByte += numRead;
				// increase the downloaded size
				this.httpDownloader.downloaded(numRead);
			}

			if (DOWNLOAD_STATUS.DOWNLOADING.equals(this.httpDownloader.state))
			{
				mIsFinished = true;
			}
		}
		catch (IOException e)
		{
			log.error("download failed ", e);
			this.httpDownloader.error(e.getMessage());
		}
		finally
		{
			if (raf != null)
			{
				try
				{
					raf.close();
				}
				catch (IOException e)
				{
				}
			}

			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (IOException e)
				{
				}
			}
		}

		log.info("End thread " + Thread.currentThread().getId());
	}
}