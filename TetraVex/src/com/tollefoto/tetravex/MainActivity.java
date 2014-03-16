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
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {

	final static int NOTSELECTEDPOSITION = -1;
	final static int NUMBEROFCOLUMNS = 3;
	/* The selected tile. */
	int mSelectedPosition = NOTSELECTEDPOSITION;
	private Chronometer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	    final GridView gridview = (GridView) findViewById(R.id.gridview);
	    GameBoardData gbd = new GameBoardData(NUMBEROFCOLUMNS);
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
	        			Toast.makeText(MainActivity.this, "YOU WIN!!!!", Toast.LENGTH_LONG).show();
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


}

