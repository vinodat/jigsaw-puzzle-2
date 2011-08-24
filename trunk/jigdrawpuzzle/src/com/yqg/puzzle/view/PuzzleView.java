package com.yqg.puzzle.view;

import android.content.Context;
import android.graphics.Bitmap;

public class PuzzleView extends TileView {

	private static final int HARD_MODE_EASY = 0;
	private static final int HARD_MODE_NORMAL = 1;
	private static final int HARD_MODE_HARD = 2;
	private static final int HARD_MODE_EXTREM_HARD = 3;

	
	public int defaultScreenWidth = 0;
	public int defaultScreenHeight = 0;
	private int mHardMod = 0;
	
	private Bitmap mPuzzleImage = null;
	
	public Bitmap getmPuzzleImage() {
		return mPuzzleImage;
	}

	public void setmPuzzleImage(Bitmap mPuzzleImage) {
		this.mPuzzleImage = mPuzzleImage;
	}

	public PuzzleView(Context context) {
		super(context);
	}
	
	private void initPuzzleBitmap(){
		if(mPuzzleImage == null){
			throw new IllegalStateException("IllegalStateException: not load the puzzle bitmap");
		}
		switch(mHardMod){
		case HARD_MODE_EASY:
			this.mXTileCount = 3;
			this.mYTileCount = 3;
			
			this.mTileArray = new int[3][3];
			
			int width = mPuzzleImage.getWidth();
	        int height = mPuzzleImage.getHeight();
	        int stepwidth = width/3;
	        int stepheight = height/3;
	        
	        int tileWidth = (this.defaultScreenHeight - 4)/3;
	        int tileHeight = (this.defaultScreenWidth - 4)/3;
	        
	        int count = 1;
			for(int i=0;i < mXTileCount ; i++){
				for(int j = 0;j < mYTileCount; j++){
					TileModel model = new TileModel();
					model.setmHeight(tileWidth);
					model.setmWidth(tileWidth);
					model.setmXPixel(j*stepwidth + (j + 1));
					Bitmap bp = Bitmap.createBitmap(mPuzzleImage, j*stepwidth, i*stepheight, stepwidth, stepheight);
				}
			}
			
			break;
		case HARD_MODE_NORMAL:
			break;
		case HARD_MODE_HARD:
			break;
		case HARD_MODE_EXTREM_HARD:
			break;
		default :
			break;
		}
		
	}

}
