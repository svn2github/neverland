package org.jabe.neverland.download.cache;

import org.jabe.neverland.download.core.DownloadInfo;
import org.jabe.neverland.download.task.CacheTask;

public abstract class ProgressCacheManager {
	
	public abstract void readFromCache(final CacheTask cacheTask);
	
	public abstract void saveToCache(final CacheTask cacheTask);
	
	public abstract boolean isInCache(final CacheTask cacheTask);
	
	public abstract void updateSectionProgress(int sectionNo, long progress, final CacheTask cacheTask);
	
	public abstract int getDownloadedPercent(DownloadInfo downloadInfo);
	
	public abstract boolean isDownloadFinished(DownloadInfo downloadInfo);
	
}
