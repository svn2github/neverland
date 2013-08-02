package org.jabe.neverland.download.core.cache.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.jabe.neverland.download.core.DownloadInfo;
import org.jabe.neverland.download.core.cache.CacheAccessException;
import org.jabe.neverland.download.core.cache.CacheReadException;
import org.jabe.neverland.download.core.cache.CacheWriteException;
import org.jabe.neverland.download.core.cache.DownloadCacheManager;
import org.jabe.neverland.download.core.cache.DownloadCacheTask;
import org.jabe.neverland.download.util.IoUtils;

public class FileCacheManager extends DownloadCacheManager {

	private static final String TAG = FileCacheManager.class.getSimpleName();
	
	private static final String APPEND_TASKFILE = ".r_task";
	private static final String APPEND_SAVEFILE = ".r_save";
	private static final String APPEND_FINISHFILE = ".apk";
	
	private static final String RANDOM_FILE_MODE = "rw";
	private static final int HEAD_SIZE = 2 * 1024;// 单位字节
	private static final int CACHE_VERSION = 1;
	
	private String CACHE_ROOT;

	public FileCacheManager(String cacheRoot) {
		super();
		CACHE_ROOT = cacheRoot;
	}

	@Override
	public void readFromCache(DownloadCacheTask cacheTask) throws IOException {
		synchronized (cacheTask) {
			final File f = getTaskFile(cacheTask.mDownloadInfo);
			final RandomAccessFile raf = new RandomAccessFile(f,
					RANDOM_FILE_MODE);
			read(raf, cacheTask);
		}
	}

	@Override
	public void saveToCache(DownloadCacheTask cacheTask) throws IOException {
		synchronized (cacheTask) {
			final File f = getTaskFile(cacheTask.mDownloadInfo);
			final RandomAccessFile raf = new RandomAccessFile(f,
					RANDOM_FILE_MODE);
			create(raf, cacheTask);
		}
	}

