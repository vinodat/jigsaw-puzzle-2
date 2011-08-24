package com.yqg.puzzle.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.view.View;
/**
 * <p>
 * 
 * </p>
 * @author Administrator
 *
 */
public class TileView extends View {
	private static final int SPLIT_LINE_WIDTH = 2;
	private int mLevel = -1;
	private int mScreenWidth = 0;
	private int mScreenHeight = 0;
	
	protected int mXTileCount = 0;
	protected int mYTileCount = 0;
	private int mTileWidth = 0;
	private int mTileHeight = 0;
	protected int[][] mTileArray = null;
	private Bitmap[] mBitmapArray = null;
	
	private Paint mPaint = new Paint();
	
	public TileView(Context context) {	
		super(context);
	}
	/**
	 * <p>init view params</p>
	 * @param level   game complexity level
	 * @param width   screen width
	 * @param height  screen height
	 */
	private void init(int level,int width,int height){
		switch(level){
		case 1:
			mLevel = 1;
			mXTileCount = 3;
			mYTileCount = 3;
			
			mScreenWidth = width;
			mScreenHeight = height;
			
			mTileWidth = (mScreenWidth - 4 * SPLIT_LINE_WIDTH) / 3;
			mTileHeight = (mScreenHeight - 4 * SPLIT_LINE_WIDTH) / 3;
			break;
		case 2:
			mLevel = 2;
			mXTileCount = 4;
			mYTileCount = 4;
			
			mScreenWidth = width;
			mScreenHeight = height;
			
			mTileWidth = (mScreenWidth - 5 * SPLIT_LINE_WIDTH) / 4;
			mTileHeight = (mScreenHeight - 5 * SPLIT_LINE_WIDTH) / 4;
			break;
		default:
			break;
		}
		
		mBitmapArray = new Bitmap[mXTileCount*mYTileCount];
		mTileArray = new int[mYTileCount][mXTileCount];
	}
	
	public boolean init(int level,int width,int height,Bitmap srcBitmap){
		init(level,width,height);
		splitBitmap(srcBitmap);
		return true;
	}

	private boolean splitBitmap(Bitmap bitmap) {
		if(bitmap == null)
			return false;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if(width < mScreenWidth || height < mScreenHeight){
			return false;
		}
		int splitWidth = width / mXTileCount;
		int splitHeight = height / mYTileCount;
		
		for(int i = 0;i < mYTileCount ; i++){
			for(int j = 0; j < mXTileCount ; j++){
				int childBmpIndex = i * mYTileCount + j;
				mTileArray[i][j] = childBmpIndex;
				if(childBmpIndex > mXTileCount * mYTileCount){
					return false;
				}else{
					mBitmapArray[childBmpIndex] = Bitmap.createBitmap(bitmap, splitWidth * j, splitHeight * i, splitWidth, splitHeight);
				}
			}
		}
		
		
		return true;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint p = new Paint();
		//p.setColor(Color.RED);
		p.setStrokeWidth(0);
		
		//draw horizond line.
		for(int i =0 ;i <= mYTileCount ; i++){
			canvas.drawLine(0, i * mTileHeight + (i)*SPLIT_LINE_WIDTH, 
					mScreenWidth, i * mTileHeight+ (i)*SPLIT_LINE_WIDTH, p);
		}
		//draw vertical line.
		for(int i = 0;i <= mXTileCount; i++){
			canvas.drawLine(i * mTileWidth + (i)*SPLIT_LINE_WIDTH, 0, 
					i * mTileWidth+(i)*SPLIT_LINE_WIDTH, mScreenHeight, p);
		}
		
		canvas.drawColor(Color.WHITE);
		for(int i=0;i< mYTileCount; i++){
			for(int j=0;j < mXTileCount; j++){
				Rect rect = new Rect();
				int left = SPLIT_LINE_WIDTH*(j+1) + j * mTileWidth;
				int top = SPLIT_LINE_WIDTH * (i+1) + i * mTileHeight;
				rect.set(left, top, left + mTileWidth, top + mTileHeight);
				canvas.drawBitmap(mBitmapArray[mTileArray[i][j]], null, rect, mPaint);
			}
		}
	}
}
