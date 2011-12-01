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
	
	public void startTimer(){
		if(timer == null)
			timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				ecliptSeconds++;
				if(callback != null){
					callback.updateTime(""+ecliptSeconds);
				}
			}
		};
		timer.schedule(task, 0, 1000);
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
