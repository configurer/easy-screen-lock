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

import com.configurer.easyscreenlock.MainActivity.LocalDeviceAdminStatusReceiver;
import com.configurer.easyscreenlock.component.ServiceManager;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceAdministratorReceiver extends DeviceAdminReceiver {

	/**
	 * Device administrator is enabled.
	 * Start the Sensor Service.
	 * */
	@Override
	public void onEnabled(Context context, Intent intent) {
		super.onEnabled(context, intent);
		
		ServiceManager.startForegroundService(context);
	}

	/**
	 * Device administrator is disabled.
	 * Stop the Sensor Service.
	 * */
	@Override
	public void onDisabled(Context context, Intent intent) {
		super.onDisabled(context, intent);
		
		/**
		 * Send disabled info to activity's receiver.
		 * */
		Intent disabled_intent = new Intent(LocalDeviceAdminStatusReceiver.class.getName());
		disabled_intent.putExtra(LocalDeviceAdminStatusReceiver.DISABLED, true);
		context.sendBroadcast(disabled_intent);
		
		ServiceManager.stopForegroundService(context);
	}

}