package org.jabe.neverland.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskAssign {

	public static long currentCount = 0;
	public static final String TAG = "TaskAssign";

	public static void showPercent(int add, long total) {
		currentCount += add;
		System.out.println("Total percent : "
				+ ((double) currentCount / (double) total) * 100 + "%");
	}

	private TaskListener taskListener;

	public void work(Task task) {

		if (taskListener == null) {
			throw new RuntimeException("task listener can not be null!");
		}

		// TaskListener lifecycle
		taskListener.onPreTask();

		File file = new File(task.getSaveFile());
		if (file.exists()) {
			return;
		}

		// 这个是记录是否下载成功。我这里也没有增加失败回复、重试之类的工作。

		final AtomicBoolean success = new AtomicBoolean(true);

		// 任务描述文件

		File taskFile = new File(task.getSaveFile() + ".r_task");

		// 真正下载的文件

		File saveFile = new File(task.getSaveFile() + ".r_save");

		boolean taskFileExist = taskFile.exists();

		RandomAccessFile taskRandomFile = null;

		RandomAccessFile downRandomFile = null;

		try {

			taskRandomFile = new RandomAccessFile(taskFile, "rw");

			downRandomFile = new RandomAccessFile(saveFile, "rw");

			long rtnLen = taskListener.getContentLength();

			Loger.log(TAG, "get length : " + (double) rtnLen / 1024 + "K");

			if (!taskFileExist) {

				// 如果文件不存在，就初始化任务文件和下载文件

				task.setContentLength(rtnLen);

				initTaskFile(taskRandomFile, task);

				downRandomFile.setLength(rtnLen);

			} else {

				// 任务文件存在就读取任务文件

				taskListener.resumeTask();

				task.read(taskRandomFile);

				if (task.getContentLength() != rtnLen) {

					throw new RuntimeException(
							"saved content length not equal listenter's assigned length while resume task");

				}

			}

			int secCount = task.getSectionCount();

			// 分配线程去下载，这里用到线程池

			taskListener.onBeforeExecute();

			ExecutorService es = Executors.newFixedThreadPool(task
					.getWorkerCount());

			Loger.log(TAG, "start worker : " + secCount);

			for (int i = 0; i < secCount; i++) {

				final int j = i;

				final Task t = task;

				final RandomAccessFile f1 = taskRandomFile;

				final RandomAccessFile f2 = downRandomFile;

				final long length = t.getContentLength();

				final long start = t.getSectionsOffset()[j];

				long endt = -1;

				long per = 0;

				// 这里要注意一下，这里是计算当前块的长度

				if (j < t.getSectionCount() - 1) {

					per = t.getContentLength() / t.getSectionCount();

					endt = per * (j + 1);

				} else {

					endt = t.getContentLength();

					per = endt - start;

				}

				final long end = endt;

				if (start >= end) {
					Loger.log(TAG, "Section has finished before. " + j);
					continue;
				}

				es.execute(new Runnable() {
					public void run() {
						try {
							down(f1, f2, t, start, end, length, j);
						} catch (IOException e) {
							success.set(false);
							Loger.log(TAG,
									"runnable worker error : " + e.getMessage());
						}
					}

				});

			}

			es.shutdown();

			es.awaitTermination(24 * 3600, TimeUnit.SECONDS);

			taskRandomFile.close();

			taskRandomFile = null;

			downRandomFile.close();

			downRandomFile = null;

			// 如果下载成功，去掉任务描述文件、帮下载文件改名

			if (success.get()) {

				taskFile.delete();

				saveFile.renameTo(file);

				taskListener.onSuccess();

			} else {
				taskListener.onFailure();
			}

		} catch (Exception e) {
			Loger.log(TAG, "Main worker error : " + e.getMessage());
			taskListener.onFailure();
		} finally {
			if (taskRandomFile != null) {
				try {
					taskRandomFile.close();
				} catch (IOException e) {
				}
				taskRandomFile = null;
			}
			if (downRandomFile != null) {
				try {
					downRandomFile.close();
				} catch (IOException e) {

				}
				downRandomFile = null;
			}
		}
	}

	/**
	 * open connection to download data from start to end.
	 * 
	 * @param taskRandomFile
	 * @param downRandomFile
	 * @param task
	 * @param start
	 * @param end
	 * @param length
	 * @param sectionNo
	 *            index
	 * @throws IOException
	 */
	public void down(RandomAccessFile taskRandomFile,
			RandomAccessFile downRandomFile, Task task, long start, long end,
			long length, int sectionNo) throws IOException {

		long oldTime = System.currentTimeMillis();

		// 这里我用HttpURLConnection下载，你也可以用HttpClient或者自己实现一个Http协议（不过貌似没有必要）

		URL u = new URL(task.getDownURL());

		HttpURLConnection conn = (HttpURLConnection) u.openConnection();

		String range = "bytes=" + start + "-" + (end - 1);

		conn.setRequestProperty("Range", range);

		conn.setRequestProperty("User-Agent", "Ray-Downer");
		try {

			conn.connect();

			if (conn.getResponseCode() != 206) {

				throw new RuntimeException();

			}

			if (conn.getContentLength() != (end - start)) {

				throw new RuntimeException();

			}

			InputStream is = conn.getInputStream();

			byte[] temp = new byte[task.getBufferSize()];// 8KB

			BufferedInputStream bis = new BufferedInputStream(is, temp.length);

			int readed = 0;

			int total = 0;

			while ((readed = bis.read(temp)) > 0) {

				total += readed;

				long offset = task.getSectionsOffset()[sectionNo];

				synchronized (task) {

					// 下载之后顺便更新描述文件，你可能会发现这里效率比较低，在一个线程同步里进行两次文件操作。你可以自己实现一个缓冲写。

					downRandomFile.seek(offset);

					downRandomFile.write(temp, 0, readed);

					offset += readed;

					task.getSectionsOffset()[sectionNo] = offset;

					task.writeOffset(taskRandomFile);

					taskListener.onUpdateProgress(readed, length);

					showPercent(readed, length);
				}

			}

		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		Loger.log(
				TAG,
				"Section finished. " + sectionNo + "cast: "
						+ (System.currentTimeMillis() - oldTime));
	}

	public void initTaskFile(RandomAccessFile taskRandomFile, Task task)
			throws IOException {

		int secCount = task.getSectionCount();

		long per = task.getContentLength() / secCount;

		long[] sectionsOffset = new long[secCount];

		for (int i = 0; i < secCount; i++) {

			sectionsOffset[i] = per * i;

		}

		task.setSectionsOffset(sectionsOffset);

		task.create(taskRandomFile);

	}

	public long getContentLength(String url) throws IOException {
		System.out.println("begin to get contentLength");
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		try {
			return conn.getContentLength();
		} finally {
			conn.disconnect();
		}
	}

	public interface TaskListener {
		public long getContentLength();

		public void onSuccess();

		public void onFailure();

		public void onPreTask();

		public void resumeTask();

		public void onBeforeExecute();

		/**
		 * not assigned main thread will call this method, you may count the
		 * current downloaded number by yourself.
		 * 
		 * @param added
		 *            new added data count
		 * @param total
		 *            total data count
		 */
		public void onUpdateProgress(double added, double total);
	}

	public void setTaskListener(TaskListener taskListener) {
		this.taskListener = taskListener;
	}
}