package com.yqg.puzzle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.yqg.puzzle.view.TileView;

public class JigdrawPuzzleMain extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        
        Display display = getWindowManager().getDefaultDisplay(); 
        int dwidth = display.getWidth();
        int dheight = display.getHeight() - 80;
        
        Drawable dbmp = getResources().getDrawable(R.drawable.test);
        Bitmap bitmap = ((BitmapDrawable)dbmp).getBitmap();
        
        TileView tv = new TileView(this);
        if(!tv.init(1, dwidth, dheight, bitmap)){//if init fail.go to choose panel.
        	//TODO process
        }else{//init suc,show
        	setContentView(R.layout.puzzleview);
            LinearLayout puzzleLayout = (LinearLayout) findViewById(R.id.puzzle_view);
            puzzleLayout.addView(tv);
        }
    }
}