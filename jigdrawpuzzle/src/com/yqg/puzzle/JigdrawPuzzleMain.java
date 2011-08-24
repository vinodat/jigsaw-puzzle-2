package com.yqg.puzzle;

import com.yqg.puzzle.view.TileView;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
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
       /* setContentView(R.layout.main);
        Resources res = getResources();
        TransitionDrawable transition = (TransitionDrawable) res.getDrawable(R.drawable.transtion);
        ImageView image = new ImageView(this);
        image.setImageDrawable(transition);
        transition.startTransition(1000);
        LinearLayout layout = (LinearLayout) findViewById(R.id.imageslayout);
        layout.addView(image);*/
        
        Display display = getWindowManager().getDefaultDisplay(); 
        int dwidth = display.getWidth();
        int dheight = display.getHeight();
        
        Drawable dbmp = getResources().getDrawable(R.drawable.test);
        Bitmap bitmap = ((BitmapDrawable)dbmp).getBitmap();
        
        TileView tv = new TileView(this);
        tv.init(2, dwidth, dheight, bitmap);
        
        setContentView(tv);
        
    }
    
    
}