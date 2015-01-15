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
import java.util.Map;

public class CountryUtils {
	
	public static final String TAG = "EasyScreenLock";
	
	public static final String COUNTRY_ZH = "China"; // zh
	public static final String COUNTRY_EN = "English"; // en
	public static final String COUNTRY_FR = "French"; // fr
	public static final String COUNTRY_DE = "German"; // de 	
	public static final String COUNTRY_IT = "Italy"; // it
	public static final String COUNTRY_JA = "Japan"; // ja 
	public static final String COUNTRY_KO = "Korea"; // ko 
	public static final String COUNTRY_MM = "Myanmar"; // mm 
	public static final String COUNTRY_TH = "Thai"; // th 
	
	public static Map<String, String> countryMap = new HashMap<String, String>();
	
	static {
		countryMap.put(LanguageUtils.LANGUAGE_ZH, COUNTRY_ZH);
		countryMap.put(LanguageUtils.LANGUAGE_EN, COUNTRY_EN);
		countryMap.put(LanguageUtils.LANGUAGE_FR, COUNTRY_FR);
		countryMap.put(LanguageUtils.LANGUAGE_DE, COUNTRY_DE);
		countryMap.put(LanguageUtils.LANGUAGE_IT, COUNTRY_IT);
		countryMap.put(LanguageUtils.LANGUAGE_JA, COUNTRY_JA);
		countryMap.put(LanguageUtils.LANGUAGE_KO, COUNTRY_KO);
		countryMap.put(LanguageUtils.LANGUAGE_MM, COUNTRY_MM);
		countryMap.put(LanguageUtils.LANGUAGE_TH, COUNTRY_TH);
	}
	
	public static String getCountryName(String language) {
		return countryMap.get(language);
	}
	
}
