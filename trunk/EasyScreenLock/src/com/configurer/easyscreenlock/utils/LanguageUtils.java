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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.configurer.easyscreenlock.setting.AppPreference;

public class LanguageUtils {

	public static final String TAG = "EasyScreenLock";
	
	/**
	 * SharedPreference key name.
	 * */
	public static final String DEFAULT_LANGUAGE = "DEFAULT_LANGUAGE";
		
	public static final String LANGUAGE_ZH = "zh"; // China
	public static final String LANGUAGE_EN = "en"; // English
	public static final String LANGUAGE_FR = "fr"; // French
	public static final String LANGUAGE_DE = "de"; // German	
	public static final String LANGUAGE_IT = "it"; // Italy
	public static final String LANGUAGE_JA = "ja"; // Japan
	public static final String LANGUAGE_KO = "ko"; // Korea
	public static final String LANGUAGE_MM = "mm"; // Myanmar
	public static final String LANGUAGE_TH = "th"; // Thai	
	
	public static Map<Integer, String> localeList = new HashMap<Integer, String>();
	
	static {
		localeList.put(0, LANGUAGE_ZH);
		localeList.put(1, LANGUAGE_EN);
		localeList.put(2, LANGUAGE_FR);
		localeList.put(3, LANGUAGE_DE);
		localeList.put(4, LANGUAGE_IT);
		localeList.put(5, LANGUAGE_JA);
		localeList.put(6, LANGUAGE_KO);
		localeList.put(7, LANGUAGE_MM);
		localeList.put(8, LANGUAGE_TH);
	}
	
	/**
	 * @param key index of Language Constant.
	 * <p>
	 * return Language constant as below :
	 * </p>
	 * <b>Index : Language </b>
	 * <li> 0 : zh (China) </li>
	 * <li> 1 : en (English </li>
	 * <li> 2 : fr (French) </li>
	 * <li> 3 : de (German) </li>
	 * <li> 4 : it (Italy) </li>
	 * <li> 5 : ja (Japan) </li>
	 * <li> 6 : ko (Korea) </li>
	 * <li> 7 : mm (Myanmar) </li>
	 * <li> 8 : th (Thai) </li>
	 * */
	public static String getLanguage(int key) {
		return localeList.get(key);
	}
	
	public static int getIndex(String language) {
		int index = -1;
		for (Entry<Integer, String> entry : localeList.entrySet()) {
			if (entry.getValue().equalsIgnoreCase(language)) {
				index = entry.getKey();
				break;
			}
				
		}
		return index;
	}
	
	public static void changeLanguage(String language, Context context) {
		try {
			if ( null != language) {
				Locale locale = new Locale(language); 
			    Locale.setDefault(locale);
			    Configuration config = new Configuration();
			    config.locale = locale;
			    context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
			}
			
			Log.i(TAG, "Language changed to " + language);
		} catch (Exception e) {
			Log.e(TAG, "[changeLanguage]" + e.getMessage());
		}
		
	}
	
	/**
	 * @return default Language constant.
	 */
	public static String getDefaultLanguage() {
		return AppPreference.getString(LanguageUtils.DEFAULT_LANGUAGE);
	}
	
}