	@Override
	public boolean isInCache(DownloadCacheTask cacheTask) {
		final File taskFile = getTaskFile(cacheTask.mDownloadInfo);
		if (taskFile.exists()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void updateSectionProgress(int sectionNo, long progress,
			DownloadCacheTask cacheTask) throws IOException{
		synchronized (cacheTask) {
			cacheTask.mDownloadedLength = cacheTask.mDownloadedLength + progress;
			cacheTask.mSectionsOffset[sectionNo-1] = cacheTask.mSectionsOffset[sectionNo-1] + progress;
			System.out.println("changed by current thread " + Thread.currentThread().getName() + " " + cacheTask.mDownloadedLength + "/" + cacheTask.mContentLength);
			final File f = getTaskFile(cacheTask.mDownloadInfo);
			final RandomAccessFile raf = new RandomAccessFile(f,
					RANDOM_FILE_MODE);
			updateProgress(raf, cacheTask);
		}
	}

	@Override
	public int getDownloadedPercent(DownloadInfo downloadInfo) {
		if (isDownloadFinished(downloadInfo)) {
			return 100;
		}
		final File file = new File(generateCacheSaveFullPath(downloadInfo));
		if (!file.exists()) {
			return 0;
		}
		final long saveFileLength = file.length();
		if (saveFileLength <= 0) {
			return 0;
		} else {
			return readPercent(downloadInfo);
		}
	}

	@Override
	public boolean isDownloadFinished(DownloadInfo downloadInfo) {
		final File file = new File(generateFinishPath(downloadInfo));
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String generateCacheSaveFullPath(DownloadInfo downloadInfo) {
		final File file = new File(CACHE_ROOT, md5(downloadInfo.getmPackageName()) + APPEND_SAVEFILE);
		return file.getAbsolutePath();
	}
	
	private File getTaskFile(DownloadInfo downloadInfo) {
		final File file = new File(CACHE_ROOT, md5(downloadInfo.getmPackageName()) + APPEND_TASKFILE);
		return file;
	}
	

	private void read(final RandomAccessFile file, final DownloadCacheTask cacheTask)
			throws CacheAccessException {
		try {
			file.seek(0);
			// read cache version 
			final int readVersion = file.readInt();
			if (readVersion != CACHE_VERSION) {
				throw new CacheReadException(TAG + ": cache version changed , need clear cache and create new!");
			}
			// read download url
			cacheTask.mDownloadInfo.setmDownloadUrl(file.readUTF());
			// read package name
			cacheTask.mDownloadInfo.setmPackageName(file.readUTF());
			// read section count
			cacheTask.mSectionCount = file.readInt();
			// read contentLength
			final long contentLength = file.readLong();
			if (cacheTask.mContentLength != contentLength) {
				throw new CacheReadException(TAG + ": cache length not equal the new !");
			} else {
				cacheTask.mContentLength = contentLength;
			}
			// read name
			cacheTask.mDownloadInfo.setmName(file.readUTF());
			// read icon Url
			cacheTask.mDownloadInfo.setmIconUrl(file.readUTF());
			// read id
			cacheTask.mDownloadInfo.setmId(String.valueOf(file.readInt()));
			// write head end~
			file.seek(HEAD_SIZE);
			cacheTask.mDownloadedLength = file.readLong();
			cacheTask.mSectionsOffset = new long[cacheTask.mSectionCount];
			for (int i = 0; i < cacheTask.mSectionCount; i++) {
				cacheTask.mSectionsOffset[i] = file.readLong();
			}
		} catch (Exception e) {
			final File f = getTaskFile(cacheTask.mDownloadInfo);
			f.delete();
			throw new CacheReadException(e.getMessage());
		} finally {
			IoUtils.closeSilently(file);
		}
	}
	
	private int readPercent(DownloadInfo downloadInfo) {
		RandomAccessFile raf = null;
		try {
			final File file = getTaskFile(downloadInfo);
			if (!file.exists()) {
				return 0;
			}
			raf = new RandomAccessFile(file, RANDOM_FILE_MODE);
			raf.seek(0);
			raf.readInt();// version
			raf.readUTF();
			raf.readUTF();
			raf.readUTF();
			raf.readInt();
			final long contentLength = raf.readLong();
			raf.seek(HEAD_SIZE);
			final long downloadedLength = raf.readLong();
			return (int)(downloadedLength/contentLength * 100);
		} catch (IOException e) {
			return 0;
		} finally {
			IoUtils.closeSilently(raf);
		}
	}

	private void create(final RandomAccessFile file, final DownloadCacheTask cacheTask)
			throws CacheAccessException {
		try {
			final DownloadInfo downloadInfo = cacheTask.mDownloadInfo;
			final long len = HEAD_SIZE + 8 * (cacheTask.mSectionCount + 1);
			file.setLength(len);
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final DataOutputStream dos = new DataOutputStream(baos);

			dos.writeInt(CACHE_VERSION);
			
			dos.writeUTF(downloadInfo.getmDownloadUrl());

			dos.writeUTF(downloadInfo.getmPackageName());

			dos.writeInt(cacheTask.mSectionCount);

			dos.writeLong(cacheTask.mContentLength);

			dos.writeUTF(downloadInfo.getmName());

			dos.writeUTF(downloadInfo.getmIconUrl());

			try {
				dos.writeInt(Integer.valueOf(downloadInfo.getmId()));
			} catch (Exception e) {
				throw new CacheWriteException(
						TAG + ": create file io error, id is not a integer!");
			}

			byte[] src = baos.toByteArray();

			byte[] temp = new byte[HEAD_SIZE];
			System.arraycopy(src, 0, temp, 0, src.length);
			file.seek(0);
			file.write(temp);
			updateFile(file, cacheTask);
		} catch (IOException e) {
			throw new CacheWriteException(e.getMessage());
		} finally {
			IoUtils.closeSilently(file);
		}
	}
	
	private void updateProgress(final RandomAccessFile file, final DownloadCacheTask cacheTask) throws CacheAccessException  {
		try {
			updateFile(file, cacheTask);
		} catch (IOException e) {
			throw new CacheWriteException(e.getMessage());
		} finally {
			IoUtils.closeSilently(file);
		}
	}

	private void updateFile(final RandomAccessFile file,
			final DownloadCacheTask cacheTask) throws IOException {
		file.seek(HEAD_SIZE);
		file.writeLong(cacheTask.mDownloadedLength);// write default
													// downloaded count
		for (int i = 0; i < cacheTask.mSectionsOffset.length; i++) {
			file.writeLong(cacheTask.mSectionsOffset[i]);
		}
	}
	
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F' };

	public static String toHexString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}
	
	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public boolean completeCacheTask(DownloadCacheTask cacheTask) {
		final File saveFile = new File(generateCacheSaveFullPath(cacheTask.mDownloadInfo));
		final File taskFIle = getTaskFile(cacheTask.mDownloadInfo);
		taskFIle.delete();
		if (!saveFile.exists()) {
			return false;
		} else {
			if (saveFile.length() == cacheTask.mContentLength) {
				if (saveFile.renameTo(new File(generateFinishPath(cacheTask.mDownloadInfo)))) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	@Override
	public String generateFinishPath(DownloadInfo downloadInfo) {
		final File file = new File(CACHE_ROOT, md5(downloadInfo.getmPackageName()) + APPEND_FINISHFILE);
		return file.getAbsolutePath();
	}
}
