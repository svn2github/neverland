package org.jabe.neverland.download.core.engine.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jabe.neverland.download.core.cache.CacheDownloadInfo;
import org.jabe.neverland.download.util.IoUtils;


public class MultiThreadTask extends AbstractTask {
	
	private static final int BUFFER_SIZE = 16 * 1024;
	
	
	public MultiThreadTask(TaskConfig mTaskConfig) {
		super(mTaskConfig);
	}

	private Boolean[] successTag;
	
	private CacheDownloadInfo mCacheDownloadInfo;
	private DownloadCacheInvoker mCacheInvoker;
	
	@Override
	public void run() {
		hasStarted = true;
		// task life cycle
		onPreTask();
		
		try {
			// whatever, need to get content length to check the task in cache
			if (mCacheDownloadInfo.mContentLength == 0) {
				mCacheDownloadInfo.mContentLength = getContentLength(mCacheDownloadInfo.mDownloadInfo.getmDownloadUrl());
			}
			if (mCacheInvoker.isInCache()) {
				mCacheInvoker.readFromCache();
			} else {
				mCacheInvoker.saveToCache();
			}
		} catch (Exception e) {
			
		}
		
		final int secCount = mCacheDownloadInfo.mSectionCount;

		// task life cycle
		onBeforeExecute();
		
		if (mCacheDownloadInfo.mDownloadedLength == mCacheDownloadInfo.mContentLength && mCacheDownloadInfo.mContentLength > 0) {
			success();
			return;
		}
		
		successTag = new Boolean[secCount];
		for (int i = 0; i < successTag.length; i++) {
			successTag[i] = false;
		}
		try {
			if (secCount > 1) {
				doMultipleWork();
			} else {
				doSingleWork();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void doSingleWork() throws IOException {
		doRealDownload(1, mCacheDownloadInfo.mContentLength, mCacheDownloadInfo.mDownloadedLength, mCacheDownloadInfo.mContentLength);
	}
	
	private void doMultipleWork() throws FileNotFoundException {
		final int secCount = mCacheDownloadInfo.mSectionCount;
//		mCacheAccessFile = new RandomAccessFile(mCacheTask.generateCacheSaveFullPath(), "rw");
		for (int j = 0; j < secCount; j++) {
			final long length = mCacheDownloadInfo.mContentLength;
			final long start = mCacheDownloadInfo.mSectionsOffset[j];
			final long per = length / secCount;
			long endt = -1;
			// 计算当前块的长度
			if (j < secCount - 1) {// 当前块是否为最后一块
				endt = per * (j + 1);
			} else {
				endt = length;
			}
			final long endF = endt;
			final int s = j + 1;
			final long realStart = j * per + start;
			getExecutorService().execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						doRealDownload(s, length, realStart, endF);
					} catch (IOException e) {
						// TODO
						triggerSectionFailure(s);
					}
				}
			});
		}
	}

	private void doRealDownload(final int sectionNo, final long contentLength, final long start, final long end) throws IOException {
		final SizeBean sb = new SizeBean(contentLength, start, end);
		final InputStream is = getStream(mCacheDownloadInfo.mDownloadInfo.getmDownloadUrl(), null, sb);
		try {
			try {
				byte[] bytes = new byte[BUFFER_SIZE];
				while (true) {
					int count = is.read(bytes, 0, BUFFER_SIZE);
					if (count == -1) {
						break;
					}
//					mCacheAccessFile.seek((sectionNo - 1) * per + mCacheTask.mSectionsOffset[sectionNo - 1]);
//					mCacheAccessFile.write(bytes, 0, count);
					mCacheInvoker.updateSectionProgress(bytes, sectionNo, count);
					onUpdateProgress(count, mCacheDownloadInfo.mDownloadedLength, mCacheDownloadInfo.mContentLength);
				}
				triggerSuccess(sectionNo);
			} finally {
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
	
	private void triggerSectionFailure(int section) {
		
	}
	
	private void triggerSuccess(int sectionNo) {
		successTag[sectionNo - 1] = true;
		checkAllSuccess();
	}

	private void checkAllSuccess() {
		boolean temp = true;
		for (int i = 0; i < successTag.length; i++) {
			if (!successTag[i]) {
				temp = false;
				break;
			}
		}
		if (temp) {
			success();
		}
	}
	
	private void success() {
		mCacheInvoker.checkFinish();
		super.onSuccess();
	}

	/* (non-Javadoc)
	 * @see org.jabe.neverland.download.core.engine.impl.DownloadTask#start()
	 */
	@Override
	public boolean start() {
		if (!hasStarted) {
			getExecutorService().execute(this);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jabe.neverland.download.core.engine.impl.DownloadTask#resume()
	 */
	@Override
	public boolean resume() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jabe.neverland.download.core.engine.impl.DownloadTask#cancel()
	 */
	@Override
	public boolean cancel() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jabe.neverland.download.core.engine.impl.DownloadTask#pause()
	 */
	@Override
	public boolean pause() {
		// TODO Auto-generated method stub
		return false;
	}
}
