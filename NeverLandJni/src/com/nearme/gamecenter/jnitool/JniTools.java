package com.nearme.gamecenter.jnitool;

public class JniTools {
	
	public static native String getFileMD5(String filePath);
	
	static {
        System.loadLibrary("GCJniTool");
    }
	
}
