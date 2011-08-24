package com.yqg.puzzle.view;

import android.graphics.Bitmap;
/**
 * <p>
 * represent the Tile.which is the basic unit of the game.
 * </p>
 * @author Administrator
 *
 */
public class TileModel {

	private int mXPixel = 0;
	private int mYPixel = 0;
	private Bitmap mTileImage = null;
	private int mWidth = 0;
	private int mHeight = 0;
	public int getmWidth() {
		return mWidth;
	}
	public void setmWidth(int mWidth) {
		this.mWidth = mWidth;
	}
	public int getmHeight() {
		return mHeight;
	}
	public void setmHeight(int mHeight) {
		this.mHeight = mHeight;
	}
	public int getmXPixel() {
		return mXPixel;
	}
	public void setmXPixel(int mXPixel) {
		this.mXPixel = mXPixel;
	}
	public int getmYPixel() {
		return mYPixel;
	}
	public void setmYPixel(int mYPixel) {
		this.mYPixel = mYPixel;
	}
	public Bitmap getmTileImage() {
		return mTileImage;
	}
	public void setmTileImage(Bitmap mTileImage) {
		this.mTileImage = mTileImage;
	}
}
