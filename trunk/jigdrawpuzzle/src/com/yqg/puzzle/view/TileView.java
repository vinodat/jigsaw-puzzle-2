package com.yqg.puzzle.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * <p>
 * 
 * </p>
 * 
 * @author Administrator
 * 
 */
public class TileView extends RelativeLayout {
	private static final int SPLIT_LINE_WIDTH = 2;
	private int mLevel = -1;
	private int mScreenWidth = 0;
	private int mScreenHeight = 0;

	protected int mXTileCount = 0;
	protected int mYTileCount = 0;
	private int mTileWidth = 0;
	private int mTileHeight = 0;
	protected TileModel[][] mTileArray = null;
	private Bitmap[] mBitmapArray = null;
	private TileModel mEmptyTile = null;

	private Paint mPaint = new Paint();
	private Bitmap mEmptyBitmap;

	public TileView(Context context) {
		super(context);
	}

	/**
	 * <p>
	 * init view params
	 * </p>
	 * 
	 * @param level
	 *            game complexity level
	 * @param width
	 *            screen width
	 * @param height
	 *            screen height
	 */
	private void init(int level, int width, int height) {
		switch (level) {
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

		mBitmapArray = new Bitmap[mXTileCount * mYTileCount];
		mTileArray = new TileModel[mYTileCount][mXTileCount];

	}

	public boolean init(int level, int width, int height, Bitmap srcBitmap) {
		init(level, width, height);
		//init tile array.
		if (srcBitmap == null)
			return false;
		int tWidth = srcBitmap.getWidth();
		int tHeight = srcBitmap.getHeight();
		if (tWidth < mScreenWidth || tHeight < mScreenHeight) {
			return false;
		}
		int splitWidth = tWidth / mXTileCount;
		int splitHeight = tHeight / mYTileCount;
		
		for (int i = 0; i < mYTileCount; i++) {
			for (int j = 0; j < mXTileCount; j++) {
				int childBmpIndex = i * mYTileCount + j;
				if (childBmpIndex > mXTileCount * mYTileCount) {
					return false;
				} 
				
				Rect rect = new Rect();
				int left = SPLIT_LINE_WIDTH * (j + 1) + j * mTileWidth;
				int top = SPLIT_LINE_WIDTH * (i + 1) + i * mTileHeight;
				rect.set(left, top, left + mTileWidth, top + mTileHeight);
				if (childBmpIndex == 0) {
					EmptyTile tileModel = new EmptyTile(getContext());
					tileModel.setmXIndex(j);
					tileModel.setmYIndex(i);
					
					tileModel.setmBitmapIndex(childBmpIndex);
					tileModel.setmAreaRect(rect);
					mTileArray[i][j] = tileModel;
					//init color with white
					int[] colors = new int[splitWidth*splitHeight];
					for(int k = 0;k< splitWidth*splitHeight ;k++){
						colors[k] = Color.WHITE;
					}
					mEmptyBitmap = Bitmap.createBitmap(colors,splitWidth, splitHeight, Bitmap.Config.RGB_565);
					tileModel.setmEmptyBitmap(mEmptyBitmap);
					Bitmap splitBitmap = Bitmap.createBitmap(
							srcBitmap, splitWidth * j, splitHeight * i,
							splitWidth, splitHeight);
					tileModel.setmBitmap(splitBitmap);
					this.addView(tileModel);
				} else {
					TileModel tileModel = new TileModel(getContext());
					tileModel.setmXIndex(j);
					tileModel.setmYIndex(i);
					
					tileModel.setmBitmapIndex(childBmpIndex);
					tileModel.setmAreaRect(rect);
					mTileArray[i][j] = tileModel;
					tileModel.setmBitmap(Bitmap.createBitmap(
							srcBitmap, splitWidth * j, splitHeight * i,
							splitWidth, splitHeight));
					this.addView(tileModel);
				}
			}
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint p = new Paint();
		p.setStrokeWidth(0);

		// draw horizond line.
		for (int i = 0; i <= mYTileCount; i++) {
			canvas.drawLine(0, i * mTileHeight + (i) * SPLIT_LINE_WIDTH,
					mScreenWidth, i * mTileHeight + (i) * SPLIT_LINE_WIDTH, p);
		}
		// draw vertical line.
		for (int i = 0; i <= mXTileCount; i++) {
			canvas.drawLine(i * mTileWidth + (i) * SPLIT_LINE_WIDTH, 0, i
					* mTileWidth + (i) * SPLIT_LINE_WIDTH, mScreenHeight, p);
		}

		canvas.drawColor(Color.WHITE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			moveTile(x, y);
			break;

		default:
			break;
		}
		
		postInvalidate();
		return true;
	}

	private void changeInfo(TileModel x,TileModel y){
		if(null == x || null == y)
			return;
		Rect r = x.getmAreaRect();
		int xindex = x.getmXIndex();
		int yindex = x.getmYIndex();
		
		x.setmYIndex(y.getmYIndex());
		x.setmXIndex(y.getmXIndex());
		x.setmAreaRect(y.getmAreaRect());
		y.setmYIndex(yindex);
		y.setmXIndex(xindex);
		y.setmAreaRect(r);
	}
	
	private TileModel getTile(int x,int y){
		for(int i = 0;i<mYTileCount;i++){
			for(int j= 0;j < mXTileCount;j++){
				TileModel tileModel = mTileArray[i][j];
				if(tileModel.getmXIndex() == x && tileModel.getmYIndex() == y)
					return tileModel;
			}
		}
		return null;
	}
	
	private TileModel getClickedTile(float x, float y){
		for (int i = 0; i < mYTileCount; i++) {
			for (int j = 0; j < mXTileCount; j++) {
				TileModel tileModel = mTileArray[i][j];
				if (tileModel != null
						&& tileModel.getmAreaRect().contains((int) x, (int) y)) {
					return tileModel;
				}
			}
		}
		return null;
	}
	
	private void moveTile(float x, float y) {
		TileModel tileModel = getClickedTile(x,y);
		if (mEmptyTile != null && tileModel != null) {
			if (mEmptyTile.getmXIndex() == tileModel.getmXIndex()) {
				int yIndex = mEmptyTile.getmYIndex();
				int xIndex = mEmptyTile.getmXIndex();
				if (yIndex > tileModel.getmYIndex()) {
					for (int k = yIndex-1; k >= tileModel.getmYIndex(); k--) {
						TileModel tileModel2 = getTile(xIndex,k);
						changeInfo(tileModel2,mEmptyTile);
					}
				} else if (yIndex < tileModel.getmYIndex()) {
					for (int k = yIndex+1; k <= tileModel.getmYIndex(); k++) {
						TileModel tileModel2 = getTile(xIndex,k);
						changeInfo(tileModel2,mEmptyTile);
					}
				}

			} else if (mEmptyTile.getmYIndex() == tileModel.getmYIndex()) {
				int xIndex = mEmptyTile.getmXIndex();
				int yIndex = mEmptyTile.getmYIndex();
				if(xIndex > tileModel.getmXIndex()){
					for(int k = xIndex-1;k >= tileModel.getmXIndex();k--){
						TileModel tTileModel = getTile(k,yIndex);
						changeInfo(tTileModel,mEmptyTile);
					}
				}else if(xIndex < tileModel.getmXIndex()){
					for(int k = xIndex+1;k <= tileModel.getmXIndex();k++){
						TileModel tTileModel = getTile(k,yIndex);
						changeInfo(tTileModel,mEmptyTile);
					}
				}
			}
		}
	}
}
