/**
 * Copyright (C) 2014, all rights reserved.
 * Company	SHENZHEN YUNZHONGFEI TECHNOLOGY CORP., LTD. 
 * Author	lailong
 * Since	2014年3月6日
 */
package org.jabe.neverland.download.core.engine.impl;

import java.io.File;
import java.util.concurrent.ExecutorService;

/**
 * 
 * @Author	LaiLong
 * @Since	2014年3月6日
 */
public abstract class AbstractTask extends CacheDownloadTask implements DownloadTaskListener {

	private DownloadTaskListener mInvoker;
	
	protected IODownloader mDownloader;
	
	private ExecutorService mExecutorService;
	
	/**
	 * @param cacheInvoker
	 * @param listener
	 */
	public AbstractTask(TaskConfig taskConfig) {
		super(taskConfig.mCacheInvoker);
		this.mInvoker = taskConfig.mDownloadTaskListener;
		this.mDownloader = taskConfig.mDownloader;
		this.mExecutorService = taskConfig.mDownloadExecutorService;
	}
	
	protected ExecutorService getExecutorService() {
		return mExecutorService;
	}
	

	/* (non-Javadoc)
	 * @see org.jabe.neverland.download.core.engine.impl.DownloadTaskListener#onSuccess()
	 */
	@Override
	public void onSuccess() {
		mInvoker.onSuccess();
	}
	/* (non-Javadoc)
	 * @see org.jabe.neverland.download.core.engine.impl.DownloadTaskListener#onFailure(java.lang.Exception)
	 */
	@Override
	public void onFailure(Exception e) {
		mInvoker.onFailure(e);
	}
	/* (non-Javadoc)
	 * @see org.jabe.neverland.download.core.engine.impl.DownloadTaskListener#onPreTask()
	 */
	@Override
	public void onPreTask() {
		mInvoker.onPreTask();
	}
	/* (non-Javadoc)
	 * @see org.jabe.neverland.download.core.engine.impl.DownloadTaskListener#onResumeTask()
	 */
	@Override
	public void onResumeTask() {
		mInvoker.onResumeTask();
	}
	/* (non-Javadoc)
	 * @see org.jabe.neverland.download.core.engine.impl.DownloadTaskListener#onPauseTask()
	 */
	@Override
	public void onPauseTask() {
		mInvoker.onPauseTask();
	}
	/* (non-Javadoc)
	 * @see org.jabe.neverland.download.core.engine.impl.DownloadTaskListener#onBeforeExecute()
	 */
	@Override
	public void onBeforeExecute() {
		mInvoker.onBeforeExecute();
	}
	/* (non-Javadoc)
	 * @see org.jabe.neverland.download.core.engine.impl.DownloadTaskListener#onCancel()
	 */
	@Override
	public void onCancel() {
		mInvoker.onCancel();
	}
	/* (non-Javadoc)
	 * @see org.jabe.neverland.download.core.engine.impl.DownloadTaskListener#onUpdateProgress(double, double, double)
	 */
	@Override
	public void onUpdateProgress(double added, double downloaded, double total) {
		mInvoker.onUpdateProgress(added, downloaded, total);
	}
	/* (non-Javadoc)
	 * @see org.jabe.neverland.download.core.engine.impl.DownloadTaskListener#onFileExist(java.io.File)
	 */
	@Override
	public void onFileExist(File file) {
		mInvoker.onFileExist(file);
	}

}
