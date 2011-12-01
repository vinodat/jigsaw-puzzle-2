package com.yqg.puzzle.utils;

import android.util.Log;

public class UtilFuncs {

	private static boolean enableLog = true;
	
	public static void logE(String tag,String msg){
		if(enableLog){
			Log.e(tag,msg);
		}
	}
}
