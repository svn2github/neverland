/**
 * Copyright (C) 2014, all rights reserved.
 * Company	SHENZHEN YUNZHONGFEI TECHNOLOGY CORP., LTD. 
 * Author	lailong
 * Since	2014年3月6日
 */
package org.jabe.neverland.download.core.cache;

import org.jabe.neverland.download.core.DownloadInfo;

/**
 * 
 * @Author LaiLong
 * @Since 2014年3月6日
 */
public class CacheDownloadInfo {

	public long mDownloadedLength = 0;

	public long[] mSectionsOffset;
	public int mWorkerCount = 0;
	public int mSectionCount = 0;

	public long mContentLength = 0;
	public DownloadInfo mDownloadInfo;
	
	public CacheDownloadInfo(DownloadInfo downloadInfo) {
		this.mDownloadInfo = downloadInfo;
		init();
	}
	
	private void init() {
		mSectionCount = mDownloadInfo.getInt(DownloadInfo.P_SECTION_COUNT, 1);
		mWorkerCount = mDownloadInfo.getInt(DownloadInfo.P_WORKER_COUNT, 1);
		initSectionOffset();
	}

	private void initSectionOffset() {
		mSectionsOffset = new long[mSectionCount];
		for (int i = 0; i < mSectionCount; i++) {
			mSectionsOffset[i] = 0;
		}
	}
}
