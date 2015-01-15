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

package com.configurer.easyscreenlock.component;

import java.util.Calendar;

import com.configurer.easyscreenlock.service.ForegroundSensorService;
import com.configurer.easyscreenlock.utils.AppUtils;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceManager {
	
	public static final String TAG = "EasyScreenLock";
	
	public static void startForegroundService(Context context) {
		Intent intent = new Intent(context, ForegroundSensorService.class);
    	PendingIntent appIntent = PendingIntent.getService(context, 0, intent, 0);
    	
        Calendar calendar = Calendar.getInstance();
        long triggerAtTime = calendar.getTimeInMillis();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, triggerAtTime, appIntent);
        
        Log.i(TAG, "Proximity foreground sensor service started");
	}
	
	public static void stopForegroundService(Context context) {
		Intent serviceIntent = new Intent(context, ForegroundSensorService.class);
		context.stopService(serviceIntent);
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    	/* remove notification by ID */
		notificationManager.cancel(AppUtils.notification_id);
		
		Log.i(TAG, "Proximity foreground sensor service stopped");
	}
}
