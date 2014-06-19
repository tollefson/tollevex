/*
tollevex - a tetravex like game

Copyright 2014 Jon Tollefson <jon@tollefoto.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.tollefoto.tetravex;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HighscoreFragment extends ListFragment {
	private ListView mListView;
	ArrayAdapter<Highscore> mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
		ScoreKeeper.get(getActivity()).saveHighscores();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		ScoreKeeper sk = ScoreKeeper.get(getActivity());
        mAdapter = new ScoreAdapter(sk.getScores());
		setListAdapter(mAdapter);
	}

	@Override
	/**
	 * We have a custom layout for out list that includes a title, header and monospace typeface
	 * so that items line up.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_highscore,  parent, false);

		mListView = (ListView) v.findViewById(android.R.id.list);
		View header = inflater.inflate(R.layout.list_item_score, null, false);
		mListView.addHeaderView(header, null, false);
		return v;
	}

	private class ScoreAdapter extends ArrayAdapter<Highscore> {
		public ScoreAdapter(ArrayList<Highscore> highscores) {
			super(getActivity(), 0, highscores);
		}

		@Override
	    public boolean isEnabled(int position) {
	        return false;
	    }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = getActivity().getLayoutInflater()
						.inflate(R.layout.list_item_score, null);
			}
			Highscore hs = getItem(position);
			TextView scoreTextView = (TextView)convertView.findViewById(R.id.highscore_list_itemTextView);
			String scoreLine = "";
			if(position < 9)
				scoreLine += " ";//so score counts take the same number of characters
			scoreLine += /*Integer.toString(position+1) + "." +*/ hs.toString();
			scoreTextView.setText(scoreLine);
			return convertView;
		}
	}
}
