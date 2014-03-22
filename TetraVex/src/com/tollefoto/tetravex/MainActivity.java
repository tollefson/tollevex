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
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	final static int NOTSELECTEDPOSITION = -1;
	/* The selected tile. */
	int mSelectedPosition = NOTSELECTEDPOSITION;
	int mNumberOfMoves = 0;
	private SharedPreferences sharedPrefs;
	private Chronometer timer;
	private SharedPreferences.OnSharedPreferenceChangeListener listener;
	private GameBoardData gbd;
	private GridView gridview;
	private TextView mMovesTextView;

	private void newGame() {
		String numberofcolumns = sharedPrefs.getString(getString(R.string.pref_boardsize_key), "3");

	    gbd = new GameBoardData(Integer.parseInt(numberofcolumns));
	    gridview.setAdapter(new TileViewAdapter(this, gbd));
	    gridview.setNumColumns(gbd.mBoardSize);
	    mNumberOfMoves = 0;
	    mMovesTextView.setText(Integer.toString(mNumberOfMoves));
	    timer.setBase(SystemClock.elapsedRealtime());
	    timer.start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
 
		boolean displayTimer = sharedPrefs.getBoolean(getString(R.string.pref_display_timer_key), false);
		if(!displayTimer)
			findViewById(R.id.timer).setVisibility(View.INVISIBLE);

		mMovesTextView = (TextView)findViewById(R.id.movesview);
		boolean displayMoves = sharedPrefs.getBoolean(getString(R.string.pref_display_moves_key), false);
		if(!displayMoves) {
			mMovesTextView.setVisibility(View.INVISIBLE);
			findViewById(R.id.label_movesview).setVisibility(View.INVISIBLE);
		}

		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			  public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
				  if(key.equals(getString(R.string.pref_display_timer_key))) {
					  if(prefs.getBoolean(key, false))
						  findViewById(R.id.timer).setVisibility(View.VISIBLE);
					  else
						  findViewById(R.id.timer).setVisibility(View.INVISIBLE);
				  } else if(key.equals(getString(R.string.pref_display_moves_key))) {
					  if(prefs.getBoolean(key, false)) {
						  mMovesTextView.setVisibility(View.VISIBLE);
						  findViewById(R.id.label_movesview).setVisibility(View.VISIBLE);
					  }
					  else {
						  mMovesTextView.setVisibility(View.INVISIBLE);
						  findViewById(R.id.label_movesview).setVisibility(View.INVISIBLE);
					  }
				 }
			  }
			};
		sharedPrefs.registerOnSharedPreferenceChangeListener(listener);

	    timer = (Chronometer) findViewById(R.id.timer);
	    gridview = (GridView) findViewById(R.id.gridview);

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	if(mSelectedPosition != NOTSELECTEDPOSITION && mSelectedPosition != position) {
	        		//Toast.makeText(MainActivity.this, "swapping " + position + " with " + mSelectedPosition, Toast.LENGTH_SHORT).show();
	        		//swap positions
	        		((TileViewAdapter)(parent.getAdapter())).setItem(position, mSelectedPosition);
	        		++mNumberOfMoves;
	        		mMovesTextView.setText(Integer.toString(mNumberOfMoves));
	        		mSelectedPosition = NOTSELECTEDPOSITION;
	        		((TileViewAdapter)(parent.getAdapter())).notifyDataSetChanged();
	        		if(((TileViewAdapter)(parent.getAdapter())).winner()) {
	        			timer.stop();
	        			Toast.makeText(MainActivity.this, "You are a winner!!!!", Toast.LENGTH_LONG).show();
	        		}
	        	}
	        	else {
	        		mSelectedPosition = position;
	        		TileView tv = (TileView)(((TileViewAdapter)(parent.getAdapter())).getItem(position));
	        		tv.setSelected(true);
	        		tv.requestFocusFromTouch();
	        	}
	        }
	    });

	    newGame();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.action_newgame:
	        newGame();
	        return true;
	    case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
	        return true;
	    case R.id.action_help:
	    	Dialog dialog = new Dialog(this);
	    	dialog.setContentView(R.layout.activity_help);
	    	dialog.setTitle(getString(R.string.app_name));
	    	dialog.setCancelable(true);
	    	dialog.show();
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}
