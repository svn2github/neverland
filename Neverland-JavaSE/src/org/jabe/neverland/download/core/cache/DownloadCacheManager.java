package org.jabe.neverland.download.core.cache;

import org.jabe.neverland.download.core.DownloadInfo;

public abstract class DownloadCacheManager {
	
	public abstract void readFromCache(final DownloadCacheTask cacheTask);
	
	public abstract void saveToCache(final DownloadCacheTask cacheTask);
	
	public abstract boolean isInCache(final DownloadCacheTask cacheTask);
	
	public abstract void updateSectionProgress(final int sectionNo, final long progress, final DownloadCacheTask cacheTask);
	
	public abstract int getDownloadedPercent(final DownloadInfo downloadInfo);
	
	public abstract boolean isDownloadFinished(final DownloadInfo downloadInfo);
	
	public abstract String generateCacheSaveFullPath(final DownloadInfo downloadInfo);
	
	public abstract String generateFinishPath(final DownloadInfo downloadInfo);
	
	public abstract boolean completeCacheTask(final DownloadCacheTask cacheTask);
	
}
