package com.yqg.puzzle.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class EmptyTile extends TileModel {

	private Bitmap mEmptyBitmap = null;
	public Bitmap getmEmptyBitmap() {
		return mEmptyBitmap;
	}
	public void setmEmptyBitmap(Bitmap mEmptyBitmap) {
		this.mEmptyBitmap = mEmptyBitmap;
	}
	public EmptyTile(Context context) {
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		super.onDraw(canvas);
		if(getmBitmapIndex() == 0){
			Paint tp = new Paint();
			tp.setAlpha(200);
			canvas.drawBitmap(mEmptyBitmap, null, getmAreaRect(), tp);
		}
	}

}
