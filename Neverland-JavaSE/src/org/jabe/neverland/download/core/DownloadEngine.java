package org.jabe.neverland.download.core;

public abstract class DownloadEngine {
	
	public abstract void startDownload(DownloadInfo downloadInfo);
	
	public abstract boolean resumeDownload(DownloadInfo downloadInfo);
	
	public abstract boolean pauseDownload(DownloadInfo downloadInfo);
	
	public abstract boolean cancelDownload(DownloadInfo downloadInfo);
	
	public abstract boolean restartDownload(DownloadInfo downloadInfo);
	
}
