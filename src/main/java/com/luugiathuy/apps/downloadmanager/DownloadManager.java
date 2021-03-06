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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.luugiathuy.apps.downloader.Downloader;
import com.luugiathuy.apps.downloader.HttpDownloader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DownloadManager {
	

	@Autowired
	private ApplicationContext context;
	@Autowired
	private  DownloadProps config;
	// Constant variables
	private static final int DEFAULT_NUM_CONN_PER_DOWNLOAD = 8;
	public static final String DEFAULT_OUTPUT_FOLDER = "";

	// Member variables
	private int mNumConnPerDownload;
	private List<Downloader> mDownloadList;
	
	/** Protected constructor */
	public DownloadManager() {
		mNumConnPerDownload = DEFAULT_NUM_CONN_PER_DOWNLOAD;
		mDownloadList = new Vector<>();
	}
	
	/**
	 * Get the max. number of connections per download
	 */
	public int getNumConnPerDownload() {
		return mNumConnPerDownload;
	}
	
	/**
	 * Set the max number of connections per download
	 */
	public void setNumConnPerDownload(int value) {
		mNumConnPerDownload = value;
	}
	
	/**
	 * Get the downloader object in the list
	 * @param index
	 * @return
	 */
	public Downloader getDownload(int index) {
		Downloader downloader = mDownloadList.get(index);
		log.info("file {} requested", downloader.getMFileName());
		return mDownloadList.get(index);
	}
	
	public void removeDownload(int index) {
		Downloader remove = mDownloadList.remove(index);
		log.info("removing file {} requested", remove.getMFileName());
	}
	
	/**
	 * Get the download list
	 * @return
	 */
	public List<Downloader> getDownloadList() {
		return mDownloadList;
	}
	
	
	public Downloader createDownload(URL verifiedURL, String outputFolder) {
		HttpDownloader fd = context.getBean(HttpDownloader.class, verifiedURL, outputFolder, mNumConnPerDownload, config);
		startDownloadList(fd);
		return fd;
	}
	
	public void startDownloadList(HttpDownloader fd)
	{
		//fd.download();
		mDownloadList.add(fd);
	}

	/**
	 * Verify whether an URL is valid
	 * @param fileURL
	 * @return the verified URL, null if invalid
	 */
	public static URL verifyFileURL(String fileURL) {
		// Only allow HTTP URLs.
        URL verifiedUrl = verifyUrl(fileURL);
        
        // Make sure URL specifies a file.
        if (verifiedUrl.getFile().length() < 2)
            return null;
        
        return verifiedUrl;
	}

	public static URL verifyUrl(String fileURL) {
		if (!(fileURL.toLowerCase().startsWith("http://") || fileURL.toLowerCase().startsWith("https://")))
            return null;
        
        // Verify format of URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(fileURL);
        } catch (Exception e) {
            return null;
        }
		return verifiedUrl;
	}

}
