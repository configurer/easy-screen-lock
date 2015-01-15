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

package com.configurer.easyscreenlock.ui.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class SensorDetailDialog extends DialogFragment {

	public static final String SENSOR_NAME = "sensor_name";
	public static final String DETAIL = "detail";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String sensorName = null;
		String detail = null;
		
		Bundle args = getArguments();
		if (args != null) {
			sensorName = args.getString(SENSOR_NAME);
			detail = args.getString(DETAIL);
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(sensorName)
			   .setMessage(detail);

		return builder.create();
	}

}
