package org.jabe.neverland.download.test;

import org.jabe.neverland.download.core.DownloadConfig;
import org.jabe.neverland.download.core.DownloadInfo;
import org.jabe.neverland.download.core.DownloadManager;
import org.jabe.neverland.download.core.DownloadProgressListener;
import org.jabe.neverland.download.core.DownloadStatusListener;

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
		downloadInfo.setmDownloadUrl("http://storefs.nearme.com.cn/uploadFiles/Pfiles/201307/15/b569cf9a2af44dbbaa5919686856907d.apk?n=com.nearme.gamecenter_V3.2.2");
		downloadInfo.setmIconUrl("");
		downloadInfo.setmId("110");
		downloadInfo.setmName("For Test");
		downloadInfo.setmPackageName("com.nearme.gamecenter");
		downloadInfo.putInt(DownloadInfo.P_SECTION_COUNT, 5);
		final DownloadManager dm = new DownloadManager(downloadConfig);
		
		dm.registerListener(new DownloadStatusListener() {
			
			@Override
			public void onStatusCountChanged(int downloadingCount) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStatusChanged(String packageName, int status) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(String packageName, Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		dm.registerListener(new DownloadProgressListener() {
			
			@Override
			public void onUpdateProgress(String packageName, double added,
					double downloaded, double total) {
//				System.out.println("Downloaded Percent : " + downloaded/total);
				System.out.println("changed by current thread " + Thread.currentThread().getName() + " " + downloaded/total);
			}
		});
		
		dm.startDownload(downloadInfo);
	}
}
