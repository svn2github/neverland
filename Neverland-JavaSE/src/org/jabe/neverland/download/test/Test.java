package org.jabe.neverland.download.test;

import org.jabe.neverland.download.core.DownloadConfig;
import org.jabe.neverland.download.core.DownloadListenerManager;
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
		final DownloadListenerManager dm = new DownloadManager(downloadConfig);
	}

}
