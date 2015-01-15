/*
 * Copyright (c) 2014-2015 Soe Yan Naing
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.configurer.easyscreenlock.listener;

import java.util.Calendar;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.configurer.easyscreenlock.component.DeviceManager;

public class ProximitySensorEventListener implements SensorEventListener{

	public static final String TAG = "EasyScreenLock";
	
	private DeviceManager deviceManager;
	private CountDownTimer timer, timerHelper;
	private float distanceUpdate;
	private Vibrator v;
	private long timerStartTime;
	private long timeOut;
	private long triggeredTime;
	
	protected PowerManager pm;
	protected WakeLock wakeLock;
	
	/** 
	 * Initialise below required components :
	 * <li> <b>{@link DeviceManager DeviceManager}</b></li> 
	 * <li> <b>{@link android.os.Vibrator Vibrator}</b></li>
	 * */
	public ProximitySensorEventListener(Context context) {				
		deviceManager = new DeviceManager(context);		
		v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		
		/**
		 * Initialise PowerManager and WakeLock(Wake lock that ensures that the CPU is running. The screen might not be on.).
		 * */
		pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);		
		wakeLock = pm.newWakeLock((PowerManager.PARTIAL_WAKE_LOCK), TAG);
	}
	
	/**
	 * Proximity Sensor's event listener.
	 * <p> 
	 * <b>values[0]:</b> Proximity sensor distance measured in centimeters <br> 
	 * <b>Note: </b> Some proximity sensors only support a binary near or far measurement. 
	 * In this case, the sensor should report its maximum range value in the far state and a lesser value in the near state.
	 * </p> 
	 * */
	@Override
	public void onSensorChanged(SensorEvent event) {
		
	   /** 
		* values[0]: Proximity sensor distance measured in centimeters. 
		*/
		final float distance = event.values[0];
		
		this.distanceUpdate = distance;
		
		if (distance > 0) { /** If Far */
			triggeredTime = Calendar.getInstance().getTimeInMillis();
		} else { /** If Near */
			/**
			 * Start wake lock before timer start working. Because timer are not work when device is deep sleep
			 * in order to prevent batter drain.
			 * */
			acquireWakeLock();
			
			startCountDownHelper();
			startCountDown();
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
		Log.i(TAG, "Proximity sensor accuracy changed : " + accuracy);
		// do nothing.
	}
	
	protected void doScreenLockAction() {
		
		deviceManager.doScreenAction();		
		clearTimers();
		
	}	
	
	protected void acquireWakeLock() {
		try {
			if (!wakeLock.isHeld())
				wakeLock.acquire();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	protected void clearTimers() {
		clearTimerHelper();
		clearTimer();		
		
		timerStartTime = 0;
		triggeredTime = 0;
		timeOut = 0;
	}

	/**
	 * Start {@link android.os.CountDownTimer CountDownTimer} that listen user's event trigger in order to make <b>Screen ON </b>(<i>Device WakeUp and show LockScrren</i>) OR 
	 * <b>Screen OFF</b> <i>(Screen Lock and Sleep as soon as possible)</i>
	 * */
	protected void startCountDown() {
		clearTimer();

		timer = new CountDownTimer(2000,2000) {

			@Override
			public void onTick(long millisUntilFinished) { /* do nothing */	}

			@Override
			public void onFinish() {
				
				timeOut = Calendar.getInstance().getTimeInMillis();
				
				if (distanceUpdate > 0 && !isTooSoon() && isInTime())
					doScreenLockAction();
				
				timer = null;				
				releaseWakeLock();
			}
		};
		
		timer.start();		
		timerStartTime = Calendar.getInstance().getTimeInMillis();
	}
	
	private void clearTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	private void releaseWakeLock() {
		if (wakeLock.isHeld())
			wakeLock.release();
	}
	
	protected boolean isTooSoon() {
		boolean isTooSoon = false;
		
		Calendar cTimerStartTime = Calendar.getInstance();
		Calendar cTriggeredTime = Calendar.getInstance();
		Calendar cEalierTimeOut = Calendar.getInstance();
		
		cTimerStartTime.setTimeInMillis(timerStartTime);
		cTriggeredTime.setTimeInMillis(triggeredTime);
		cEalierTimeOut.add(Calendar.SECOND, -1);
		
		/*System.out.println();
		System.out.println("soon st " + cTimerStartTime.getTime());
		System.out.println("soon tt " + cTriggeredTime.getTime());
		System.out.println("soon et " + cEalierTimeOut.getTime());
		System.out.println();*/
		
		if (cTriggeredTime.after(cTimerStartTime) && cTriggeredTime.before(cEalierTimeOut))
			isTooSoon = true;
		
		Log.i(TAG, "isTooSoon ? " + isTooSoon);
		return isTooSoon;
	}
	
	protected boolean isInTime() {
		
		boolean isInTime = false;
		
		Calendar cTimerStartTime = Calendar.getInstance();
		Calendar cTriggeredTime = Calendar.getInstance();
		Calendar cTimeOut = Calendar.getInstance();
		
		cTimerStartTime.setTimeInMillis(timerStartTime);
		cTriggeredTime.setTimeInMillis(triggeredTime);
		cTimeOut.setTimeInMillis(timeOut);
		
		/*System.out.println();
		System.out.println("startTime = " + cTimerStartTime.getTime());
		System.out.println("triggered = " + cTriggeredTime.getTime());
		System.out.println("endedTime = " + cTimeOut.getTime());
		System.out.println();*/

		/** If triggered time is within timeRange */
		if (cTriggeredTime.after(cTimerStartTime) && cTriggeredTime.before(cTimeOut)) {
			
			/** If triggered time is out of timeRange */
			if (cTriggeredTime.before(cTimerStartTime) || cTriggeredTime.after(cTimeOut)) 
				isInTime = false;
			else
				isInTime = true;
		}
		
		Log.i(TAG, "isInTime ? " + isInTime);
		return isInTime;
	}
	
	/**
	 * Start {@link android.os.CountDownTimer CountDownTimer} that indicate users by vibrating to make event trigger,
	 * so that user can know when to make event trigger (Screen On/Off).
	 * */
	protected void startCountDownHelper() {
		clearTimerHelper();
		
		timerHelper = new CountDownTimer(1100, 1100) {
			
			@Override
			public void onTick(long millisUntilFinished) { /*do nothing */ }
			
			@Override
			public void onFinish() {
				v.vibrate(100);
				timerHelper = null;
			}
		};
		timerHelper.start();
		
	}
	
	private void clearTimerHelper() {
		if (timerHelper != null) {
			timerHelper.cancel();
			timerHelper = null;
		}
	}
	
}