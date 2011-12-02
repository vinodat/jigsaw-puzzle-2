package com.yqg.puzzle.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 游戏时间计时器
 * @author yqg1817
 *
 */
public class PlayTimer {
	private static Timer timer;
	private TimerCallBack callback;
	private int ecliptSeconds = 0;
	private TimerTask mTask = null;
	
	public void startTimer(){
		if(timer == null){
			timer = new Timer();
		}
		if(mTask != null){
			mTask.cancel();
		}
		
		mTask = new TimerTask() {
			@Override
			public void run() {
				ecliptSeconds++;
				if(callback != null){
					callback.updateTime(""+ecliptSeconds);
				}
			}
		};
		
		
		timer.purge();
		timer.schedule(mTask, 0, 1000);
	}
	
	public void pauseTimer(){
		if(timer != null){
			timer.cancel();	
			timer = null;
		}
	}
	
	public void stopTimer(){
		if(timer != null){
			timer.cancel();	
			timer = null;
		}
		ecliptSeconds = 0;
	}
	
	/**
	 * 时钟回调函数
	 * @author yqg1817
	 *
	 */
	public interface TimerCallBack{
		boolean updateTime(String time);
	}
	
	
	public TimerCallBack getCallback() {
		return callback;
	}


	public void setCallback(TimerCallBack callback) {
		this.callback = callback;
	}
}
