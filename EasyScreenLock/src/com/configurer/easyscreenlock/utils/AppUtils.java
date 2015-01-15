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

package com.configurer.easyscreenlock.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Window;
import android.view.WindowManager;

import com.configurer.easyscreenlock.R;
import com.configurer.easyscreenlock.WakeUpHelperActivity;

public class AppUtils {

	public static final int notification_id = 1;
	
	public static void startHelperActivity(Context context) {
		Intent intent = new Intent(context, WakeUpHelperActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static void turnScreenOnFlags(Window window) {
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	}
	
	public static void setUpEmailProvider(Context context) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.feedback_email)});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.subject));
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,	context.getString(R.string.body_tip));
		emailIntent.setType("text/plain");
		context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.email_title)));
	}

	public static void uninstallApp(Context context) {
		Intent intent = new Intent(Intent.ACTION_DELETE);
		intent.setData(Uri.parse(context.getString(R.string.uninstall_package_uri)));
		context.startActivity(intent);
	}

	/**
	 * Check if current device android version is lower than 4.0 (API 14).
	 * */
//	public static boolean isLowerAPI() {
//		return android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
//	}
	
	/**
	 * Check if current device android version is equal and higher than 4.2.2 (API 17).
	 * */
//	public static boolean isHeigherAPI() {
//		return android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
//	}
}
