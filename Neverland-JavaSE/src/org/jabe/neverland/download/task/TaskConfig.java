package org.jabe.neverland.download.task;

import java.util.concurrent.ExecutorService;

import org.jabe.neverland.download.core.DownloadInfo;
import org.jabe.neverland.download.downloader.Downloader;

public class TaskConfig {
	
	public final CacheTask mCacheTask;
	public final DownloadTaskListener mDownloadTaskListener;
	public final DownloadInfo mDownloadInfo;
	public final ExecutorService mDownloadExecutorService;
	public final Downloader mDownloader;
	
	private TaskConfig(final Builder builder) {
		mCacheTask = builder.mCacheTask;
		mDownloadTaskListener = builder.mDownloadTaskListener;
		mDownloadInfo = builder.mDownloadInfo;
		mDownloadExecutorService = builder.mDownloadExecutorService;
		mDownloader = builder.mDownloader;
	}
	
	public static class Builder {
		
		private CacheTask mCacheTask;
		private DownloadTaskListener mDownloadTaskListener;
		private DownloadInfo mDownloadInfo;
		private ExecutorService mDownloadExecutorService;
		private Downloader mDownloader;
		
		public Builder() {
		}
		
		public Builder addCacheTask(final CacheTask cacheTask) {
			this.mCacheTask = cacheTask;
			return this;
		}
		
		public Builder addDownloadTaskListener(final DownloadTaskListener downloadTaskListener) {
			this.mDownloadTaskListener = downloadTaskListener;
			return this;
		}
		
		public Builder addDownloadInfo(final DownloadInfo downloadInfo) {
			this.mDownloadInfo = downloadInfo;
			return this;
		}
		
		public Builder addDownloadExecutor(final ExecutorService executorService) {
			this.mDownloadExecutorService = executorService;
			return this;
		}
		
		public Builder addDownloader(final Downloader downloader) {
			this.mDownloader = downloader;
			return this;
		}
	}
}
