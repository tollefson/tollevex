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
	private int mBoardSize  = 3;
	private TileData mGameBoard[][];

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
	
	public int getSize() {
		return mBoardSize;
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
					setData(x-1, y-1, getData(position2));
				}
				pos = pos + 1;
			}
		}

		//locate and set pos1 data at pos2
		pos = 0;
		for (x = 1; x <= mBoardSize; ++x) {
			for (y = 1; y <= mBoardSize; ++y) {
				if (position2 == pos) {
					setData(x-1, y-1, pos1Data);
				}
				pos = pos + 1;
			}
		}
	}

	public boolean winner() {
		/*
		 * We don't need to compare when we get to the last tile in each row because
		 * there is no tile to it's right, but we do have to check the tile above it -
		 * except on the first row.
		 */
		for (int x = 1; x <= mBoardSize; ++x) {
			for (int y = 1; y <= mBoardSize; ++y) {
				if(x == 1) { //for top row just check the tile to the right
					if(y!= mBoardSize && mGameBoard[x-1][y-1].mRight != mGameBoard[x-1][y].mLeft)
						return false;
				} else { //check the tile to the right and above on other rows
					if(( y!= mBoardSize && mGameBoard[x-1][y-1].mRight != mGameBoard[x-1][y].mLeft))
						return false;
					if(mGameBoard[x-1][y-1].mTop != mGameBoard[x-2][y-1].mBottom)
						return false;
				}
			}
		}
		return true; //all edges matched
	}

	//initialize the board with playable game values
	private void init() {
		mGameBoard = new TileData[mBoardSize][mBoardSize];

		TileData previousLeftTD = null;
		TileData previousTopTD = null;
		for (int x = 1; x <= mBoardSize; ++x) {
			for (int y = 1; y <= mBoardSize; ++y) {
				if(x > 1)
					previousTopTD = mGameBoard[x-2][y-1];
				TileData td = TileData.createWithLeft(previousLeftTD, previousTopTD);
				setData(x-1, y-1, td);
				previousLeftTD = td;
			}
		}
		scramble();
	}
	
	//sets the TileData at the given location
	private void setData(int x, int y, TileData td) {
		mGameBoard[x][y] = td;
		//calculate hot spots
		//don't call this from init because not all spots will be filled.
		return;
	}

	//randomize the playing board for more fun
	private void scramble() {
		TileData temp;
		for (int x = 1; x <= mBoardSize; ++x) {
			for (int y = 1; y <= mBoardSize; ++y) {
				int newX = mRandom.nextInt(mBoardSize);
				int newY = mRandom.nextInt(mBoardSize);
				temp = mGameBoard[x-1][y-1];
				setData(x-1, y-1, mGameBoard[newX][newY]);
				setData(newX, newY, temp);
			}
		}
		//make sure we didn't accidently leave it in a winning position
		if(winner()) {
			temp = mGameBoard[0][0];
			setData(0, 0, mGameBoard[0][1]);
			setData(0, 1, temp);
		}
	}
}
