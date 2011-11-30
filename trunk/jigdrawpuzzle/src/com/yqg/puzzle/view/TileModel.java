package com.yqg.puzzle.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
/**
 * <p>
 * sigle image tile infomation.
 * </p>
 * @author Administrator
 *
 */
public class TileModel extends View{
	public TileModel(Context context) {
		super(context);
	}
	private int mXIndex = -1,mYIndex = -1;
	private int mBitmapIndex = -1;
	private Bitmap mBitmap = null;
	private Rect mAreaRect = new Rect(0,0,0,0);
	private Rect mOldArea = new Rect();
	
	public Rect getmOldArea() {
		return mOldArea;
	}
	
	public void setmOldArea(Rect mOldArea) {
		this.mOldArea = mOldArea;
	}
	
	public Bitmap getmBitmap() {
		return mBitmap;
	}
	public void setmBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}
	
	public int getmXIndex() {
		return mXIndex;
	}
	public void setmXIndex(int mXIndex) {
		this.mXIndex = mXIndex;
	}
	public int getmYIndex() {
		return mYIndex;
	}
	public void setmYIndex(int mYIndex) {
		this.mYIndex = mYIndex;
	}
	public int getmBitmapIndex() {
		return mBitmapIndex;
	}
	public void setmBitmapIndex(int mBitmapIndex) {
		this.mBitmapIndex = mBitmapIndex;
	}
	public Rect getmAreaRect() {
		return mAreaRect;
	}
	public void setmAreaRect(Rect mAreaRect) {
		this.mAreaRect = mAreaRect;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(
				mBitmap, null,
				getmAreaRect(), new Paint());
	}
}
