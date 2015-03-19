/*
tollevex - a tetravex like game

Copyright 2014,2015 Jon Tollefson <jon@tollefoto.com>

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

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final static int NOTSELECTEDPOSITION = -1;
    static final String STATE_NUMBEROFCOLUMNS = "numberOfColumns";
    static final String STATE_GAMEBOARD = "gameBoard";
    static final String STATE_MOVES = "moveCount";
    static final String STATE_CURRENTTIME = "currentTime";

	/* Zero or one tile can be selected at a time */
	private int mSelectedPosition = NOTSELECTEDPOSITION;
	private int mNumberOfMoves = 0;
	private SharedPreferences sharedPrefs;
	private Chronometer mTimer;
	/*Save current timer value when game is paused*/
	private long mPauseTime = 0;
	private TextView mMovesTextView;
	private TextView mMovesLabelTextView;
	private Button mNewGameButton;
	private GameBoardData mGbd;
	private GridView mGridview;

    private void newGame(Bundle savedInstanceState) {
		int numberOfColumns;
        String gameState = "";
        int stoppedTime = 0;

        if (savedInstanceState != null) {
            // Restore state members from saved instance
            numberOfColumns = savedInstanceState.getInt(STATE_NUMBEROFCOLUMNS);
            gameState = savedInstanceState.getString(STATE_GAMEBOARD);
            mNumberOfMoves = savedInstanceState.getInt(STATE_MOVES);
            String currentTime = savedInstanceState.getString(STATE_CURRENTTIME);
            String timeSplits[] = currentTime.split(":");
            if (timeSplits.length == 2) {
                stoppedTime = Integer.parseInt(timeSplits[0]) * 60 * 1000
                        + Integer.parseInt(timeSplits[1]) * 1000;
            } else if (timeSplits.length == 3) {
                stoppedTime = Integer.parseInt(timeSplits[0]) * 60 * 60 * 1000
                        + Integer.parseInt(timeSplits[1]) * 60 * 1000
                        + Integer.parseInt(timeSplits[2]) * 1000;
            }
        }
        else {
            numberOfColumns = Integer.parseInt(sharedPrefs.getString(getString(R.string.pref_boardsize_key), "3"));
            mNumberOfMoves = 0;
        }

        mTimer.setBase(SystemClock.elapsedRealtime() - stoppedTime);
	    mGbd = new GameBoardData(gameState, numberOfColumns);
	    mGridview.setAdapter(new TileAdapter(this, mGbd));
	    mGridview.setNumColumns(mGbd.getSize());
	    mMovesTextView.setText(Integer.toString(mNumberOfMoves));
        if(((TileAdapter)(mGridview.getAdapter())).winner())
            mNewGameButton.setVisibility(View.VISIBLE);
        else {
            mNewGameButton.setVisibility(View.INVISIBLE);
            mTimer.start();
        }
	}

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT < 16) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //Remove title bar
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            }
            else {
                View decorView = getWindow().getDecorView();
                // Hide the status bar.
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
                // Remember that you should never show the action bar if the
                // status bar is hidden, so hide that too if necessary.
                ActionBar actionBar = getActionBar();
                if (actionBar != null) {
                    actionBar.hide();
                }
            }
        }

        setContentView(R.layout.activity_main);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.OnSharedPreferenceChangeListener sharedPreflistener;

		/*Set the initial visibility for the timer*/
	    mTimer = (Chronometer) findViewById(R.id.timer);
		boolean displayTimer = sharedPrefs.getBoolean(getString(R.string.pref_display_timer_key), false);
		if(!displayTimer)
			mTimer.setVisibility(View.INVISIBLE);

		/*Set the initial visibility for the moves counter*/
		mMovesTextView = (TextView)findViewById(R.id.movesview);
		mMovesLabelTextView = (TextView)findViewById(R.id.label_movesview);
		boolean displayMoves = sharedPrefs.getBoolean(getString(R.string.pref_display_moves_key), false);
		if(!displayMoves) {
			mMovesTextView.setVisibility(View.INVISIBLE);
			mMovesLabelTextView.setVisibility(View.INVISIBLE);
		}

		mNewGameButton = (Button)findViewById(R.id.new_game_button);
		mNewGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				newGame(null);
			}
		});

		/* Listen for changes to the visibility of the moves counter and timer*/
		sharedPreflistener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			  public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
				  if(key.equals(getString(R.string.pref_display_timer_key))) {
					  if(prefs.getBoolean(key, false))
						  mTimer.setVisibility(View.VISIBLE);
					  else
						  mTimer.setVisibility(View.INVISIBLE);
				  } else if(key.equals(getString(R.string.pref_display_moves_key))) {
					  if(prefs.getBoolean(key, false)) {
						  mMovesTextView.setVisibility(View.VISIBLE);
						  mMovesLabelTextView.setVisibility(View.VISIBLE);
					  }
					  else {
						  mMovesTextView.setVisibility(View.INVISIBLE);
						  mMovesLabelTextView.setVisibility(View.INVISIBLE);
					  }
				 }
			  }
			};
		sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPreflistener);

		/*Listen for touches to the tiles*/
	    mGridview = (GridView) findViewById(R.id.gridview);
	    mGridview.setOnItemClickListener(new OnItemClickListener() {
	    	/*When a tile is touched we save the position and highlight the tile.
	    	 * If a tile is already selected and another is touched then we swap the two tiles.
	    	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	    	 */
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	TileAdapter tva = ((TileAdapter)(parent.getAdapter()));
	        	if(mSelectedPosition != NOTSELECTEDPOSITION && mSelectedPosition != position) {
	        		//swap positions
	        		tva.setItem(position, mSelectedPosition);
	        		++mNumberOfMoves;
	        		mMovesTextView.setText(Integer.toString(mNumberOfMoves));
	        		mSelectedPosition = NOTSELECTEDPOSITION;
	        		if(tva.winner()) {
	        			mTimer.stop();
                        mNewGameButton.setVisibility(View.VISIBLE);
	        			//save score if high score
	        			ScoreKeeper sk = ScoreKeeper.get(parent.getContext());
	        			sk.addScore(mGbd.getSize(), mNumberOfMoves, mTimer.getText().toString());
	        			Toast.makeText(MainActivity.this, "You are a winner!!!!", Toast.LENGTH_LONG).show();
	        		}
	        	}
	        	else if(mSelectedPosition == position) {//if we touch the same tile again we deselect it
	        		mSelectedPosition = NOTSELECTEDPOSITION;
	        		v.setSelected(false);
	        	}
	        	else {
	        		mSelectedPosition = position;
	        		v.setSelected(true);
	        	}
	        }
	    });

   	    newGame(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		//don't start clock if they have already won
		if(!((TileAdapter)(mGridview.getAdapter())).winner()) {
			if(mPauseTime != 0) {
				mTimer.setBase(mTimer.getBase() + SystemClock.elapsedRealtime() - mPauseTime);
				mPauseTime = 0;
			}
		    mTimer.start();
		}
	}

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save the user's current game state
        mPauseTime = SystemClock.elapsedRealtime();
        mTimer.stop();
        savedInstanceState.putString(STATE_CURRENTTIME, mTimer.getText().toString());
        savedInstanceState.putInt(STATE_MOVES, mNumberOfMoves);
        savedInstanceState.putInt(STATE_NUMBEROFCOLUMNS, mGbd.getSize());
        savedInstanceState.putString(STATE_GAMEBOARD, mGbd.saveGameState());
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
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
	        newGame(null);
	        return true;
	    case R.id.action_highscores:
	    	Intent highscoresIntent = new Intent(this, HighscoreActivity.class);
	    	startActivity(highscoresIntent);
	    	return true;
	    case R.id.action_settings:
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
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
