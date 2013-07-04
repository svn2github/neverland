package org.jabe.neverland.download.task;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import org.jabe.neverland.download.cache.ProgressCacheManager;
import org.jabe.neverland.download.core.DownloadInfo;

public class CacheTask {
	
	public long mDownloadedLength = 0;
	
	public long[] mSectionsOffset;
	public int mWorkerCount;
	public int mSectionCount;
	
	public long mContentLength;
	public String mDownloadUrl;
	public String mSaveFileFullPath;
	
	public DownloadInfo mDownloadInfo;
	
	private ProgressCacheManager mProgressCacheManager;
	private ReentrantLock mDownloadedLock;
	
	private CacheTask(ProgressCacheManager progressCacheManager) {
		this.mProgressCacheManager = progressCacheManager;
	}
	
	protected void initSectionOffset() {
		final long per = mContentLength / mSectionCount;
		mSectionsOffset = new long[mSectionCount];
		for (int i = 0; i < mSectionCount; i++) {
			mSectionsOffset[i] = per * i;
		}
	}

	public DownloadInfo getmDownloadInfo() {
		return mDownloadInfo;
	}

	public void setmDownloadInfo(DownloadInfo mDownloadInfo) {
		this.mDownloadInfo = mDownloadInfo;
	}
	
	public void readFromCache() throws IOException {
		mProgressCacheManager.readFromCache(this);
	}
	
	public void saveToCache() throws IOException {
		mProgressCacheManager.saveToCache(this);
	}
	
	public boolean isInCache() {
		return mProgressCacheManager.isInCache(this);
	}
	
	public void updateSectionProgress(int sectionNo, long progress) throws IOException {
		mDownloadedLock.lock();
		mDownloadedLength =+ progress;
		mDownloadedLock.unlock();
		mProgressCacheManager.updateSectionProgress(sectionNo, progress, this);
	}
}
