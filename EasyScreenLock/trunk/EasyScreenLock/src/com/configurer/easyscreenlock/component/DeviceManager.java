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

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import com.configurer.easyscreenlock.R;
import com.configurer.easyscreenlock.receiver.DeviceAdministratorReceiver;
import com.configurer.easyscreenlock.utils.AppUtils;

/**
 * Handle to <b>"WakeUp Device"</b> if Device is sleep (OR) <br>
 * handle to <b>"Screen Lock & Sleep"</b> if Device is wake up.
 * */
public class DeviceManager {

	public static final String TAG = "EasyScreenLock";
	
	protected DevicePolicyManager deviceManger;
	protected ComponentName compName;
	protected Context context;
	
	protected PowerManager pm;
	
	private boolean isDeviceAdminEnable = false;
	
	public DeviceManager(Context context) {
		this.context = context;
		
		try {
			deviceManger = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			compName = new ComponentName(context, DeviceAdministratorReceiver.class);
			pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		} catch (Exception e) {
			Log.e(TAG, "[DeviceManager]" + e.getMessage());
		}		
		
		initializeDeviceAdminRef();
		/* Purpose of Initialising at constructor is to use later many time by avoiding performance issue */
	}
	
	/**************************************************** Action Methods ****************************************************/	
	
	/**
	 * Wake Up Device if Device is Sleep OR do likewise as opposite action.
	 * */
	@SuppressLint("NewApi")
	public void doScreenAction() {
		
		try {
			
			if (isDeviceAdminEnable) {
				
				if (pm.isScreenOn()) {
					Log.i(TAG, "Lock Screen");
						
					/* 
					 * [ A trick of lockNow() within (4.+ to under 4.2.2) ]
					 * Issue : https://code.google.com/p/android/issues/detail?can=2&start=0&num=100&q=&colspec=ID%20Type%20Status%20Owner%20Summary%20Stars&groupby=&sort=&id=26599
					 */
					Handler handlerUI = new Handler();
                    handlerUI.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        	deviceManger.lockNow();
                        }
                    }, 200);

                    if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 && 
                    		android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    	deviceManger.lockNow();
                    }                    
                    
				} else {
					Log.i(TAG, "WakeUp Device");
										
					AppUtils.startHelperActivity(context);
				}
				
			}
			
		} catch (Exception e) {
			Log.e(TAG, "[doScreenAction]" + e.getMessage(), e);
		}
		
	}

	public boolean isScreenOn() {
		return pm.isScreenOn();
	}
	
	/****************************************************** Initialising ******************************************************/
	
	/*** Initialise DEVICE ADMIN reference that indicates whether application is enable for Device Administrator OR not. */
	protected void initializeDeviceAdminRef() {		
		try {
			validateDeviceAdministrator();
			isDeviceAdminEnable = true;
		} catch (Exception e) {
			isDeviceAdminEnable = false;
		}
	}
	
	/****************************************************** Validating ******************************************************/
	
	protected void validateDeviceAdministrator() throws AppPermissionException{
		if (!deviceManger.isAdminActive(compName))
			throw new AppPermissionException(this.context.getString(R.string.error_permission));
	}
}	