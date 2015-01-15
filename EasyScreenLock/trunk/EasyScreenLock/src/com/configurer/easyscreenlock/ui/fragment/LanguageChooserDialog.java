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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.configurer.easyscreenlock.R;
import com.configurer.easyscreenlock.setting.AppPreference;
import com.configurer.easyscreenlock.utils.LanguageUtils;

public class LanguageChooserDialog extends DialogFragment{

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		int defaultCountry = LanguageUtils.getIndex(LanguageUtils.getDefaultLanguage());
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle(R.string.countries_list_title)
	    .setSingleChoiceItems(R.array.countries_list, defaultCountry, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AppPreference.putString(LanguageUtils.DEFAULT_LANGUAGE, LanguageUtils.getLanguage(which));
				dismiss();
				
				restartActivity();
			}
	    });

		return builder.create();
	}
		
	private void restartActivity() {		
	    Intent intent = getActivity().getIntent();
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    getActivity().finish();
	    getActivity().overridePendingTransition(0, 0);

	    startActivity(intent);
	    getActivity().overridePendingTransition(0, 0);		
	}

}
