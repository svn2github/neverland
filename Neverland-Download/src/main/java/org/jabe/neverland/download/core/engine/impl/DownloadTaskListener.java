package org.jabe.neverland.download.core.engine.impl;

import java.io.File;

public interface DownloadTaskListener {

	public void onSuccess();

	/**
	 * 异常可能来自不同的逻辑层次,有可能是主控的Thread,也有可能是Worker Thread.
	 * 
	 * @param e
	 */
	public void onFailure(Exception e);

	public void onPreTask();

	public void onResumeTask();

	public void onPauseTask();

	public void onBeforeExecute();

	public void onCancel();

	/**
	 * Never assigned main-thread call this method, you can count the current
	 * downloaded number by yourself.
	 * 
	 * @param added
	 * @param downloaded
	 *            has added the increment
	 * @param total
	 */
	public void onUpdateProgress(double added, double downloaded,
			double total);

	public void onFileExist(File file);
}
