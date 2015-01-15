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

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.configurer.easyscreenlock.component.AppDataInitializer;
import com.configurer.easyscreenlock.receiver.DeviceAdministratorReceiver;
import com.configurer.easyscreenlock.setting.AppPreference;
import com.configurer.easyscreenlock.ui.adapter.GuideViewPagerAdapter;
import com.configurer.easyscreenlock.ui.fragment.PagerItemFragment;
import com.configurer.easyscreenlock.utils.LanguageUtils;

/**
 * 
 * @author Soe Yan Naing
 * @mailto info.easyscreenlock@gmail.com
 *
 * <p>
 * <b> This is Launcher(Main) Activity </b> <br>
 * This Activity is responsible for automatically Lock/unLock Screen by using Proximity Sensor.
 * </p>
 */
public class MainActivity extends ActionBarActivity implements OnClickListener {

	public static final String TAG = "EasyScreenLock_" + MainActivity.class.getSimpleName();

	private static final int ACTIVATION_REQUEST_CODE= 12123;	
	
	protected Button actionBtn;	
	protected ImageButton guideBtn;
	protected Button close;
	protected View context_view;
	protected RelativeLayout guideLayout;

	protected DevicePolicyManager deviceManger;
	protected ActivityManager activityManager;
	protected ComponentName compName;
	protected LocalDeviceAdminStatusReceiver receiver;
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	protected GuideViewPagerAdapter guideViewPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	protected ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		AppDataInitializer.initializeAppData();
		restoreLanguage();
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onStart() {
		Log.i(TAG, "onStart");
		super.onStart();
//		AppDataInitializer.initializeAppData();
//		restoreLanguage();
//		setContentView(R.layout.activity_main);		
		initializeComponents();
		initializeView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingActivity.class));
			MainActivity.this.finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.action:
			doAction(deviceManger.isAdminActive(compName));
			break;
		case R.id.guide:
			showViewPager();
			break;
		case R.id.close:
			clearViewPager();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			
			switch (requestCode) {
			case ACTIVATION_REQUEST_CODE:
				Toast.makeText(MainActivity.this, getString(R.string.enabled_info), Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}
			
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.i(TAG, "onConfigurationChanged");
		getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());		
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();
		if (deviceManger == null) {
			initializeComponents();			
		}
		initializeView();
		receiver = new LocalDeviceAdminStatusReceiver();
		registerReceiver(receiver, new IntentFilter(LocalDeviceAdminStatusReceiver.class.getName()));
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause");
		super.onPause();
		if (receiver != null)
			unregisterReceiver(receiver);
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "onStop");
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
			activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			compName = new ComponentName(this, DeviceAdministratorReceiver.class);
		} catch (Exception e) {
			deviceManger = null;
			activityManager = null;
			compName = null;
			Log.e(TAG, e.getMessage());
		}
	}
	
	private void restoreLanguage() {
		LanguageUtils.changeLanguage(AppPreference.getString(LanguageUtils.DEFAULT_LANGUAGE), MainActivity.this);
	}

	private void clear() {
		deviceManger = null;
		activityManager = null;
		compName = null;
	}

	private void initializeView() {	
		actionBtn = (Button) findViewById(R.id.action);
		guideBtn = (ImageButton) findViewById(R.id.guide);
		guideLayout = (RelativeLayout) findViewById(R.id.guide_layout);
		close = (Button) guideLayout.findViewById(R.id.close);
		
		setActionName(deviceManger.isAdminActive(compName));
		
		actionBtn.setOnClickListener(this);
		guideBtn.setOnClickListener(this);
		close.setOnClickListener(this);
		
		guideViewPagerAdapter = new GuideViewPagerAdapter(getSupportFragmentManager(), PagerItemFragment.PAGE_1, PagerItemFragment.PAGE_2);
		viewPager = (ViewPager) findViewById(R.id.guide_view_pager);
		viewPager.setAdapter(guideViewPagerAdapter);		
	}
	
	private void showViewPager() {
		
//		if (viewPager == null) {
//			guideViewPagerAdapter = new GuideViewPagerAdapter(getSupportFragmentManager(), PagerItemFragment.PAGE_1, PagerItemFragment.PAGE_2);
//			viewPager = (ViewPager) findViewById(R.id.guide_view_pager);
//			viewPager.setAdapter(guideViewPagerAdapter);
//		}
		
		if (viewPager != null)
			viewPager.setVisibility(View.VISIBLE);
		
		guideLayout.setVisibility(View.VISIBLE);
	}
	
	private void clearViewPager() {
//		if (viewPager != null) {
//			viewPager.removeAllViews();
//			viewPager.invalidate();
//			viewPager = null;
//			guideViewPagerAdapter = null;
//		}
		guideLayout.setVisibility(View.GONE);		
	}

	private void setActionName(boolean isActivated) {
		if (isActivated)
			actionBtn.setText(R.string.disable);
		else
			actionBtn.setText(R.string.enable);
	}

	private void doAction(boolean isActivated) {
		if (isActivated)
			removeActivation();
		else
			activateDeviceAdmin();
	}
	
	private void activateDeviceAdmin() {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.usage_info));
		startActivityForResult(intent, ACTIVATION_REQUEST_CODE);
	}
	
	private void removeActivation() {
		try {
			deviceManger.removeActiveAdmin(compName);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	public class LocalDeviceAdminStatusReceiver extends BroadcastReceiver {
		public static final String DISABLED = "device_admin_disabled";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getBooleanExtra(DISABLED, false)) {
				Toast.makeText(context, context.getString(R.string.uninstall_info), Toast.LENGTH_LONG).show();

				if (deviceManger != null)
					setActionName(deviceManger.isAdminActive(compName));
			}
		}
		
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		MainActivity.this.finish();
	}
	
}
