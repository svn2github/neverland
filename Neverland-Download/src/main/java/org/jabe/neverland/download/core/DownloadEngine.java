package org.jabe.neverland.download.core;

import org.jabe.neverland.download.core.engine.impl.IODownloader;

public interface DownloadEngine {
	
	public boolean startDownload(DownloadInfo downloadInfo);
	
	public boolean resumeDownload(DownloadInfo downloadInfo);
	
	public boolean pauseDownload(DownloadInfo downloadInfo);
	
	public boolean cancelDownload(DownloadInfo downloadInfo);
	
	public boolean restartDownload(DownloadInfo downloadInfo);
	
	public void setIoDownloader(IODownloader downloader);
	
}
