package com.lion.engine.util;

import android.content.Context;

public class GetResource {
	
	public static int getStringResource (Context ctx, String name){
		String packageName = ctx.getPackageName();
		try {
			Object obj;
			obj = Class.forName(packageName+".R$string").newInstance();
//			return ctx.getResources().getString(Class.forName(packageName+".R$string").getDeclaredField(name).getInt(obj));
			return Class.forName(packageName+".R$string").getDeclaredField(name).getInt(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} 
	}
	
	public static int getLayoutResource (Context ctx, String name){
		String packageName = ctx.getPackageName();
		try {
			Object obj;
			obj = Class.forName(packageName+".R$layout").newInstance();
//			return ctx.getResources().getString(Class.forName(packageName+".R$string").getDeclaredField(name).getInt(obj));
			return Class.forName(packageName+".R$layout").getDeclaredField(name).getInt(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} 
	}
	
	public static int getDrawableResource (Context ctx, String name){
		String packageName = ctx.getPackageName();
		try {
			Object obj;
			obj = Class.forName(packageName+".R$drawable").newInstance();
			return Class.forName(packageName+".R$drawable").getDeclaredField(name).getInt(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} 
	}
	
	public static int getIdResource (Context ctx, String name){
		String packageName = ctx.getPackageName();
		try {
			Object obj;
			obj = Class.forName(packageName+".R$id").newInstance();
			return Class.forName(packageName+".R$id").getDeclaredField(name).getInt(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} 
	}
	public static int getDimemResource (Context ctx, String name){
		String packageName = ctx.getPackageName();
		try {
			Object obj;
			obj = Class.forName(packageName+".R$dimen").newInstance();
			return Class.forName(packageName+".R$dimen").getDeclaredField(name).getInt(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} 
	}
	public static int getColorResource (Context ctx, String name){
		String packageName = ctx.getPackageName();
		try {
			Object obj;
			obj = Class.forName(packageName+".R$color").newInstance();
			return Class.forName(packageName+".R$color").getDeclaredField(name).getInt(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} 
	}
}
