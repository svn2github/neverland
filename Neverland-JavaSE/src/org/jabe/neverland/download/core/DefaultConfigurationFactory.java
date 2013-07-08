package org.jabe.neverland.download.core;

import org.jabe.neverland.download.cache.FileCacheManager;
import org.jabe.neverland.download.cache.ProgressCacheManager;
import org.jabe.neverland.download.core.impl.DefaultOperationMaper;
import org.jabe.neverland.download.core.impl.DownloadTaskEngine;

public class DefaultConfigurationFactory {

	public static DownloadEngine getDefaultDownloadEngine(ProgressCacheManager progressCacheManager) {
		return new DownloadTaskEngine(progressCacheManager);
	}
	
	public static DownloadOperationMaper getDefaultMaper(DownloadEngine downloadEngine) {
		return new DefaultOperationMaper(downloadEngine);
	}
	
	public static ProgressCacheManager getDefaultCacheManager(String rootPath) {
		return new FileCacheManager(rootPath);
	}
}
