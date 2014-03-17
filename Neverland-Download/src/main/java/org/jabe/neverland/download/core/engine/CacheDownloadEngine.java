package org.jabe.neverland.download.core.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

import org.jabe.neverland.download.core.AbstractMessageDeliver;
import org.jabe.neverland.download.core.DownloadCacheManager;
import org.jabe.neverland.download.core.DownloadEngine;
import org.jabe.neverland.download.core.DownloadInfo;
import org.jabe.neverland.download.core.DownloadListenerWraper;
import org.jabe.neverland.download.core.IODownloader;
import org.jabe.neverland.download.core.AbstractMessageDeliver.Message;
import org.jabe.neverland.download.core.AbstractMessageDeliver.MessageListener;
import org.jabe.neverland.download.core.AbstractMessageDeliver.MessageType;
import org.jabe.neverland.download.core.AbstractMessageDeliver.StatusMessage;
import org.jabe.neverland.download.core.DownloadStatus;

public class CacheDownloadEngine extends DownloadListenerWraper implements
		DownloadEngine {

	protected final Map<String, MultiThreadTask> mDownloadTaskMap = new HashMap<String, MultiThreadTask>();
	protected final ReentrantLock mTaskLock = new ReentrantLock();
	protected final DownloadCacheManager mProgressCacheManager;
	protected final ExecutorService mDownloadExecutor;
	protected volatile IODownloader mIODownloader = null;

	private AbstractMessageDeliver mMessageDeliver;

	public CacheDownloadEngine(DownloadCacheManager mProgressCacheManager,
			ExecutorService executorService,
			AbstractMessageDeliver messageDeliver) {
		super(null);
		this.mProgressCacheManager = mProgressCacheManager;
		this.mDownloadExecutor = executorService;
		this.mMessageDeliver = messageDeliver;
		mMessageDeliver.setEngineMessger(mGlobalMessager);
	}

	private volatile MessageListener mGlobalMessager = new MessageListener() {

		@Override
		public void onFire(Message m) {
			if (m.type == MessageType.STATUS) {
				final StatusMessage statusMessage = (StatusMessage) m;
				if (statusMessage.changedStatus >= 0
						&& statusMessage.changedStatus == DownloadStatus.DOWNLOAD_STATUS_FINISHED
								.ordinal()) {
					removeTaskByPackageName(statusMessage.packageName);
				}
			}
		}
	};

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
		mTaskLock.lock();
		if (mDownloadTaskMap.containsKey(downloadInfo.getmPackageName())) {
			final DownloadTask downloadTask = mDownloadTaskMap.get(downloadInfo
					.getmPackageName());
			mTaskLock.unlock();
			return downloadTask.start();
		} else {
			final DownloadCacheInvoker cacheInvoker = new DownloadCacheInvoker(
					mProgressCacheManager, downloadInfo);
			final TaskConfig taskConfig = new TaskConfig.Builder()
					.addCacheInvoker(cacheInvoker)
					.addDownloadExecutor(mDownloadExecutor)
					.addMessageDeliver(mMessageDeliver)
					.addDownloader(
							mIODownloader != null ? mIODownloader
									: getDefaultDownloader()).build();
			final MultiThreadTask downloadTask = new MultiThreadTask(taskConfig);
			mDownloadTaskMap.put(downloadInfo.getmPackageName(), downloadTask);
			mTaskLock.unlock();
			return downloadTask.start();
		}

	}
	
	private void removeTaskByPackageName(String name) {
		mTaskLock.lock();
		if (mDownloadTaskMap.containsKey(name)) {
			final MultiThreadTask multiThreadTask = mDownloadTaskMap.get(name);
			multiThreadTask.clear();
			mDownloadTaskMap.remove(name);
		}
		mTaskLock.unlock();
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
