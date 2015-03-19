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

import java.util.Random;

/*
 * This class holds a list of all the tiles for the game.
 */
public class GameBoardData {

	public final static int MAXBOARDSIZE = 5;
	public final static int MINBOARDSIZE = 2;
	public final static int DEFAULTBOARDSIZE = 3;
    final static String ROWSEPARATOR = ";";
    final static String TILESEPARATOR = ":";

	// Number of tiles in each direction
	private int mBoardSize  = 3;
	private TileData mGameBoard[][];
	private static Random mRandom = new Random();


	public GameBoardData(String gameState, int boardSize) {
		if(boardSize < MINBOARDSIZE || boardSize > MAXBOARDSIZE)
			mBoardSize = DEFAULTBOARDSIZE;
		else
			mBoardSize = boardSize;

        mGameBoard = new TileData[mBoardSize][mBoardSize];
        if(gameState!=null && gameState.length() > 0) {
            loadGameState(gameState);
        } else
    		init();
	}

    //@returns game board as a string of numbers with each row
    //  split by SEPARATOR.
    public String saveGameState() {
        String state = "";
        String row = "";

        for (int x = 1; x <= mBoardSize; ++x) {
            for (int y = 1; y <= mBoardSize; ++y) {
                row += mGameBoard[x-1][y-1].tileState();
                if(y < mBoardSize)
                    row += TILESEPARATOR;
            }
            state += row;
            row = "";
            if(x < mBoardSize)
                state += ROWSEPARATOR;
        }
        return state;
    }

    public void loadGameState(String state) {
        String delimitors = ROWSEPARATOR;
        String[] rowTokens = state.split(delimitors);
        for (int x = 1; x <= mBoardSize; ++x) {
            delimitors = TILESEPARATOR;
            String[] tileData = rowTokens[x - 1].split(delimitors);
            for (int y = 1; y <= mBoardSize; ++y) {
                TileData td = new TileData(tileData[y-1]);
                setData(x - 1, y - 1, td);
            }
        }
    }

	public int getSize() {
		return mBoardSize;
	}

	public TileData getData(int position) {
		int pos = 0;
		int x, y;
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
		int x, y;
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

    /*
	 * @return true if game board is in winning position
	 */
	public boolean winner() {
		/*
		 * We don't need to compare when we get to the last tile in each row because
		 * there is no tile to it's right, but we do have to check the tile above it -
		 * except on the first row.
		 */
		for (int x = 1; x <= mBoardSize; ++x) {
			for (int y = 1; y <= mBoardSize; ++y) {
				if(x == 1) { //for top row just check the tile to the right
					if(y!= mBoardSize && mGameBoard[x-1][y-1].getRight() != mGameBoard[x-1][y].getLeft())
						return false;
				} else { //check the tile to the right and above on other rows
					if(( y!= mBoardSize && mGameBoard[x-1][y-1].getRight() != mGameBoard[x-1][y].getLeft()))
						return false;
					if(mGameBoard[x-1][y-1].getTop() != mGameBoard[x-2][y-1].getBottom())
						return false;
				}
			}
		}
		return true; //all edges matched
	}


    //initialize the board with playable game values
	private void init() {
		TileData previousLeftTD = null;
		TileData previousTopTD = null;

		for (int x = 1; x <= mBoardSize; ++x) {
			for (int y = 1; y <= mBoardSize; ++y) {
				if(x > 1) //if we are not in the first row then get the tile above us
					previousTopTD = mGameBoard[x-2][y-1];
				TileData td = TileData.createWithLeft(previousLeftTD, previousTopTD);
				setData(x-1, y-1, td);
				if(y == mBoardSize)
					previousLeftTD = null;
				else
					previousLeftTD = td;
			}
		}
               scramble();
	}
	
	//sets the TileData at the given location
	private void setData(int x, int y, TileData td) {
		mGameBoard[x][y] = td;
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
