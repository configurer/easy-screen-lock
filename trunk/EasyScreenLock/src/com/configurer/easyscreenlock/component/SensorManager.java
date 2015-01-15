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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.configurer.easyscreenlock.R;
import com.configurer.easyscreenlock.setting.App;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Sensor;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseArray;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@SuppressLint("UseSparseArrays")
@SuppressWarnings("deprecation")
public class SensorManager {
		
	public static String TAG = "EasyScreenLock";
	
	/**
	 * reference : {@link link http://developer.android.com/reference/android/util/SparseArray.html}
	 * 
	 * <p>
	 * SparseArrays map integers to Objects. Unlike a normal array of Objects, there can be gaps in the indices. 
	 * It is intended to be more memory efficient than using a HashMap to map Integers to Objects, 
	 * both because it avoids auto-boxing keys and its data structure doesn't rely on an extra entry object for each mapping.
	 * </p> 
	 * */
	public static SparseArray<Spanned> sparseArraySensorList = new SparseArray<Spanned>();
	
	static {
		try {
			Context context = App.getContext();
			sparseArraySensorList.put(Sensor.TYPE_ACCELEROMETER, Html.fromHtml(context.getString(R.string.TYPE_ACCELEROMETER)));
			sparseArraySensorList.put(Sensor.TYPE_MAGNETIC_FIELD, Html.fromHtml(context.getString(R.string.TYPE_MAGNETIC_FIELD)));
			sparseArraySensorList.put(Sensor.TYPE_ORIENTATION, Html.fromHtml(context.getString(R.string.TYPE_ORIENTATION)));
			sparseArraySensorList.put(Sensor.TYPE_GYROSCOPE, Html.fromHtml(context.getString(R.string.TYPE_GYROSCOPE)));
			sparseArraySensorList.put(Sensor.TYPE_LIGHT, Html.fromHtml(context.getString(R.string.TYPE_LIGHT)));
			sparseArraySensorList.put(Sensor.TYPE_PRESSURE, Html.fromHtml(context.getString(R.string.TYPE_PRESSURE)));
			sparseArraySensorList.put(Sensor.TYPE_TEMPERATURE, Html.fromHtml(context.getString(R.string.TYPE_TEMPERATURE)));
			sparseArraySensorList.put(Sensor.TYPE_PROXIMITY, Html.fromHtml(context.getString(R.string.TYPE_PROXIMITY)));
			sparseArraySensorList.put(Sensor.TYPE_GRAVITY, Html.fromHtml(context.getString(R.string.TYPE_GRAVITY)));
			sparseArraySensorList.put(Sensor.TYPE_LINEAR_ACCELERATION, Html.fromHtml(context.getString(R.string.TYPE_LINEAR_ACCELERATION)));
			sparseArraySensorList.put(Sensor.TYPE_ROTATION_VECTOR, Html.fromHtml(context.getString(R.string.TYPE_ROTATION_VECTOR)));
			sparseArraySensorList.put(Sensor.TYPE_RELATIVE_HUMIDITY, Html.fromHtml(context.getString(R.string.TYPE_RELATIVE_HUMIDITY)));
			sparseArraySensorList.put(Sensor.TYPE_AMBIENT_TEMPERATURE, Html.fromHtml(context.getString(R.string.TYPE_AMBIENT_TEMPERATURE)));
		} catch (Exception e) {
			sparseArraySensorList.clear();
			Log.e(TAG, "static sparseArraySensorList initializing error " + e.getMessage());
		}
	}
	
	public static Spanned getSensorDetail(int type) {
		return sparseArraySensorList.get(type, null);
	}
	
	/**
	 * return Sensor map [SensorType - integer, SensorName - String] from current available sensors.
	 * */
	public static Map<Integer,String> getAvailableSensorNames(Context context) {

		Map<Integer,String> map = null;

		try {
			map = new HashMap<Integer, String>();
			
			android.hardware.SensorManager mSensorManager =  (android.hardware.SensorManager) context.getSystemService(Context.SENSOR_SERVICE);			
			List<Sensor> sensorlist = mSensorManager.getSensorList(Sensor.TYPE_ALL);
    	    
			if (!sensorlist.isEmpty()) {
		    	for (Sensor s : sensorlist) {
		    		map.put(s.getType(), s.getName());
		    	}
			}
	    
		} catch (Exception e) {
			map.clear();
			Log.e(TAG, e.getMessage());
		}	
		
		return map;
	}
	
}
