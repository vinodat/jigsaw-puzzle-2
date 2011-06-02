package com.yqg.puzzle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class JigdrawPuzzleMain extends Activity {
	
	private Bitmap[][] bmps = new Bitmap[3][3];
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setContentView(R.layout.main);
        
        Display display = getWindowManager().getDefaultDisplay(); 
        int dwidth = display.getWidth();
        int dheight = display.getHeight();
        
        Drawable dbmp = getResources().getDrawable(R.drawable.test);
        Bitmap bitmap = ((BitmapDrawable)dbmp).getBitmap();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        
        int stepwidth = width/3;
        int stepheight = height/3;
        LinearLayout layout = (LinearLayout) findViewById(R.id.imageslayout);
        for(int i = 0;i < 3;i ++){
        	LinearLayout llayout = new LinearLayout(this);
        	llayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, dheight/3));
        	llayout.setOrientation(LinearLayout.HORIZONTAL);
        	for(int j = 0;j< 3;j++){
        		Bitmap bp = Bitmap.createBitmap(bitmap, j*stepwidth, i*stepheight, stepwidth, stepheight);
        		ImageView iv = new ImageView(this);
        		iv.setLayoutParams(new LayoutParams(dwidth/3, dheight/3));
        		iv.setScaleType(ScaleType.CENTER_INSIDE);
        		iv.setImageBitmap(bp);
        		llayout.addView(iv);
        	}
        	layout.addView(llayout);
        	llayout = null;
        }
    }
    
    
}