package org.jabe.neverland.download.core.engine.impl;

import java.io.IOException;

import org.jabe.neverland.download.core.DownloadInfo;
import org.jabe.neverland.download.core.cache.CacheDownloadInfo;
import org.jabe.neverland.download.core.cache.DownloadCacheManager;

/**
 * 
 * A task use to udpate downloaded-cache in manager
 * 
 * @author Jabe 2013-8-1
 * 
 */
public class DownloadCacheInvoker {
	
	public CacheDownloadInfo mCacheDownloadInfo;
	
	private DownloadCacheManager mProgressCacheManager;
	
	public DownloadCacheInvoker(DownloadCacheManager progressCacheManager, DownloadInfo downloadInfo) {
		this.mProgressCacheManager = progressCacheManager;
		this.mCacheDownloadInfo = new CacheDownloadInfo(downloadInfo);
	}
	
	public void readFromCache() throws IOException {
		mProgressCacheManager.readFromCache(mCacheDownloadInfo);
	}
	
	public void saveToCache() throws IOException {
		mProgressCacheManager.saveToCache(mCacheDownloadInfo);
	}
	
	public String generateCacheSaveFullPath() {
		return mProgressCacheManager.generateCacheSaveFullPath(mCacheDownloadInfo.mDownloadInfo);
	}
	
	public boolean isInCache() {
		return mProgressCacheManager.isInCache(mCacheDownloadInfo);
	}
	
	public boolean checkFinish() {
		return mProgressCacheManager.completeCacheTask(mCacheDownloadInfo);
	}
	
	public void updateSectionProgress(byte[] bytes, int sectionNo, long progress) throws IOException {
		mProgressCacheManager.updateSectionProgress(bytes, sectionNo, progress, mCacheDownloadInfo);
	}

}
