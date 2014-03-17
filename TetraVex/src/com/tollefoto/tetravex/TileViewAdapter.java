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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class TileViewAdapter extends BaseAdapter {
    private Context mContext;
    private GameBoardData mGameBoardData;

    public TileViewAdapter(Context c, GameBoardData gameBoardData) {
        mContext = c;
        mGameBoardData = gameBoardData;
    }

    public int getCount() {
        return mGameBoardData.mBoardSize * mGameBoardData.mBoardSize;
    }

    public Object getItem(int position) {
        return 0;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public void setItem(int position, int position2) {
    	TileView tv = mThumbIds[position];
    	mThumbIds[position] = mThumbIds[position2];
    	mThumbIds[position2] = tv;
    	mGameBoardData.swap(position, position2);
    }

    // create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
        TileView tileView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	TileData td = mGameBoardData.getData(position);
            tileView = new TileView(mContext, null, td);
            int w = mContext.getResources().getDisplayMetrics().widthPixels;
            tileView.setLayoutParams(new GridView.LayoutParams(w/mGameBoardData.mBoardSize-5, w/mGameBoardData.mBoardSize-5));
            tileView.setPadding(0, 0, 0, 0);
            mThumbIds[position] = tileView;
        } else {
            tileView = mThumbIds[position];//(TileView)convertView;
        }

        return tileView;
    }

    //Returns true if board is in winning position
    public boolean winner() {
    	boolean w = mGameBoardData.winner();
    	return w;
    }
    // references to our images
   private TileView [] mThumbIds = {null, null, null, null, null,
		   null, null, null, null, null,
		   null, null, null, null, null,
		   null, null, null, null, null,
		   null, null, null, null, null};
    
    };
   