package com.yqg.puzzle.view;

import android.content.Context;
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
	private static final String TAG = "TileView";
	protected static final int SPLIT_LINE_WIDTH = 2;
	protected int mScreenWidth = 0;
	protected int mScreenHeight = 0;

	
	protected int mTileWidth = 0;
	protected int mTileHeight = 0;
	protected TileModel[][] mTileArray = null;
	
	protected PuzzleCallBack puzzleCallBack;

	public TileView(Context context) {
		super(context);
	}


	public interface PuzzleCallBack{
		void gameOverCallBack();
	}

	public PuzzleCallBack getPuzzleCallBack() {
		return puzzleCallBack;
	}

	public void setPuzzleCallBack(PuzzleCallBack puzzleCallBack) {
		this.puzzleCallBack = puzzleCallBack;
	}
}
