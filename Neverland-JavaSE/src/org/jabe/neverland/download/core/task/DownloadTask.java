package org.jabe.neverland.download.core.task;


public class DownloadTask implements Runnable {
	
	private TaskConfig mTaskConfig;
	
	public DownloadTask(TaskConfig mTaskConfig, CacheTask mCacheTask) {
		super();
		this.mTaskConfig = mTaskConfig;
		this.mCacheTask = mCacheTask;
	}

	private CacheTask mCacheTask;
	
	@Override
	public void run() {
		// task life cycle
		mTaskConfig.mDownloadTaskListener.onPreTask();
		
		if (mCacheTask.isInCache()) {
			mCacheTask.readFromCache();
		} else {
			mCacheTask.saveToCache();
		}
		
		final int secCount = mCacheTask.mSectionCount;

		// task life cycle
		mTaskConfig.mDownloadTaskListener.onBeforeExecute();
		
		if (mCacheTask.mDownloadedLength == mCacheTask.mContentLength && mCacheTask.mContentLength > 0) {
			onSuccess();
			return;
		}
		
		if (secCount > 1) {
			doMultipleWork();
		} else {
			doSingleWork();
		}
	}

	private void doSingleWork() {
		final Runnable r = new Runnable() {
			
			@Override
			public void run() {
				doRealDownload(mCacheTask.mContentLength, mCacheTask.mDownloadedLength, mCacheTask.mContentLength);
			}
		};
		mTaskConfig.mDownloadExecutorService.execute(r);
	}

	private void doMultipleWork() {
		
	}

	private void doRealDownload(final long contengLength, final long start, final long end) {
		
	}
	
	private void onSuccess() {
		
	}
}
