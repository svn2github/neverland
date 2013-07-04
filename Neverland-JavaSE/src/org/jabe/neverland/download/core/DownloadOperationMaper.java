package org.jabe.neverland.download.core;

public abstract class DownloadOperationMaper {
	
	protected DownloadManager mDownloadManager;	
	protected DownloadEngine mDownloadEngine;
	
	protected DownloadOperationMaper(DownloadManager downloadManager, DownloadEngine downloadEngine) {
		this.mDownloadEngine = downloadEngine;
		this.mDownloadManager = downloadManager;
	}
	
	public abstract void operationMap(DownloadInfo downloadInfo, DownloadStatus lastStatus);
	
}
