package org.jabe.neverland.download.cache;

import org.jabe.neverland.download.core.DownloadInfo;
import org.jabe.neverland.download.task.CacheTask;

public class FileCacheManager extends ProgressCacheManager {

	@Override
	public void readFromCache(CacheTask cacheTask) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveToCache(CacheTask cacheTask) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isInCache(CacheTask cacheTask) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateSectionProgress(int sectionNo, long progress,
			CacheTask cacheTask) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDownloadedPercent(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isDownloadFinished(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		return false;
	}

}
