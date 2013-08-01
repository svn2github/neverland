package org.jabe.neverland.download.core.engine.impl;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jabe.neverland.download.core.cache.DownloadCacheTask;
import org.jabe.neverland.download.core.engine.impl.IODownloader.SizeBean;
import org.jabe.neverland.download.util.IoUtils;


public class DownloadTask implements Runnable {
	
	private static final int BUFFER_SIZE = 16 * 1024;
	
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
			// whatever, need to get content length to check the task in cache
			mCacheTask.mContentLength = getContentLength(mCacheTask.mDownloadInfo.getmDownloadUrl());
			if (mCacheTask.isInCache()) {
				mCacheTask.readFromCache();
			} else {
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
	
	private void doSingleWork() {
		try {
			doRealDownload(1, mCacheTask.mContentLength, mCacheTask.mDownloadedLength, mCacheTask.mContentLength);
		} catch (IOException e) {
			// TODO
		}
	}

	private void doMultipleWork() {
		final int secCount = mCacheTask.mSectionCount;
		for (int j = 0; j < secCount; j++) {
			final long length = mCacheTask.mContentLength;
			final long start = mCacheTask.mSectionsOffset[j];
			long endt = -1;
			long per = 0;
			// 计算当前块的长度
			if (j < secCount - 1) {

				per = length / secCount;

				endt = per * (j + 1);

			} else {

				endt = length;

				per = endt - start;

			}
		}
	}

	private void doRealDownload(final int sectionNo, final long contentLength, final long start, final long end) throws IOException {
		final SizeBean sb = new SizeBean(contentLength, start, end);
		final InputStream is = mTaskConfig.mDownloader.getStream(mCacheTask.mDownloadInfo.getmDownloadUrl(), null, sb);
		try {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(mCacheTask.generateCacheSaveFullPath()), BUFFER_SIZE);
			try {
				byte[] bytes = new byte[BUFFER_SIZE];
				while (true) {
					int count = is.read(bytes, 0, BUFFER_SIZE);
					if (count == -1) {
						break;
					}
					os.write(bytes, 0, count);
					mCacheTask.updateSectionProgress(sectionNo, count);
					mTaskConfig.mDownloadTaskListener.onUpdateProgress(count, mCacheTask.mDownloadedLength, mCacheTask.mContentLength);
				}
				onSuccess();
			} finally {
				IoUtils.closeSilently(os);
			}
		} finally {
			IoUtils.closeSilently(is);
		}
	}
	
	private long getContentLength(String url) throws IOException {
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		long l = conn.getContentLength();
		conn.disconnect();
		return l;
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
