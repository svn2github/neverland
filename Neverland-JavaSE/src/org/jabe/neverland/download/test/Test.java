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
		downloadInfo.setmDownloadUrl("http://storefs.nearme.com.cn/uploadFiles/Pfiles/201306/30/149811f37b5dfd12b8ee5297d97f67f9.apk?n=com.locojoy.immt_a_chs.nearme.gamecenter_2.5.1.1");
		downloadInfo.setmIconUrl("");
		downloadInfo.setmId("110");
		downloadInfo.setmName("For Test");
		downloadInfo.setmPackageName("com.nearme.gamecenter");
		final DownloadManager dm = new DownloadManager(downloadConfig);
		dm.startDownload(downloadInfo);
		
	}

}
