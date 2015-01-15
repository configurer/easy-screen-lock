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

package com.configurer.easyscreenlock.setting;

import android.content.Context;
import android.content.SharedPreferences;


public class AppPreference {
	
	protected static final String FILE_NAME = "EASY_SCREEN_LOCK_FILE";
	protected static final int MODE = Context.MODE_PRIVATE;
	
	public static void putInt(String key, int value) {
		SharedPreferences sp = App.getContext().getSharedPreferences(FILE_NAME, MODE);
		sp.edit().putInt(key, value).commit();
	}

	public static int getInt(String key) {
		SharedPreferences sp = App.getContext().getSharedPreferences(FILE_NAME, MODE);
		/* Set 0 to Default Value */
		int defValue = 0;
		return sp.getInt(key, defValue);
	}
	
	public static void putLong(String key, long value) {
		SharedPreferences sp = App.getContext().getSharedPreferences(FILE_NAME, MODE);
		sp.edit().putLong(key, value).commit();
	}
	
	public static long getLong(String key) {
		SharedPreferences sp = App.getContext().getSharedPreferences(FILE_NAME, MODE);
		/* Set 0 to Default Value */
		long defValue = 0;
		return sp.getLong(key, defValue);
	}
	
	public static void putString(String key, String value) {
		SharedPreferences sp = App.getContext().getSharedPreferences(FILE_NAME, MODE);
		sp.edit().putString(key, value).commit();
	}
	
	public static String getString(String key) {
		SharedPreferences sp = App.getContext().getSharedPreferences(FILE_NAME, MODE);
		/* Set null to Default Value */		 
		return sp.getString(key, null);
	}

	public static void resetSharedPreferences() {
		SharedPreferences sp = App.getContext().getSharedPreferences(FILE_NAME, MODE);
		sp.edit().clear().commit();
	}
}
