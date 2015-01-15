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

import com.configurer.easyscreenlock.setting.AppPreference;
import com.configurer.easyscreenlock.utils.LanguageUtils;

/**
 * Handle Application data initialising, must call at <b> Application First Time </b>.
 * */
public class AppDataInitializer {
	
	public static final String FIRST_TIME = "first_time";

	public static void initializeAppData() {
		
		if (AppPreference.getInt(FIRST_TIME) <= 0) {
			
			/* Initialise i18N <Default Language - English> */
			AppPreference.putString(LanguageUtils.DEFAULT_LANGUAGE, LanguageUtils.LANGUAGE_EN);
			
			AppPreference.putInt(FIRST_TIME, 1);
		}
				
	}
	
}
