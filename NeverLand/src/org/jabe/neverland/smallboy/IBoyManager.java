package org.jabe.neverland.smallboy;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;

public abstract class IBoyManager {
	
	private HashMap<Integer, IBoy> mBoyMap = new HashMap<Integer, IBoy>();

	protected final void addBoy(IBoy boy) {
		mBoyMap.put(boy.hashCode(), boy);
	}
	
	protected final void removeBoy(IBoy boy) {
		mBoyMap.remove(boy.hashCode());
	}
	
	protected final IBoy getBoy(String name) {
		return mBoyMap.get(name);
	}
	
	protected final boolean hasBoy(IBoy boy) {
		return mBoyMap.containsKey(boy.hashCode());
	}
	
	public abstract void showBoy(Activity activity, IBoy boy);
	
	public abstract void dismisBoy(Activity activity, IBoy boy);
	
	public abstract void showBoy(Context context, IBoy boy);
	
	public abstract void dismisBoy(Context context, IBoy boy);
	
}
