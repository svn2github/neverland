package org.jabe.neverland.download.core.engine.impl;

import java.util.concurrent.ExecutorService;

public class TaskConfig {
	
	public final DownloadCacheInvoker mCacheInvoker;
	public final DownloadTaskListener mDownloadTaskListener;
	public final ExecutorService mDownloadExecutorService;
	public final IODownloader mDownloader;
	
	private TaskConfig(final Builder builder) {
		mCacheInvoker = builder.mCacheTask;
		mDownloadTaskListener = builder.mDownloadTaskListener;
		mDownloadExecutorService = builder.mDownloadExecutorService;
		mDownloader = builder.mDownloader;
	}
	
	public static class Builder {
		
		private DownloadCacheInvoker mCacheTask;
		private DownloadTaskListener mDownloadTaskListener;
		private ExecutorService mDownloadExecutorService;
		private IODownloader mDownloader;
		
		public Builder() {
		}
		
		public Builder addCacheInvoker(final DownloadCacheInvoker cacheTask) {
			this.mCacheTask = cacheTask;
			return this;
		}
		
		public Builder addDownloadTaskListener(final DownloadTaskListener downloadTaskListener) {
			this.mDownloadTaskListener = downloadTaskListener;
			return this;
		}
		
		public Builder addDownloadExecutor(final ExecutorService executorService) {
			this.mDownloadExecutorService = executorService;
			return this;
		}
		
		public Builder addDownloader(final IODownloader downloader) {
			this.mDownloader = downloader;
			return this;
		}
		
		public TaskConfig build() {
			return new TaskConfig(this);
		}
	}
}
