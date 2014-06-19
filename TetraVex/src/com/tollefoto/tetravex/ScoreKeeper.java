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
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

/**
 * 
 * @author tollefso
 * This is a singleton class that provides the list of high scores.
 */
public class ScoreKeeper {
	private static final String FILENAME = "highscores.json";
	private static final String TAG = "ScoreKeeper";
	private static final int    MAXNUMBEROFSCORES = 10;

	private ArrayList<Highscore> mHighscores;
	private HighscoreJSONSerializer mSerializer;
	private static ScoreKeeper sScoreKeeper;
	private Context mAppContext;
	
	private ScoreKeeper(Context appContext) {
		mAppContext = appContext;
		mSerializer = new HighscoreJSONSerializer(mAppContext, FILENAME);
		
		try {
			mHighscores = mSerializer.loadHighscores();
		} catch (Exception e) {
			mHighscores = new ArrayList<Highscore>();
			Log.e(TAG, "Error laoding high scores", e);
		}
	}
	
	public static ScoreKeeper get(Context c) {
		if(sScoreKeeper == null) {
			sScoreKeeper = new ScoreKeeper(c.getApplicationContext());
		}
		return sScoreKeeper;
	}

	public ArrayList<Highscore> getScores() {
		return mHighscores;
	}

	public void addScore(int boardSize, int moves, String time) {
		Highscore hs = new Highscore(boardSize, moves, time);
		if( !mHighscores.contains(hs))
			mHighscores.add(hs);
		Collections.sort(mHighscores);
		removeExtraScores();
	}

	public Highscore getScore(UUID id) {
		for (Highscore hs: mHighscores) {
			if(hs.getId().equals(id))
				return hs;
		}
		return null;
	}
	
	public boolean saveHighscores() {
		try {
			mSerializer.saveHighscores(mHighscores);
			Log.d(TAG, "high scores saved");
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Error in saving high scores: ", e);
			return false;
		}
	}

	/* Assumes scores are sorted by boardsize. */
	private void removeExtraScores() {
		int scoreBoardSize = 0;
		int scoreCount = 0;
		Iterator<Highscore> itr = mHighscores.iterator();
		while(itr.hasNext()) {
			Highscore hs = itr.next();
			if(hs.getBoardSize()!=scoreBoardSize) {
				scoreBoardSize = hs.getBoardSize();
				scoreCount = 0;
			}
			++scoreCount;
			if(scoreCount > MAXNUMBEROFSCORES)
				itr.remove();
		}
	}
}
