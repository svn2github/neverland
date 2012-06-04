package org.jabe.neverland.download;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

//这个是任务Bean

public class Task {
	private String downURL;
	private String saveFile;
	private int bufferSize = 64 * 1024;
	private int workerCount;
	private int sectionCount;
	private long contentLength;
	private long[] sectionsOffset;

	public static final int HEAD_SIZE = 4096;

	// 读下载描述文件内容

	public synchronized void read(RandomAccessFile file) throws IOException {

		byte[] temp = new byte[HEAD_SIZE];

		file.seek(0);

		int readed = file.read(temp);

		if (readed != temp.length) {

			throw new RuntimeException();

		}

		ByteArrayInputStream bais = new ByteArrayInputStream(temp);

		DataInputStream dis = new DataInputStream(bais);

		downURL = dis.readUTF();

		saveFile = dis.readUTF();

		sectionCount = dis.readInt();

		contentLength = dis.readLong();
		sectionsOffset = new long[sectionCount];
		for (int i = 0; i < sectionCount; i++) {
			sectionsOffset[i] = file.readLong();
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

			throw new RuntimeException();

		}
		file.seek(HEAD_SIZE);
		for (int i = 0; i < sectionsOffset.length; i++) {
			file.writeLong(sectionsOffset[i]);
		}// 这个是下载主程序
	}

	public static void main(String[] args) throws IOException {
		
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

	public static void test2() throws IOException {

		Task task = new Task();

		task.setDownURL("http://student1.scut.edu.cn:8880/manage/news/data/1208421861893.xls");

		task.setSaveFile("H:/Test3.xls");

		task.setSectionCount(5);

		task.setWorkerCount(1);

		task.setBufferSize(128 * 1024);

		TaskAssign ta = new TaskAssign();

		ta.work(task);
	}

	public static void test3() throws IOException {

		Task task = new Task();

		task.setDownURL("http://go.microsoft.com/fwlink/?linkid=57034");

		task.setSaveFile("H:/vc2005express.iso");

		task.setSectionCount(500);

		task.setWorkerCount(300);

		task.setBufferSize(128 * 1024);

		TaskAssign ta = new TaskAssign();

		ta.work(task);
	}

	public static void test4() throws IOException {

		Task task = new Task();

		task.setDownURL("http://down.sandai.net/Thunder5.7.9.472.exe");

		task.setSaveFile("H:/Thunder.exe");

		task.setSectionCount(50);

		task.setWorkerCount(50);

		task.setBufferSize(128 * 1024);

		TaskAssign ta = new TaskAssign();

		ta.work(task);
	}

	public static void test5() throws IOException {

		Task task = new Task();

		task.setDownURL("http://gamecenter.wanyol.com:8080/fs/adIcons/201203/c9a73adccf964ec7aa40bda215b29bee.jpg");

		task.setSaveFile("H:/test.jpg");

		task.setSectionCount(10);

		task.setWorkerCount(10);

		task.setBufferSize(128 * 1024);

		TaskAssign ta = new TaskAssign();

		ta.work(task);
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

}
