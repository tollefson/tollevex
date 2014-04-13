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
 * This class hold the data for one tile game piece - the value for each of the four triangles.
 * Any numbers not supplied in the constructor are randomly generated.  The values should be from 1 to 9.
 */
public class TileData {
	/*
	 * min value is assumed by code to be one(1).
	 */
	public final static int MAXVALUE = 9;

	public int mLeft;
	public int mRight;
	public int mTop;
	public int mBottom;

	public boolean mHotLeft = false, mHotRight = false, mHotTop = false, mHotBottom = false;

	private static Random mRandom = new Random();

	public TileData() {
		mLeft = mRandom.nextInt(MAXVALUE) + 1;
		mRight = mRandom.nextInt(MAXVALUE) + 1;
		mTop = mRandom.nextInt(MAXVALUE) + 1;
		mBottom = mRandom.nextInt(MAXVALUE) + 1;
		setHot(false, false, false, false);
	}
	public TileData(int left, int right, int top, int bottom) {
		mLeft = left;
		mRight = right;
		mTop = top;
		mBottom = bottom;
		setHot(false, false, false, false);
	}
	public TileData(TileData td) {
		mLeft = td.mLeft;
		mRight = td.mRight;
		mTop = td.mTop;
		mBottom = td.mBottom;
		setHot(td.mHotLeft, td.mHotRight, mHotTop, mHotBottom);
	}

	public boolean equals(TileData td) {
		return mLeft == td.mLeft && mRight == td.mRight && mTop == td.mTop && mBottom == td.mBottom;
	}
	
	public static TileData createWithLeft(TileData left, TileData top) {
		TileData td;
		if(left == null) {
			if(top == null)
				td = new TileData(getRandom(), getRandom(), getRandom(), getRandom());
			else
				td = new TileData(getRandom(), getRandom(), top.mBottom, getRandom());
		}
		else {
			if( top == null)
				td = new TileData(left.mRight, getRandom(), getRandom(), getRandom());
			else
				td = new TileData(left.mRight, getRandom(), top.mBottom, getRandom());
		}
		return td;
	}

	public void setHot(boolean left, boolean right, boolean top, boolean bottom) {
		mHotLeft = left;
		mHotRight = right;
		mHotTop = top;
		mHotBottom = bottom;
	}

	private static int getRandom() {
		return mRandom.nextInt(MAXVALUE) + 1;
	}
}
