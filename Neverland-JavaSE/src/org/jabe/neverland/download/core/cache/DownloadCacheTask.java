package org.jabe.neverland.download.core.cache;

import java.io.IOException;

import org.jabe.neverland.download.core.DownloadInfo;

public class DownloadCacheTask {
	
	public long mDownloadedLength = 0;
	
	public long[] mSectionsOffset;
	public int mWorkerCount;
	public int mSectionCount;
	
	public long mContentLength;
	public DownloadInfo mDownloadInfo;
	
	private DownloadCacheManager mProgressCacheManager;
	
	public DownloadCacheTask(DownloadCacheManager progressCacheManager, DownloadInfo downloadInfo) {
		this.mProgressCacheManager = progressCacheManager;
	}
	
	protected void initSectionOffset() {
		final long per = mContentLength / mSectionCount;
		mSectionsOffset = new long[mSectionCount];
		for (int i = 0; i < mSectionCount; i++) {
			mSectionsOffset[i] = per * i;
		}
	}
	
	public void readFromCache() throws IOException {
		mProgressCacheManager.readFromCache(this);
	}
	
	public void saveToCache() throws IOException {
		mProgressCacheManager.saveToCache(this);
	}
	
	public String generateCacheSaveFullPath() {
		return mProgressCacheManager.generateCacheSaveFullPath(mDownloadInfo);
	}
	
	public boolean isInCache() {
		return mProgressCacheManager.isInCache(this);
	}
	
	public boolean checkFinish() {
		return mProgressCacheManager.completeCacheTask(this);
	}
	
	public void updateSectionProgress(int sectionNo, long progress) throws IOException {
		mProgressCacheManager.updateSectionProgress(sectionNo, progress, this);
	}

}
