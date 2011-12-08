package com.yqg.puzzle;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.JetPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yqg.puzzle.utils.Constants;
import com.yqg.puzzle.utils.PlayTimer;
import com.yqg.puzzle.utils.UtilFuncs;
import com.yqg.puzzle.utils.PlayTimer.TimerCallBack;
import com.yqg.puzzle.view.PuzzleView;
import com.yqg.puzzle.view.TileView;
import com.yqg.puzzle.view.TileView.PuzzleCallBack;

public class JigdrawPuzzleMain extends Activity {
	private static final String TAG = "JigdrawPuzzleMain";
	
	private static final String PLAY_TIME = "play_time";
	private static final int TIMER_MESSAGE = 1;
	//dialogue id define.
	private static final int DIALOGUE_EXIT_ID = 100;
	
	//private field
	private PuzzleView mGameView = null;
	private LinearLayout mPuzzleLayout = null;
	private Bitmap mOrigBitmap = null;
	private TextView mTxtView = null;
	private RelativeLayout mImageLayout = null;
	private Intent mIntent = null;
	
	//state fields
	private boolean isOrigImageShow = false;
	private boolean isNewImageGet = false;
	private int mGameViewWidth = 0;
	private int mGameViewHeight = 0;
	private int mLevel = 2;
	private boolean isMusicOn = true;
	
