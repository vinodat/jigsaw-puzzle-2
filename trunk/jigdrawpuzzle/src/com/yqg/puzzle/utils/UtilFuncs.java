package com.yqg.puzzle.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class UtilFuncs {
	private static final String TAG = "UtilFuncs";
	private static boolean enableLog = true;
	private static int mLastStrId = 0;
	private static Toast toast = null;
	
	public static void logE(String tag,String msg){
		if(enableLog){
			Log.e(tag,msg);
		}
	}
	
	public static void showToast(Context context,int strId){
		if(strId != mLastStrId){
			mLastStrId =strId;	
			if(toast == null){
				toast = Toast.makeText(context, strId, Toast.LENGTH_LONG);
			}else{
				toast.setText(strId);
			}
		}
		toast.show();
	}
	
	//decodes image and scales it to reduce memory consumption.from www.
	public static Bitmap decodeFile(String path,int desWidth,int desHeight){
    	Bitmap result = null;
    	File f = null;
    	FileInputStream fileInputStream = null;
        try {
        	f = new File(path);
        	if(!f.exists()){
        		return null;
        	}
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            fileInputStream = new FileInputStream(f);
			BitmapFactory.decodeStream(fileInputStream,null,o);

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
            while(true){
                if(width_tmp / 2 < desWidth || height_tmp / 2 < desHeight)
                    break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            result = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        	logE(TAG,"error:"+e.getStackTrace());
        }finally{
        	if(f != null){
        		f = null;
        	}
        	if(fileInputStream != null){
        		try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				fileInputStream = null;
        	}
        }
        return result;
    }
	
	/**
	 * get image real path from uri
	 * @param contentUri
	 * @param activityRef
	 * @return
	 */
    public static String getRealPathFromURI(Uri contentUri,WeakReference<Activity> activityRef) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = activityRef.get().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
