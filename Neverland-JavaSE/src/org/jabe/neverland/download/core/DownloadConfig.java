package org.jabe.neverland.download.core;

import java.util.concurrent.Executor;

public final class DownloadConfig {
	
	public final Executor mTaskExecutor;
	
	public final int mMaxTaskSizeInWaitingQueue;
	public final int mMaxTaskSizeInRunningQueue;
	
	public final DownloadOperationMaper mDownloadOperationMaper;
	
	private DownloadConfig(final Builder builder) {
		mTaskExecutor = builder.mTaskExecutor;
		mMaxTaskSizeInRunningQueue = builder.mMaxTaskSizeInRunningQueue;
		mMaxTaskSizeInWaitingQueue = builder.mMaxTaskSizeInWaitingQueue;
		mDownloadOperationMaper = builder.mDownloadOperationMaper;
	}
	public static class Builder {
		private Executor mTaskExecutor;
		private int mMaxTaskSizeInWaitingQueue;
		private int mMaxTaskSizeInRunningQueue;
		private DownloadOperationMaper mDownloadOperationMaper;
		public Builder() {
			
		}
	}
}
