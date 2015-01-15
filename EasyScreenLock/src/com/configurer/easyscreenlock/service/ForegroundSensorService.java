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

package com.configurer.easyscreenlock.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.configurer.easyscreenlock.MainActivity;
import com.configurer.easyscreenlock.R;
import com.configurer.easyscreenlock.listener.ProximitySensorEventListener;
import com.configurer.easyscreenlock.setting.AppPreference;
import com.configurer.easyscreenlock.utils.AppUtils;


/**
 * <p> 
 * This service run in the "foreground". This supported Android 2.0 APIs and Up.
 * </p> 
 * referenced from {@link link http://www.vogella.com/code/ApiDemos/src/com/example/android/apis/app/ForegroundService.html}
 */
public class ForegroundSensorService extends Service {
    
	public static final String TAG = "EasyScreenLock";
	
    private static final Class<?>[] mSetForegroundSignature = new Class[] { 
    	boolean.class };
    private static final Class<?>[] mStartForegroundSignature = new Class[] {
        int.class, Notification.class};
    private static final Class<?>[] mStopForegroundSignature = new Class[] {
        boolean.class};
    
    private NotificationManager mNM;
    private Method mSetForeground;
    private Method mStartForeground;
    private Method mStopForeground;
    private Object[] mSetForegroundArgs = new Object[1];
    private Object[] mStartForegroundArgs = new Object[2];
    private Object[] mStopForegroundArgs = new Object[1];
    
    private SensorManager mSensorManager;
    private Sensor sensor;
    private SensorEventListener listener;
    
    void invokeMethod(Method method, Object[] args) {
        try {
            method.invoke(this, args);
        } catch (InvocationTargetException e) {
            // Should not happen.
            Log.w(TAG, "Unable to invoke method", e);
        } catch (IllegalAccessException e) {
            // Should not happen.
            Log.w(TAG, "Unable to invoke method", e);
        }
    }
    
    /**
     * This is a wrapper around the new startForeground method, using the older
     * APIs if it is not available.
     */
    void startForegroundCompat(int id, Notification notification) {
    	registerListener();
        
        // If we have the new startForeground API, then use it.
        if (mStartForeground != null) {
            mStartForegroundArgs[0] = Integer.valueOf(id);
            mStartForegroundArgs[1] = notification;
            invokeMethod(mStartForeground, mStartForegroundArgs);
            return;
        }
        
        // Fall back on the old API.
        mSetForegroundArgs[0] = Boolean.TRUE;
        invokeMethod(mSetForeground, mSetForegroundArgs);
        mNM.notify(id, notification);
       
    }
    
    protected void registerListener() {
	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    
	    if (isSensorAvailable()) {
	    	sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		    
		    /**
		     * Showing available sensor on Device
		     * */
		    showAvailableSensors();
		    
		    listener = new ProximitySensorEventListener(this);
		    
		    boolean isRegistered = mSensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
		    
		    Log.i(TAG, "Registering listener : " + isRegistered);
	    } else {
	    	Log.w(TAG, "Proximity Sensor not available, sorry :( ");
	    }
	}
    
    public void showAvailableSensors() {
    	try {
    		
    		List<Sensor> list = mSensorManager.getSensorList(Sensor.TYPE_ALL);
    	    if (list != null) {
    	    	Log.i(TAG, list.size() + " Available Sensors");
    	    	
    	    	for (Sensor s : list) {
    	    		Log.i(TAG, "Name : " + s.getName() + ", Type : " + s.getType());
    	    	}
    	    	System.out.println("-done-");
    	    } else {
    	    	Log.w(TAG, "You have no sensors");
    	    }
    	    
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
    }
    
    public boolean isSensorAvailable() {
    	boolean available = false;
    	try {
			
    		List<Sensor> list = mSensorManager.getSensorList(Sensor.TYPE_PROXIMITY);
    		if (list != null) 
    			available = !list.isEmpty();
    		
		} catch (Exception e) {
			available = false;
			Log.e(TAG, e.getMessage());
		}
    	
    	return available;
    }
    
    public void unRegisterListener() {
		mSensorManager.unregisterListener(listener);
		
		/* clearing references in order to avoid duplicate initialise when registering */
		listener = null;			
		sensor = null;
		mSensorManager = null;
	}
    
    /**
     * This is a wrapper around the new stopForeground method, using the older
     * APIs if it is not available.
     */
    void stopForegroundCompat(int id) {
    	unRegisterListener();
        
        // If we have the new stopForeground API, then use it.
        if (mStopForeground != null) {
            mStopForegroundArgs[0] = Boolean.TRUE;
            invokeMethod(mStopForeground, mStopForegroundArgs);
            return;
        }
        
        // Fall back on the old API.  Note to cancel BEFORE changing the
        // foreground state, since we could be killed at that point.
        mNM.cancel(id);
        mSetForegroundArgs[0] = Boolean.FALSE;
        invokeMethod(mSetForeground, mSetForegroundArgs);
        
    }
    
    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        try {
            mStartForeground = getClass().getMethod("startForeground",
                    mStartForegroundSignature);
            mStopForeground = getClass().getMethod("stopForeground",
                    mStopForegroundSignature);
            
            handleCommand();
            return;
        } catch (NoSuchMethodException e) {
            // Running on an older platform.
            mStartForeground = mStopForeground = null;
        }
        try {
            mSetForeground = getClass().getMethod("setForeground",
                    mSetForegroundSignature);
            handleCommand();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    "OS doesn't have Service.startForeground OR Service.setForeground!");
        }
    }

    @Override
    public void onDestroy() {
        // Make sure our notification is gone.
        stopForegroundCompat(AppUtils.notification_id);
    }

    // This is the old onStart method that will be called on the pre-2.0
    // platform.  On 2.0 or later we override onStartCommand() so this
    // method will not be called.
//    @Override
//    public void onStart(Intent intent, int startId) {
////        handleCommand(intent);
//    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
////        handleCommand(intent);
//        // We want this service to continue running until it is explicitly
//        // stopped, so return sticky.
//        return START_STICKY;
//    }

    NotificationCompat.Builder mBuilder;
    Bitmap largeImage = null;
    
    public NotificationCompat.Builder getBuilder() {
    	if (mBuilder == null) {
    		return new NotificationCompat.Builder(this);
    	}
    	return mBuilder;
    }
    
    void handleCommand() {
		mBuilder = getBuilder();
		
		Bitmap largeImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
		
		mBuilder.setSmallIcon(R.drawable.ic_launcher)
				.setLargeIcon(largeImage)
		        .setContentTitle(getString(R.string.app_name))
		        .setContentText(getText(R.string.service_running))
				.setWhen(getPreviousTimeStamp());

		Intent resultIntent = new Intent(this, MainActivity.class);
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addNextIntent(resultIntent);		
		
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification late
		notificationManager.notify(AppUtils.notification_id, mBuilder.build());
		
        startForegroundCompat(AppUtils.notification_id, mBuilder.build());
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
	public static final String pervious_time_stamp = "pervious_time_stamp";
	public long getPreviousTimeStamp() {
		if (AppPreference.getLong(pervious_time_stamp) <= 0) {
			AppPreference.putLong(pervious_time_stamp, System.currentTimeMillis());
			return AppPreference.getLong(pervious_time_stamp);
		}
		return AppPreference.getLong(pervious_time_stamp);
	}
	

	public class ScreenOffReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			
		}
		
	}

}