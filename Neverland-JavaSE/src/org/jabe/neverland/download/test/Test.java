package org.jabe.neverland.download.test;

import org.jabe.neverland.download.core.DownloadConfig;
import org.jabe.neverland.download.core.DownloadInfo;
import org.jabe.neverland.download.core.DownloadManager;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		final DownloadConfig downloadConfig = new DownloadConfig.Builder()
				.addMaxTaskSizeInRunning(2)
				.addMaxTaskSizeInWaiting(2)
				.build();
		final DownloadInfo downloadInfo = new DownloadInfo();
		final DownloadManager dm = new DownloadManager(downloadConfig);
		
		dm.startDownload(null);
	}

}
