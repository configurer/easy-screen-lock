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

import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.configurer.easyscreenlock.component.SensorManager;
import com.configurer.easyscreenlock.receiver.DeviceAdministratorReceiver;
import com.configurer.easyscreenlock.setting.AppPreference;
import com.configurer.easyscreenlock.ui.fragment.LanguageChooserDialog;
import com.configurer.easyscreenlock.ui.fragment.SensorDetailDialog;
import com.configurer.easyscreenlock.utils.AppUtils;
import com.configurer.easyscreenlock.utils.CountryUtils;
import com.configurer.easyscreenlock.utils.LanguageUtils;

public class SettingActivity extends ActionBarActivity implements View.OnClickListener{

	public static final String TAG = "EasyScreenLock";
	
	protected DevicePolicyManager deviceManger;
	protected ComponentName compName;
	
	protected Button btnUninstall;
	protected RelativeLayout change_language_layout;
	protected ImageButton btnMailTo;
	protected TextView txt_uninstall_info;
	protected TextView txt_empty, learn_more;
	protected LinearLayout content_list_layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		restoreLanguage();
		setContentView(R.layout.activity_setting);
		initializeLayoutData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initializeComponents();
		initializeLayout();
	}

	@Override
	protected void onStop() {
		super.onStop();
		clear();
	}

	/**
	 * Initialise below components :
	 * <li><b>Device Policy Manager </b> - Public interface for managing policies enforced on a device. </li>
	 * <li><b>Activity Manager </b> - Interact with the overall activities running in the system. </li>
	 * <li><b>Component Name </b> - Identifier for a specific application component.</li>
	 */
	private void initializeComponents() {
		try {
			deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
			compName = new ComponentName(this, DeviceAdministratorReceiver.class);
		} catch (Exception e) {
			deviceManger = null;
			compName = null;
			Log.e(TAG, e.getMessage());
		}
	}
	
	private void restoreLanguage() {
		LanguageUtils.changeLanguage(AppPreference.getString(LanguageUtils.DEFAULT_LANGUAGE), SettingActivity.this);
	}
	
	/**
	 * Initialise most sensitive UI information (at <b>Resume()</b> LifeCycle step)
	 * */
	protected void initializeLayout() {
		btnUninstall = (Button) findViewById(R.id.btn_uninstall);
		btnMailTo = (ImageButton) findViewById(R.id.btn_mail_to);
		txt_uninstall_info = (TextView) findViewById(R.id.txt_uninstall_info);
		change_language_layout = (RelativeLayout) findViewById(R.id.change_language_layout);

		boolean active = deviceManger.isAdminActive(compName);
		
		if (active)
			txt_uninstall_info.setVisibility(View.VISIBLE);
		else
			txt_uninstall_info.setVisibility(View.GONE);
		
		btnUninstall.setEnabled(!deviceManger.isAdminActive(compName));
		
		TextView txt_country_name = (TextView) change_language_layout.findViewById(R.id.txt_country_name);
		String countryName = CountryUtils.getCountryName(LanguageUtils.getDefaultLanguage());
		
		txt_country_name.setText(countryName != null ? countryName : getString(R.string.countries_list_title));
		
		btnUninstall.setOnClickListener(this);
		btnMailTo.setOnClickListener(this);
		change_language_layout.setOnClickListener(this);
	}
	
	/**
	 * Initialise non-sensitive UI information (at <b>onCreate()</b> LifeCycle step)
	 * */
	@SuppressWarnings("static-access")
	protected void initializeLayoutData() {
		// test
//		TextView t = (TextView) findViewById(R.id.outline_0);
//		t.setText("Uninstall ");
//		t.setTypeface(Typeface.createFromAsset(getAssets(), "zawgyi_one.ttf"));
		
		// end
		
		txt_empty = (TextView) findViewById(R.id.empty_list);
		learn_more = (TextView) findViewById(R.id.learn_more);
		content_list_layout = (LinearLayout) findViewById(R.id.content_list_layout);
		
		learn_more.setMovementMethod(LinkMovementMethod.getInstance());
		
		try {
			Map<Integer, String> availableSensorNames = SensorManager.getAvailableSensorNames(SettingActivity.this);

			if (availableSensorNames.isEmpty()) {
				txt_empty.setVisibility(View.VISIBLE);
				
				content_list_layout.setVisibility(View.GONE);
				learn_more.setVisibility(View.GONE);
			} else {
				txt_empty.setVisibility(View.GONE);
				
				content_list_layout.setVisibility(View.VISIBLE);
				learn_more.setVisibility(View.VISIBLE);
				
				SensorManager manager = new SensorManager();
				for (Entry<Integer, String> sensor : availableSensorNames.entrySet()) {
					/* use sensor manager as object reference way in order to avoid static block re-initialising */
					addNewContent(sensor.getValue(), manager.getSensorDetail(sensor.getKey()), content_list_layout);
				}
			}
			
		} catch (Exception e) {
			Log.e(TAG, "[initializeLayoutData] " + e.getMessage());
		}
		
	}
	
	@SuppressLint("InflateParams")
	protected void addNewContent(final String sensorName, final Spanned detail, LinearLayout containerLayout) {
		View contentItemView = getLayoutInflater().inflate(R.layout.fragment_content, null);
		
		((TextView)contentItemView.findViewById(R.id.txt_content)).setText(sensorName);
		
		contentItemView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = new Bundle();
				args.putString(SensorDetailDialog.SENSOR_NAME, sensorName);
				args.putString(SensorDetailDialog.DETAIL, detail.toString());
				
				SensorDetailDialog dialog = new SensorDetailDialog();
				dialog.setArguments(args);
				dialog.show(getSupportFragmentManager(), TAG);
			}
		});
		
		containerLayout.addView(contentItemView);
	}
	
	protected void clear() {
		deviceManger = null;
		compName = null;
	}
	
	/**************************************************** Actions *******************************************************/
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_uninstall:
			AppUtils.uninstallApp(this);			
			break;
			
		case R.id.change_language_layout:
			LanguageChooserDialog dig = new LanguageChooserDialog();			
			dig.show(getSupportFragmentManager(), TAG);
			/*getSupportFragmentManager().executePendingTransactions(); useful when want to add Listener */
			break;
		
		case R.id.btn_mail_to:
			AppUtils.setUpEmailProvider(SettingActivity.this);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		backToParent();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == android.R.id.home) {
			backToParent();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void backToParent() {
		startActivity(new Intent(SettingActivity.this, MainActivity.class));
		SettingActivity.this.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
}
