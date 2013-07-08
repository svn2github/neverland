package org.jabe.neverland.download.core.impl;

import java.util.HashMap;
import java.util.Map;

import org.jabe.neverland.download.cache.ProgressCacheManager;
import org.jabe.neverland.download.core.DownloadEngine;
import org.jabe.neverland.download.core.DownloadInfo;
import org.jabe.neverland.download.task.DownloadTask;

public class DownloadTaskEngine implements DownloadEngine {
	
	protected final Map<String, DownloadTask> mDownloadTaskMap = new HashMap<String, DownloadTask>();
	protected final ProgressCacheManager mProgressCacheManager;
	
	public DownloadTaskEngine(ProgressCacheManager mProgressCacheManager) {
		this.mProgressCacheManager = mProgressCacheManager;
	}

	@Override
	public boolean startDownload(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean resumeDownload(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pauseDownload(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelDownload(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean restartDownload(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		return false;
	}
	
	protected boolean startD(DownloadInfo downloadInfo) {
		
		if (mDownloadTaskMap.containsKey(downloadInfo.getmPackageName())) {
			final DownloadTask downloadTask = mDownloadTaskMap.get(downloadInfo.getmPackageName());
			return downloadTask.startDownload();
		} else {
//			final DownloadTask downloadTask = new DownloadTask(mTaskConfig, mCacheTask);
		}
		return false;
	}
}
