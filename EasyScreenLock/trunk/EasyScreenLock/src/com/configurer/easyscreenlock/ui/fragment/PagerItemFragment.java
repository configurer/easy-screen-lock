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


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.configurer.easyscreenlock.R;

public class PagerItemFragment extends Fragment{

	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String PAGE_NUMBER = "page_number";
	
	public static final int PAGE_1 = 1;
	public static final int PAGE_2 = 2;
	

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static PagerItemFragment newInstance(int pageNum) {
		PagerItemFragment fragment = new PagerItemFragment();
		Bundle args = new Bundle();
		args.putInt(PAGE_NUMBER, pageNum);
		fragment.setArguments(args);
		return fragment;
	}

	public PagerItemFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		int pageNum = 0;
		
		Bundle args = getArguments();
		if (args != null)
			pageNum = args.getInt(PAGE_NUMBER);
		
		View view = null;
		switch (pageNum) {
		case PAGE_1:
			view = inflater.inflate(R.layout.fragment_guide1, container, false);
			TextView info = (TextView) view.findViewById(R.id.txt_guide_info);
			info.setText(Html.fromHtml(getString(R.string.guide_detail)));
			break;
		case PAGE_2:
			view = inflater.inflate(R.layout.fragment_guide2, container, false);
			break;
		default:
			view = new View(getActivity());
			break;
		}

		return view;
	}
	
}
