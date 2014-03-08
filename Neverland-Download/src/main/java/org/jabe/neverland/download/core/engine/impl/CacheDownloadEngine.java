package org.jabe.neverland.download.core.engine.impl;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.jabe.neverland.download.core.DownloadEngine;
import org.jabe.neverland.download.core.DownloadInfo;
import org.jabe.neverland.download.core.cache.DownloadCacheManager;

public class CacheDownloadEngine implements DownloadEngine {

	protected final Map<String, MultiThreadTask> mDownloadTaskMap = Collections.synchronizedMap(new HashMap<String, MultiThreadTask>());
	protected final DownloadCacheManager mProgressCacheManager;
	protected final ExecutorService mDownloadExecutor;

	public CacheDownloadEngine(DownloadCacheManager mProgressCacheManager,
			ExecutorService executorService) {
		this.mProgressCacheManager = mProgressCacheManager;
		this.mDownloadExecutor = executorService;
	}

	@Override
	public boolean startDownload(DownloadInfo downloadInfo) {
		// TODO Auto-generated method stub
		return startD(downloadInfo);
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
		// if the task was already in the map.
		if (mDownloadTaskMap.containsKey(downloadInfo.getmPackageName())) {
			final DownloadTask downloadTask = mDownloadTaskMap.get(downloadInfo
					.getmPackageName());
			return downloadTask.start();
		} else {
			final DownloadCacheInvoker cacheInvoker = new DownloadCacheInvoker(mProgressCacheManager, downloadInfo);
			final TaskConfig taskConfig = new TaskConfig.Builder()
					.addCacheInvoker(cacheInvoker)
					.addDownloadExecutor(mDownloadExecutor)
					.addDownloadTaskListener(mGlobalTaskListener)
					.addDownloader(new BaseIODownloader())
					.build();
			final MultiThreadTask downloadTask = new MultiThreadTask(taskConfig);
			mDownloadTaskMap.put(downloadInfo.getmPackageName(), downloadTask);
			return downloadTask.start();
		}
	}
	
	protected DownloadTaskListener mGlobalTaskListener = new DownloadTaskListener() {
		
		@Override
		public void onUpdateProgress(double added, double downloaded, double total) {
			// TODO Auto-generated method stub
//			System.out.println("Downloaded Percent : " + downloaded/total);
//			System.out.println("changed by current thread " + Thread.currentThread().getName() + " " + downloaded + "/" + total);
			
		}
		
		@Override
		public void onSuccess() {
			// TODO Auto-generated method stub
			System.out.println("Download Success ");
		}
		
		@Override
		public void onFailure(Exception e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPreTask() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onResumeTask() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPauseTask() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBeforeExecute() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFileExist(File file) {
			// TODO Auto-generated method stub
			
		}
	};
}
