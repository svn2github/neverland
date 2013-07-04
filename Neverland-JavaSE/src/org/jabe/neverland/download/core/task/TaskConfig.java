package org.jabe.neverland.download.core.task;

import java.util.concurrent.ExecutorService;

import org.jabe.neverland.download.core.DownloadInfo;

public class TaskConfig {
	
	public final CacheTask mCacheTask;
	public final DownloadTaskListener mDownloadTaskListener;
	public final DownloadInfo mDownloadInfo;
	public final ExecutorService mDownloadExecutorService;
	
	private TaskConfig(final Builder builder) {
		mCacheTask = builder.mCacheTask;
		mDownloadTaskListener = builder.mDownloadTaskListener;
		mDownloadInfo = builder.mDownloadInfo;
		mDownloadExecutorService = builder.mDownloadExecutorService;
	}
	
	public static class Builder {
		
		private CacheTask mCacheTask;
		private DownloadTaskListener mDownloadTaskListener;
		private DownloadInfo mDownloadInfo;
		private ExecutorService mDownloadExecutorService;
		
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
	}
}
