package org.jabe.neverland.download;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultDownloadManager extends DownloadManager {

	public static void main(String[] args) {
		test3();
	}

	public static void test3() {
		
	}

	private File mDownloadPathFile;

	private String mDownloadPathString;

	private ExecutorService mExecutorService = Executors.newCachedThreadPool();

	private TaskListener mTaskListener = new TaskListener() {

		@Override
		public void onUpdateProgress(double added, double downloaded,
				double total) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSuccess() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFailure(Exception e) {
			// TODO Auto-generated method stub

		}
	};

	public DefaultDownloadManager(String path) {
		mDownloadPathString = path;
		mDownloadPathFile = new File(path);
	}

	private Map<String, TaskAssign> mTaskMap = new HashMap<String, TaskAssign>();

	@Override
	public DownloadStatus startDownload(String url, String tag) {
		if (hasFinished(url, tag)) {
			return DownloadStatus.DOWNLOAD_STATUS_FINISHED;
		}
		if (hasDownloadingFile(url, tag)) {
			resumeDownload(tag);
			return DownloadStatus.DOWNLOAD_STATUS_RESUME;
		}
		if (!isDownloading(tag)) {
			startNewTask(url, tag);
		}
		return DownloadStatus.DOWNLOAD_STATUS_STARTED;
	}

	private void startNewTask(String url, String tag) {
		final Task task = new Task();
		task.setDownURL(url);
		task.setPackageName(tag);
		task.setSaveFile(mDownloadPathString + generateFileName(url, tag));
		task.setSectionCount(1);
		task.setWorkerCount(1);
		task.setBufferSize(8 * 1024);
		final TaskAssign ta = new TaskAssign(mExecutorService);
		final TaskListener tl = mTaskListener;
		ta.setTaskListener(tl);
		ta.work(task);
		mTaskMap.put(tag, ta);
	}

	public Task getCacheDownloadTask(String url, String tag) {
		File file = new File(mDownloadPathFile, generateFileName(url, tag));
		if (file.isFile() && file.exists()) {
			Task task = new Task();
			RandomAccessFile raf = null;
			try {
				raf = new RandomAccessFile(file, "rw");
				task.read(raf);
			} catch (Exception e) {
				e.printStackTrace();
				file.delete();
				task = null;
			} finally {
				try {
					if (raf != null) {
						raf.close();
					}
				} catch (Exception e) {
				}
			}
			return task;
		} else {
			return null;
		}
	}

	@Override
	public boolean resumeDownload(String tag) {
		if (isDownloading(tag)) {
			mTaskMap.get(tag).resumeWork();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean pauseDownload(String tag) {
		if (isDownloading(tag)) {
			mTaskMap.get(tag).pauseWork();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean cancelDownload(String tag) {
		if (isDownloading(tag)) {
			mTaskMap.get(tag).cancel();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean restartDownload(String tag) {
		return false;
	}

	@Override
	public boolean hasDownloadingFile(String url, String tag) {
		return getCacheDownloadTask(url, tag) == null ? false : true;
	}

	@Override
	public boolean isDownloading(String tag) {
		return mTaskMap.containsKey(tag);
	}

	@Override
	public boolean hasFinished(String url, String tag) {
		try {
			String path = mDownloadPathString + generateFileName(url, tag);
			File file = new File(path);
			if (file.exists()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public String generateFileName(String url, String tag) {
		if (url == null)
			return null;
		int p = url.lastIndexOf("/");
		return url.substring(p + 1);
	}

}