	private PlayTimer mTimer = new PlayTimer();
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case TIMER_MESSAGE:
				CharSequence time = msg.getData().getCharSequence(PLAY_TIME);
				String timeConsume = getString(R.string.str_time_consume);
				String strTime = String.format(timeConsume, time);
				mTxtView.setText(strTime);
				break;
			default:break;
			}
		};
	};
	
	
	private JetPlayer mJet = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        mTimer.setCallback(timerCallback);
        initStateFromPreferencesSetting();
        if(isMusicOn){
        	//prepare bg music.
            prepareJetPlayer();
        }
        
        Display display = getWindowManager().getDefaultDisplay(); 
        mGameViewWidth = display.getWidth();
        int dheight = display.getHeight();
        
        //320*240 [80]
        int bannerHeight = dheight - mGameViewWidth;
        if(bannerHeight > 80){
        	bannerHeight = 80;
        }
        mGameViewHeight = dheight - bannerHeight;
        getDefaultBitmap();
        
        initGame();
    }
    
    private void initGame(){
    	initView(mLevel);
		//random disrupt.
        mGameView.randomDisrupt();
    }

    private void initStateFromPreferencesSetting() {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());	
    	String strLevel = preferences.getString(Constants.KEY_PREFER_LEVEL, "1");
    	mLevel = Integer.parseInt(strLevel);
    	isMusicOn = preferences.getBoolean(Constants.KEY_PREFER_AUDIO, true);
	}

	/**
     * get default bitmap.
     */
	private void getDefaultBitmap() {
		//get default bitmap
        Drawable dbmp = getResources().getDrawable(R.drawable.game_pic);
        mOrigBitmap = ((BitmapDrawable)dbmp).getBitmap();
	}
    
	/**
	 * init the view .
	 */
	private void initView(int level){
		//init view
        mGameView = new PuzzleView(this);
        mGameView.setPuzzleCallBack(puzzleCallback);
        
        setContentView(R.layout.puzzleview);
        if(!mGameView.init(level, mGameViewWidth, mGameViewHeight, mOrigBitmap)){//init fail.
        	//TODO process;go to choose panel.
        	Toast.makeText(this, "The image is too small!", Toast.LENGTH_SHORT);
        }else{//init suc,show
        	mPuzzleLayout = (LinearLayout) findViewById(R.id.puzzle_view);
            mTxtView = (TextView) mPuzzleLayout.findViewById(R.id.txt_view_timer);
            mPuzzleLayout.addView(mGameView);
        }
        //init level view.
		TextView tv = (TextView) findViewById(R.id.textView_level);
		String strLevel = getString(R.string.str_level);
		String levelValue = null;
		switch(level){
		case 1:
			levelValue = getString(R.string.str_level_low);
			break;
		case 2:
			levelValue = getString(R.string.str_level_medium);
			break;
		case 3:
			levelValue = getString(R.string.str_level_high);
			break;
		default:
			levelValue = getString(R.string.str_level_low);
			break;
		}
		
		String sb = String.format(strLevel, levelValue);
		tv.setText(sb.toString());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_choose:
			chooseGameImage();
			break;
		case R.id.menu_check_image:
			showOriginImage(true);
			break;
		case R.id.menu_setting:
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), PreferSettingActivity.class);
			startActivityForResult(intent, REQUEST_CODE_SETTING);
			break;
		//case R.id.menu_setting_oudiosetting:
		//	break;
		case R.id.menu_save:
			break;
		case R.id.menu_ad:
			Intent intentAd = new Intent();
			intentAd.setClass(getApplicationContext(), AdActivity.class);
			startActivity(intentAd);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus){
			changeTimerState(true);
			playJetPlayer();
		}else{
			changeTimerState(false);
			stopJetPlayer();
		}
	}
	int REQUEST_CODE_CHOOSE_IMAGE = 110;
	int REQUEST_CODE_SETTING = 111;
	
	private void chooseGameImage(){
		
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, REQUEST_CODE_CHOOSE_IMAGE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE_CHOOSE_IMAGE){
			if(resultCode == Activity.RESULT_OK){
				isNewImageGet = true;
				mIntent = data;
			}else{
				isNewImageGet = false;
			}
		}else if(requestCode == REQUEST_CODE_SETTING){
			if(data != null){
				boolean audioModified = data.getExtras().getBoolean(Constants.KEY_PREFERENCES_AUDIO_SETTING_MODIFIED);
				if(audioModified){
					initStateFromPreferencesSetting();
					if(isMusicOn){
						prepareJetPlayer();
						playJetPlayer();
					}else{
						if(mJet != null){
							mJet.pause();	
							stopJetPlayer();
						}
					}
				}
				
				boolean levelModified = data.getExtras().getBoolean(Constants.KEY_PREFERENCES_LEVEL_SETTING_MODIFIED);
				if(levelModified){
					initStateFromPreferencesSetting();
					initGame();
				}
			}
		}
	}
	
	private void showOriginImage(boolean show){
		if(mImageLayout == null){
			LayoutInflater inflater = getLayoutInflater();
			mImageLayout = (RelativeLayout) inflater.inflate(R.layout.origin_image_viewlayout, null);
			ImageView mOrigImg = (ImageView) mImageLayout.findViewById(R.id.origin_image_view);
			mOrigImg.setImageBitmap(mOrigBitmap);
		}
		RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.puzzle_relative_view);
		if(show){
			if(!isOrigImageShow){
				isOrigImageShow = true;
				rlayout.addView(mImageLayout);
				changeTimerState(false);
			}
		}else{
			if(isOrigImageShow){
				isOrigImageShow = false;
				rlayout.removeView(mImageLayout);	
				mImageLayout = null;
				changeTimerState(true);
			}
		}
		rlayout.postInvalidate();
	}
	
	private void changeTimerState(boolean turnOn){
		if(turnOn){
			if(!isOrigImageShow){
				mTimer.startTimer();	
			}
		}else{
			mTimer.pauseTimer();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(isOrigImageShow){
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//showDialog(DIALOGUE_EXIT_ID);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOGUE_EXIT_ID:
			return new AlertDialog.Builder(JigdrawPuzzleMain.this)
					.setTitle(
							R.string.str_game_exit_check)
							.setMessage(R.string.str_game_exit_check)
							.setPositiveButton(
							R.string.str_btn_yes, new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).setNegativeButton(R.string.str_btn_no,
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dismissDialog(DIALOGUE_EXIT_ID);
								}
							}).create();
		}
		return super.onCreateDialog(id);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(isOrigImageShow){
			showOriginImage(false);
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_BACK){
			showDialog(DIALOGUE_EXIT_ID);
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.puzzle_main_menu, menu);
		return true;
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	
    @Override
    protected void onResume() {
    	super.onResume();
    	if(isNewImageGet){
    		mTimer.stopTimer();
    		Uri uri = mIntent.getData();
    		if(uri != null){
    			try {
    				String path = UtilFuncs.getRealPathFromURI(uri,new WeakReference<Activity>(this));
    				if(!TextUtils.isEmpty(path)){
    					mOrigBitmap = UtilFuncs.decodeFile(path,mGameViewWidth*2,mGameViewHeight*2);
    				}
				} catch(Exception e){
					UtilFuncs.logE(TAG,"error:"+e.getStackTrace());
					UtilFuncs.showToast(this, R.string.str_image_error);
				}
    		}
    		if(mOrigBitmap == null){
    			getDefaultBitmap();
    		}
    		initView(mLevel);
    		//random disrupt.
            mGameView.randomDisrupt();
            showOriginImage(false);
    	}
    	isNewImageGet = false;
    	changeTimerState(true);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	stopJetPlayer();
    	changeTimerState(false);
    }
    
    private TimerCallBack timerCallback = new PlayTimer.TimerCallBack() {
		
		@Override
		public boolean updateTime(String time) {
			Message msg = mHandler.obtainMessage(TIMER_MESSAGE);
			Bundle data = new Bundle();
			data.putCharSequence(PLAY_TIME, time);
			msg.setData(data);
			mHandler.sendMessage(msg);
			return false;
		}
	};
	
	private PuzzleCallBack puzzleCallback = new TileView.PuzzleCallBack() {
		
		@Override
		public void gameOverCallBack() {
			changeTimerState(false);
			Toast.makeText(JigdrawPuzzleMain.this, "You win!", Toast.LENGTH_SHORT).show();
		}
	};
	
	private void prepareJetPlayer(){
		if(mJet == null){
			new JetPlayerPrepareTask().execute(new String[]{});
		}
        //mJet.play();
	}
	
	private void stopJetPlayer(){
		if(mJet != null){
			mJet.pause();	
		}
	}
	
	private void playJetPlayer(){
		if(mJet != null){
			mJet.play();
		}
	}
	
	private class JetPlayerPrepareTask extends AsyncTask<String,Void,Void>{

		@Override
		protected Void doInBackground(String... params) {
			synchronized (this) {
				mJet = JetPlayer.getJetPlayer();
		        mJet.loadJetFile(getResources().openRawResourceFd(R.raw.level1));
		        byte segmentId = 0;
		        // JET info: end game music, loop 4 times normal pitch
		        mJet.queueJetSegment(1, 0, -1, 0, 0, segmentId);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			playJetPlayer();
		}
		
	}
}