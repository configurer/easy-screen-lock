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

package com.configurer.easyscreenlock.receiver;

import com.configurer.easyscreenlock.component.ServiceManager;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RebootEventReceiver extends BroadcastReceiver{

	public static final String TAG = "EasyScreenLock";
	
	protected DevicePolicyManager deviceManger;
	protected ActivityManager activityManager;
	protected ComponentName compName;
	
	@Override
	public void onReceive(Context context, Intent intent) {
	
		try {
			deviceManger = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			compName = new ComponentName(context, DeviceAdministratorReceiver.class);
			
			if (deviceManger.isAdminActive(compName)) 
				ServiceManager.startForegroundService(context);
			else 
				ServiceManager.stopForegroundService(context);
			
		} catch (Exception e) {
			deviceManger = null;
			activityManager = null;
			compName = null;
			Log.e(TAG, e.getMessage());
		}
		
	}
	
}
