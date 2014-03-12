package org.jabe.neverland.download.core.engine.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.jabe.neverland.download.core.AbstractMessageDeliver;
import org.jabe.neverland.download.core.DownloadEngine;
import org.jabe.neverland.download.core.DownloadInfo;
import org.jabe.neverland.download.core.DownloadListenerWraper;
import org.jabe.neverland.download.core.cache.DownloadCacheManager;

public class CacheDownloadEngine extends DownloadListenerWraper implements DownloadEngine {

	protected final Map<String, MultiThreadTask> mDownloadTaskMap = Collections.synchronizedMap(new HashMap<String, MultiThreadTask>());
	protected final DownloadCacheManager mProgressCacheManager;
	protected final ExecutorService mDownloadExecutor;
	protected volatile IODownloader mIODownloader = null;
	
	private AbstractMessageDeliver mMessageDeliver;

	public CacheDownloadEngine(DownloadCacheManager mProgressCacheManager,
			ExecutorService executorService, AbstractMessageDeliver messageDeliver) {
		super(null);
		this.mProgressCacheManager = mProgressCacheManager;
		this.mDownloadExecutor = executorService;
		this.mMessageDeliver = messageDeliver;
	}

	@Override
	public boolean startDownload(DownloadInfo downloadInfo) {
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
					.addMessageDeliver(mMessageDeliver)
					.addDownloader(mIODownloader != null ? mIODownloader : getDefaultDownloader())
					.build();
			final MultiThreadTask downloadTask = new MultiThreadTask(taskConfig);
			mDownloadTaskMap.put(downloadInfo.getmPackageName(), downloadTask);
			return downloadTask.start();
		}
	}
	
	private IODownloader defaultDownloader = null;
	private IODownloader getDefaultDownloader() {
		if (defaultDownloader == null) {
			defaultDownloader = new BaseIODownloader();
		}
		return defaultDownloader;
	}

	@Override
	public void setIoDownloader(IODownloader downloader) {
		this.mIODownloader = downloader;
	}
}
