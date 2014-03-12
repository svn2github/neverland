/**
 * Copyright (C) 2014, all rights reserved.
 * Company	SHENZHEN YUNZHONGFEI TECHNOLOGY CORP., LTD. 
 * Author	lailong
 * Since	2014年3月6日
 */
package org.jabe.neverland.download.core.engine.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;

import org.jabe.neverland.download.core.AbstractMessageDeliver;

/**
 * 
 * @Author LaiLong
 * @Since 2014年3月6日
 */
public abstract class AbstractTask extends CacheDownloadTask implements
		IODownloader {

	private IODownloader mDownloader;

	private ExecutorService mExecutorService;
	
	private AbstractMessageDeliver mMessageDeliver;

	/**
	 * @param cacheInvoker
	 * @param listener
	 */
	public AbstractTask(TaskConfig taskConfig) {
		super(taskConfig.mCacheInvoker);
		this.mDownloader = taskConfig.mDownloader;
		this.mMessageDeliver = taskConfig.mMessageDeliver;
		this.mExecutorService = taskConfig.mDownloadExecutorService;
	}

	protected ExecutorService getExecutorService() {
		return mExecutorService;
	}

	protected void onSuccess() {
		mMessageDeliver.pushFinishMessage(getPackageName());
	}

	protected void onFailure(Exception e) {
		mMessageDeliver.pushExceptionMessage(getPackageName(), e);
	}

	protected void onPreTask() {
		// TODO
	}

	protected void onResumeTask() {
		// TODO
	}

	protected void onPauseTask() {
		// TODO
	}

	protected void onBeforeExecute() {
		// TODO
	}

	protected void onCancel() {
		// TODO
	}

	protected void onUpdateProgress(double added, double downloaded, double total) {
		mMessageDeliver.pushUpdateProgressMessage(getPackageName(), added, downloaded, total);
	}

	protected void onFileExist(File file) {
		this.onSuccess();
	}

	@Override
	public InputStream getStream(String imageUri, Object extra, SizeBean sb)
			throws IOException {
		return mDownloader.getStream(imageUri, extra, sb);
	}

}
