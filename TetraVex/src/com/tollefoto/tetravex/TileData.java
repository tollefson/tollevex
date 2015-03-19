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
 * This class hold the data for one tile/square game piece - the value for each of the four triangles.
 * Any numbers not supplied in the constructor are randomly generated.  The values should be from 1 to 9.
 */
public class TileData {
	/*
	 * The minimum number that will appear on the tiles.
	 */
	public final static int MINVALUE = 1;
	/*
	 * The maximum number that will appear on the titles.
	 */
	public final static int MAXVALUE = 9;

    final static String DATASEPARATOR = ",";

	private static Random mRandom = new Random();

	private int mLeft;
	private int mRight;
	private int mTop;
	private int mBottom;

	public TileData(String state) {
        String delimitors = DATASEPARATOR;
        String[] tokens = state.split(delimitors);

        mLeft = Integer.parseInt(tokens[0]);
		mRight = Integer.parseInt(tokens[1]);
		mTop = Integer.parseInt(tokens[2]);
		mBottom = Integer.parseInt(tokens[3]);
	}
	public TileData(int left, int right, int top, int bottom) {
		setLeft(left);
		setRight(right);
		setTop(top);
		setBottom(bottom);
	}

	public static TileData createWithLeft(TileData left, TileData top) {
		TileData td;
		if(left == null) {
			if(top == null)
				td = new TileData(getRandom(), getRandom(), getRandom(), getRandom());
			else
				td = new TileData(getRandom(), getRandom(), top.getBottom(), getRandom());
		}
		else {
			if( top == null)
				td = new TileData(left.getRight(), getRandom(), getRandom(), getRandom());
			else
                td = new TileData(left.getRight(), getRandom(), top.getBottom(), getRandom());
		}
		return td;
	}


	public int getLeft() {
		return mLeft;
	}
	public void setLeft(int left) {
		mLeft = left;
	}
	public int getRight() {
		return mRight;
	}
	public void setRight(int right) {
		mRight = right;
	}
	public int getTop() {
		return mTop;
	}
	public void setTop(int top) {
		mTop = top;
	}
	public int getBottom() {
		return mBottom;
	}
	public void setBottom(int bottom) {
		mBottom = bottom;
	}
	public String tileState() {
        String state = "";
        state += getLeft() + DATASEPARATOR + getRight() + DATASEPARATOR + getTop() + DATASEPARATOR + getBottom();
        return state;
    }

	/*
	 * @return random number between MINVALUE and MAXVALUE.
	 * @see MINVALUE
	 * @see MAXVALUE
	 */
	private static int getRandom() {
		return mRandom.nextInt(MAXVALUE) + MINVALUE;
	}
}
