package org.jabe.neverland.download.task;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jabe.neverland.download.downloader.Downloader.SizeBean;
import org.jabe.neverland.download.util.IoUtils;


public class DownloadTask implements Runnable {
	
	private static final int BUFFER_SIZE = 8 * 1024;
	
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
				try {
					doRealDownload(mCacheTask.mContentLength, mCacheTask.mDownloadedLength, mCacheTask.mContentLength);
				} catch (IOException e) {
				}
			}
		};
		mTaskConfig.mDownloadExecutorService.execute(r);
	}

	private void doMultipleWork() {
		
	}

	private void doRealDownload(final long contentLength, final long start, final long end) throws IOException {
		
		final SizeBean sb = new SizeBean(contentLength, start, end);
		final InputStream is = mTaskConfig.mDownloader.getStream(mCacheTask.mDownloadUrl, null, sb);
		try {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(mCacheTask.mSaveFileFullPath), BUFFER_SIZE);
			try {
				IoUtils.copyStream(is, os);
			} finally {
				IoUtils.closeSilently(os);
			}
		} finally {
			IoUtils.closeSilently(is);
		}
	}
	
	private void onSuccess() {
		
	}
}
