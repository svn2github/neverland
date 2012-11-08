package org.jabe.neverland.download;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jabe.neverland.download.exception.ReadTaskFileException;

//这个是任务Bean

public class Task {
	private String downURL;
	private String saveFile;
	private int bufferSize = 4 * 1024;//64K, 4K
	private int workerCount;
	private int sectionCount;
	private long contentLength;
	private volatile long downloadedLength = 0;
	private long[] sectionsOffset;

	public static final int HEAD_SIZE = 1024;//单位字节

	// 读下载描述文件内容

	public synchronized void read(RandomAccessFile file) throws ReadTaskFileException {
		try {
			file.seek(0);
			downURL = file.readUTF();

			saveFile = file.readUTF();

			sectionCount = file.readInt();

			contentLength = file.readLong();

			file.seek(HEAD_SIZE);
			downloadedLength = file.readLong();

			sectionsOffset = new long[sectionCount];
			for (int i = 0; i < sectionCount; i++) {
				sectionsOffset[i] = file.readLong();
			}
		} catch (Exception e) {
			throw new ReadTaskFileException(e.getMessage());
		}

	}

	public synchronized void create(RandomAccessFile file) throws IOException {

		if (sectionCount != sectionsOffset.length) {

			throw new RuntimeException();

		}

		long len = HEAD_SIZE + 8 * sectionCount;

		file.setLength(len);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		DataOutputStream dos = new DataOutputStream(baos);

		dos.writeUTF(downURL);

		dos.writeUTF(saveFile);

		dos.writeInt(sectionCount);

		dos.writeLong(contentLength);

		byte[] src = baos.toByteArray();

		byte[] temp = new byte[HEAD_SIZE];

		System.arraycopy(src, 0, temp, 0, src.length);

		file.seek(0);

		file.write(temp);

		writeOffset(file);
	}

	// 更新下载的过程
	public synchronized void writeOffset(RandomAccessFile file)
			throws IOException {
		if (sectionCount != sectionsOffset.length) {
			throw new RuntimeException("sectionCount != sectionsOffset.length");
		}
		file.seek(HEAD_SIZE);
		file.writeLong(downloadedLength);//write default downloaded count
		for (int i = 0; i < sectionsOffset.length; i++) {
			file.writeLong(sectionsOffset[i]);
		}
	}

	public synchronized void updateDownloadedLength(long added) {
		downloadedLength += added;
	}

	public static void main(String[] args) {

		long oldTime = System.currentTimeMillis();

		System.out.println("Start Download");

		test3();

		System.out.println("\n\n===============\nFinished. Total Cast : " + ((double)(System.currentTimeMillis() - oldTime))/(double)60000 + "min");
	}

	public static void test1() throws IOException {

		Task task = new Task();

		task.setDownURL("http://61.152.235.21/qqfile/qq/2007iistable/QQ2007IIKB1.exe");

		task.setSaveFile("H:/Test2.exe");

		task.setSectionCount(200);

		task.setWorkerCount(100);

		task.setBufferSize(256 * 1024);

		TaskAssign ta = new TaskAssign();

		ta.work(task);
	}

	public static void test2() {

		Task task = new Task();

		task.setDownURL("http://student1.scut.edu.cn:8880/manage/news/data/1208421861893.xls");

		task.setSaveFile("H:/Test3.xls");

		task.setSectionCount(5);

		task.setWorkerCount(1);

		task.setBufferSize(128 * 1024);

		TaskAssign ta = new TaskAssign();

		ta.work(task);
	}

	public static void test3() {
		final String url = "http://netstorage.unity3d.com/unity/UnitySetup-3.5.6.exe";
		Task task = new Task();
		task.setDownURL(url);
		try {
			task.setContentLength(getContentLength(url));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		task.setSaveFile("H:/UnitySetup-3.5.6.exe");
		task.setSectionCount(100);
		task.setWorkerCount(100);
		task.setBufferSize(8 * 1024);
		TaskAssign ta = new TaskAssign();
		ta.setTaskListener(new TaskListener() {
			@Override
			public void onSuccess() {

			}

			@Override
			public void onFailure(Exception e) {

			}

			@Override
			public void onUpdateProgress(double added, double downloaded,
					double total) {
				System.out.println("current percent :" + ((downloaded / total) * 100));
			}
		});
		ta.work(task);
	}

	public static void test4() {

		Task task = new Task();

		task.setDownURL("http://down.sandai.net/Thunder5.7.9.472.exe");

		task.setSaveFile("H:/Thunder.exe");

		task.setSectionCount(50);

		task.setWorkerCount(50);

		task.setBufferSize(128 * 1024);

		TaskAssign ta = new TaskAssign();

		ta.work(task);
	}

	public static void test5() {

		Task task = new Task();

		task.setDownURL("http://gamecenter.wanyol.com:8080/fs/adIcons/201203/c9a73adccf964ec7aa40bda215b29bee.jpg");

		task.setSaveFile("H:/test.jpg");

		task.setSectionCount(10);

		task.setWorkerCount(10);

		task.setBufferSize(128 * 1024);

		TaskAssign ta = new TaskAssign();

		ta.work(task);
	}
	public static double currentCount = 0;
	public static void showPercent(double add, double total) {
		currentCount += add;
		System.out.println("Total percent : "
				+ (currentCount / total) * 100 + "%");
	}


	public static long getContentLength(String url) throws IOException {
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		try {
			return conn.getContentLength();
		} finally {
			conn.disconnect();
		}
	}

	public String getDownURL() {
		return downURL;
	}

	public void setDownURL(String downURL) {
		this.downURL = downURL;
	}

	public String getSaveFile() {
		return saveFile;
	}

	public void setSaveFile(String saveFile) {
		this.saveFile = saveFile;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getWorkerCount() {
		return workerCount;
	}

	public void setWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}

	public int getSectionCount() {
		return sectionCount;
	}

	public void setSectionCount(int sectionCount) {
		this.sectionCount = sectionCount;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public long[] getSectionsOffset() {
		return sectionsOffset;
	}

	public void setSectionsOffset(long[] sectionsOffset) {
		this.sectionsOffset = sectionsOffset;
	}

	public long getDownloadedLength() {
		return downloadedLength;
	}

}
