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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {

	final static int NOTSELECTEDPOSITION = -1;
	/* The selected tile. */
	int mSelectedPosition = NOTSELECTEDPOSITION;
	private Chronometer timer;
	private SharedPreferences.OnSharedPreferenceChangeListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 
		boolean displayTimer = sharedPrefs.getBoolean("pref_display_timer", false);
		if(!displayTimer)
			findViewById(R.id.timer).setVisibility(View.INVISIBLE);
		String numberofcolumns = sharedPrefs.getString("pref_boardsize", "3");
		
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			  public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
				  if(key.equals("pref_display_timer")) {
					  if(prefs.getBoolean(key, false))
						  findViewById(R.id.timer).setVisibility(View.VISIBLE);
					  else
						  findViewById(R.id.timer).setVisibility(View.INVISIBLE);
				  }
			  }
			};
			sharedPrefs.registerOnSharedPreferenceChangeListener(listener);

	    final GridView gridview = (GridView) findViewById(R.id.gridview);
	    GameBoardData gbd = new GameBoardData(Integer.parseInt(numberofcolumns));
	    gridview.setAdapter(new TileViewAdapter(this, gbd));
	    gridview.setNumColumns(gbd.mBoardSize);
	    timer = (Chronometer) findViewById(R.id.timer);
	    timer.setBase(SystemClock.elapsedRealtime());
	    timer.start();

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	if(mSelectedPosition != NOTSELECTEDPOSITION && mSelectedPosition != position) {
	        		//Toast.makeText(MainActivity.this, "swapping " + position + " with " + mSelectedPosition, Toast.LENGTH_SHORT).show();
	        		//swap positions
	        		((TileViewAdapter)(parent.getAdapter())).setItem(position, mSelectedPosition);
	        		mSelectedPosition = NOTSELECTEDPOSITION;
	        		((TileViewAdapter)(parent.getAdapter())).notifyDataSetChanged();
	        		if(((TileViewAdapter)(parent.getAdapter())).winner()) {
	        			timer.stop();
	        			Toast.makeText(MainActivity.this, "You are a winner!!!!", Toast.LENGTH_LONG).show();
	        		}
	        	} else {
	        		mSelectedPosition = position;
	        		v.setSelected(true);
	        		v.requestFocusFromTouch();
	        	}
	        }
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
		return true;
	}


}

