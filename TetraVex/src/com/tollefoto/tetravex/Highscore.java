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

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;


/* Holds a single high score.*/
public class Highscore implements Comparable<Highscore> {
	
	private static final String JSON_ID = "id";
	private static final String JSON_BOARDSIZE = "boardsize";
	private static final String JSON_MOVES = "moves";
	private static final String JSON_TIMER = "timer";

	private UUID mId;
	private int mBoardSize;
	private int mMoves;
	private String mTimer;

	public Highscore(int boardSize, int moves, String timer) {
		//generate unique id
		mId = UUID.randomUUID();
		setBoardSize(boardSize);
		setMoves(moves);
		setTimer(timer);
	}

	public Highscore(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		setBoardSize(json.getInt(JSON_BOARDSIZE));
		setMoves(json.getInt(JSON_MOVES));
		setTimer(json.getString(JSON_TIMER));
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();

		json.put(JSON_ID, getId().toString());
		json.put(JSON_BOARDSIZE, getBoardSize());
		json.put(JSON_MOVES, getMoves());
		json.put(JSON_TIMER, getTimer());
		return json;
	}

	public int getBoardSize() {
		return mBoardSize;
	}

	public void setBoardSize(int boardSize) {
		mBoardSize = boardSize;
	}

	public int getMoves() {
		return mMoves;
	}

	public void setMoves(int moves) {
		mMoves = moves;
	}

	public String getTimer() {
		return mTimer;
	}

	public void setTimer(String timer) {
		mTimer = timer;
	}

	public String toString() {
		String s = String.format("%3d%6d%10s", getBoardSize(), getMoves(), getTimer());
		return s;
	}

	public UUID getId() {
		return mId;
	}

	public int compareTo(Highscore compareScore) {
		int compareMoves = compareScore.getMoves(); 
		//ascending order by board size
		int compareValue = this.mBoardSize - compareScore.getBoardSize();
		if(compareValue == 0) {
			//ascending order by moves and then by time
			compareValue = this.getMoves() - compareMoves;
			if(compareValue == 0)
				compareValue = this.getTimer().compareTo(compareScore.getTimer());
			}
		return compareValue;
	}	

	@Override
	public boolean equals(Object compareScore) {
	    if (!(compareScore instanceof Highscore))
	    	return false;
		Highscore hs = (Highscore)compareScore;
		return hs.getBoardSize() == this.getBoardSize()
				&& hs.getMoves() == this.getMoves()
				&& hs.getTimer().equals(this.getTimer());
	}
}
