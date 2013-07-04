package org.jabe.neverland.download.task;

import org.jabe.neverland.download.core.DownloadInfo;

public abstract class CacheTask {
	
	protected long mDownloadedLength = 0;
	
	protected long[] mSectionsOffset;
	protected int mWorkerCount;
	protected int mSectionCount;
	
	protected long mContentLength;
	protected String mDownloadUrl;
	protected String mSaveFileFullPath;
	
	
	private DownloadInfo mDownloadInfo;
	
	protected void initSectionOffset() {
		final long per = mContentLength / mSectionCount;
		mSectionsOffset = new long[mSectionCount];
		for (int i = 0; i < mSectionCount; i++) {
			mSectionsOffset[i] = per * i;
		}
	}
	

	public DownloadInfo getmDownloadInfo() {
		return mDownloadInfo;
	}

	public void setmDownloadInfo(DownloadInfo mDownloadInfo) {
		this.mDownloadInfo = mDownloadInfo;
	}
	
	public abstract void readFromCache();
	
	public abstract void saveToCache();
	
	public abstract boolean isInCache();
	
	public abstract void updateSectionProgress(int sectionNo, long progress);
}
