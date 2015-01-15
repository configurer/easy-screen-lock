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

package com.configurer.easyscreenlock;

import com.configurer.easyscreenlock.utils.AppUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

/**
 * This Helper Activity is wake up device by using below Intent Flags :
 * <li> {@link WindowManager.LayoutParams.FLAG_FULLSCREEN} </li>
 * <li> {@link WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON} </li>
 * <li> {@link WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED} </li>
 * <li> {@link WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON} </li>
 * 
 * <b><p> After Device is waked up and Screen is On, this activity is automatically finish. </p></b>
 * 
 * */
public class WakeUpHelperActivity extends Activity {

	public static final String TAG = "EasyScreenLock Helper";
	public WindowManager.LayoutParams params;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		
		AppUtils.turnScreenOnFlags(getWindow());
		
		setContentView(R.layout.activity_wake_up_helper);
		
		Log.i(TAG, "Helper Activity started");
	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
		Log.i(TAG, "Helper Activity stopped");
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onResume");
		
		periodicallyShutDownSelf();
	}
	
	/**
	 * Periodically finish self after <b>onResume()</b> after LifeCycle step is called.
	 * */
	protected void periodicallyShutDownSelf() {
		
		new Handler().postDelayed( new Thread() {

			@Override
			public void run() {
				super.run();
				
				WakeUpHelperActivity.this.finish();				
			}
			
		}, 300);
		
	}

}
