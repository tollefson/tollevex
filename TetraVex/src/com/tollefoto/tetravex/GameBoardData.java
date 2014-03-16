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

import java.util.Random;

/*
 * This class holds a list of all the tiles for the game.
 */
public class GameBoardData {

	public final static int MAXBOARDSIZE = 5;
	public final static int MINBOARDSIZE = 2;
	public final static int DEFAULTBOARDSIZE = 3;


	// Number of tiles in each direction
	public int mBoardSize  = 3;
	private TileData mGameBoard[][];
	private TileData mSolutionGameBoard[][];

	private static Random mRandom = new Random();

	public GameBoardData() {
		mBoardSize = DEFAULTBOARDSIZE;
		init();
	}

	public GameBoardData(int boardSize) {
		if(boardSize < MINBOARDSIZE || boardSize > MAXBOARDSIZE)
			mBoardSize = DEFAULTBOARDSIZE;
		else
			mBoardSize = boardSize;
		init();
	}
	
	//@returns null if x or y position are out of bounds
	public TileData getData(int x, int y) {
		if(x > mBoardSize || x < 1 || y > mBoardSize || y < 1)
			return null;
		return mGameBoard[x][y];
	}
	public TileData getData(int position) {
		int pos = 0;
		int x = 1, y = 1;
		if(position < 0 || position > (mBoardSize * mBoardSize) - 1)
			return null;
		for (x = 1; x <= mBoardSize; ++x) {
			for (y = 1; y <= mBoardSize; ++y) {
				if (position == pos)
					return mGameBoard[x-1][y-1];
				pos = pos + 1;
			}
		}
		return null;
	}

	public void swap(int position1, int position2) {
		int pos = 0;
		int x = 1, y = 1;
		TileData pos1Data = null;
		if(position1 < 0 || position1 > (mBoardSize * mBoardSize) - 1)
			return;
		for (x = 1; x <= mBoardSize; ++x) {
			for (y = 1; y <= mBoardSize; ++y) {
				if (position1 == pos) {
					pos1Data = mGameBoard[x-1][y-1];
					mGameBoard[x-1][y-1] = getData(position2);
				}
				pos = pos + 1;
			}
		}

		//locate and set pos1 at pos2
		pos = 0;
		for (x = 1; x <= mBoardSize; ++x) {
			for (y = 1; y <= mBoardSize; ++y) {
				if (position2 == pos) {
					mGameBoard[x-1][y-1] = pos1Data;
				}
				pos = pos + 1;
			}
		}
	}

	public boolean winner() {
		for (int x = 1; x <= mBoardSize; ++x) {
			for (int y = 1; y <= mBoardSize; ++y) {
				if(!mGameBoard[x-1][y-1].equals(mSolutionGameBoard[x-1][y-1]))
					return false;
			}
		}
		return true; //all positions matched
	}

	//initialize the board with playable game values
	private void init() {
		mGameBoard = new TileData[mBoardSize][mBoardSize];
		mSolutionGameBoard = new TileData[mBoardSize][mBoardSize];

		TileData previousLeftTD = null;
		TileData previousTopTD = null;
		for (int x = 1; x <= mBoardSize; ++x) {
			for (int y = 1; y <= mBoardSize; ++y) {
				if(x > 1)
					previousTopTD = mGameBoard[x-2][y-1];
				TileData td = TileData.createWithLeft(previousLeftTD, previousTopTD);
				mGameBoard[x-1][y-1] = td;
				mSolutionGameBoard[x-1][y-1] = td;
				previousLeftTD = td;
			}
		}
		scramble();
	}
	
	//randomize the playing board for more fun
	private void scramble() {
		for (int x = 1; x <= mBoardSize; ++x) {
			for (int y = 1; y <= mBoardSize; ++y) {
				int newX = mRandom.nextInt(mBoardSize);
				int newY = mRandom.nextInt(mBoardSize);
				TileData temp = mGameBoard[x-1][y-1];
				mGameBoard[x-1][y-1] = mGameBoard[newX][newY];
				mGameBoard[newX][newY] = temp;
			}
		}
	}
}
