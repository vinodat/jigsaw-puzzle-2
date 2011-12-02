package com.yqg.puzzle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.JetPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	
	//private field
	private TextView mTxtView = null;
	private PlayTimer timer = new PlayTimer();
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
        timer.setCallback(timerCallback);
        
        prepareJetPlayer();
        
        Display display = getWindowManager().getDefaultDisplay(); 
        int dwidth = display.getWidth();
        int dheight = display.getHeight();
        
        //320*240 [80]
        int bannerHeight = dheight - dwidth;
        if(bannerHeight > 80){
        	bannerHeight = 80;
        }
        
        Drawable dbmp = getResources().getDrawable(R.drawable.test);
        Bitmap bitmap = ((BitmapDrawable)dbmp).getBitmap();
        
        PuzzleView tv = new PuzzleView(this);
        tv.setPuzzleCallBack(puzzleCallback);
        int level = 1;
        if(!tv.init(level, dwidth, dheight - bannerHeight, bitmap)){//init fail.
        	//TODO process;go to choose panel.
        }else{//init suc,show
        	setContentView(R.layout.puzzleview);
            LinearLayout puzzleLayout = (LinearLayout) findViewById(R.id.puzzle_view);
            mTxtView = (TextView) puzzleLayout.findViewById(R.id.txt_view_timer);
            puzzleLayout.addView(tv);
        }
        
        initView(level);
        
        //random disrupt.
        tv.randomDisrupt();
    }
    
	/**
	 * init the view .
	 */
	private void initView(int level){
		TextView tv = (TextView) findViewById(R.id.textView_level);
		String strLevel = getString(R.string.str_level);
		//StringBuilder sb = new StringBuilder();
		   // Send all output to the Appendable object sb
		//Formatter formatter = new Formatter(sb);
		//formatter.format(strLevel, mLevel);
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
		UtilFuncs.logE(TAG, ">>>>>>>>>>>>> "+sb );
		tv.setText(sb.toString());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
    @Override
    protected void onResume() {
    	super.onResume();
    	timer.startTimer();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	stopJetPlayer();
    	timer.pauseTimer();
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
			timer.stopTimer();
			Toast.makeText(JigdrawPuzzleMain.this, "You win!", Toast.LENGTH_SHORT).show();
		}
	};
	
	private void prepareJetPlayer(){
		new JetPlayerPrepareTask().execute(null);
        //mJet.play();
	}
	
	private void stopJetPlayer(){
		mJet.pause();
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
			mJet.play();
		}
		
	}
}