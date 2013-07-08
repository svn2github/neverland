package org.jabe.neverland.download.core.engine.impl;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jabe.neverland.download.core.cache.DownloadCacheTask;
import org.jabe.neverland.download.core.engine.impl.IODownloader.SizeBean;
import org.jabe.neverland.download.util.IoUtils;


public class DownloadTask implements Runnable {
	
	private static final int BUFFER_SIZE = 8 * 1024;
	
	protected TaskConfig mTaskConfig;
	
	public DownloadTask(TaskConfig mTaskConfig) {
		super();
		this.mTaskConfig = mTaskConfig;
		mCacheTask = mTaskConfig.mCacheTask;
	}

	private DownloadCacheTask mCacheTask;
	private volatile boolean hasStarted = false;
	
	@Override
	public void run() {
		hasStarted = true;
		// task life cycle
		mTaskConfig.mDownloadTaskListener.onPreTask();
		
		try {
			if (mCacheTask.isInCache()) {
				mCacheTask.readFromCache();
			} else {
				if (mCacheTask.mContentLength == 0) {
					
				}
				mCacheTask.saveToCache();
			}
		} catch (Exception e) {
			
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
	
//	private long 

	private void doSingleWork() {
		try {
			doRealDownload(1, mCacheTask.mContentLength, mCacheTask.mDownloadedLength, mCacheTask.mContentLength);
		} catch (IOException e) {
		}
	}

	private void doMultipleWork() {
		
	}

	private void doRealDownload(final int sectionNo, final long contentLength, final long start, final long end) throws IOException {
		final SizeBean sb = new SizeBean(contentLength, start, end);
		final InputStream is = mTaskConfig.mDownloader.getStream(mCacheTask.mDownloadInfo.getmDownloadUrl(), null, sb);
		try {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(mCacheTask.generateCacheSaveFullPath()), BUFFER_SIZE);
			try {
//				IoUtils.copyStream(is, os);
				byte[] bytes = new byte[BUFFER_SIZE];
				while (true) {
					int count = is.read(bytes, 0, BUFFER_SIZE);
					if (count == -1) {
						break;
					}
					os.write(bytes, 0, count);
					mCacheTask.updateSectionProgress(sectionNo, count);
				}
				onSuccess();
			} finally {
				IoUtils.closeSilently(os);
			}
		} finally {
			IoUtils.closeSilently(is);
		}
	}
	
	private void onSuccess() {
		
	}

	public boolean startDownload() {
		if (!hasStarted) {
			mTaskConfig.mDownloadExecutorService.execute(this);
			return true;
		} else {
			return restartDownload();
		}
	}

	public boolean resumeDownload() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean pauseDownload() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean cancelDownload() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean restartDownload() {
		if (!hasStarted) {
			mTaskConfig.mDownloadExecutorService.execute(this);
			return true;
		} else {
			// TODO
			return false;
		}
	}
}
