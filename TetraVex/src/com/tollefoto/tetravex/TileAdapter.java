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

public class TileAdapter extends BaseAdapter {
    private Context mContext;
    private GameBoardData mGameBoardData;

    public TileAdapter(Context c, GameBoardData gameBoardData) {
        mContext = c;
        mGameBoardData = gameBoardData;
    }

    public boolean areAllItemsEnabled()
    {
        return !winner();
    }

    public int getCount() {
        return mGameBoardData.getSize() * mGameBoardData.getSize();
    }

    public Object getItem(int position) {
    	return mGameBoardData.getData(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean isEnabled(int position)
    {
        return !winner();
    }

    public void setItem(int position, int position2) {
    	mGameBoardData.swap(position, position2);
    	notifyDataSetChanged();
    }

    // create a new TileView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
        TileView tileView;
        TileData tileData = mGameBoardData.getData(position);
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            tileView = new TileView(mContext, null);
            int w = mContext.getResources().getDisplayMetrics().widthPixels;
            tileView.setLayoutParams(new GridView.LayoutParams(w/mGameBoardData.getSize()-5,
            		w/mGameBoardData.getSize()-5));
            tileView.setPadding(0, 0, 0, 0);
        } else
        	tileView = (TileView)convertView;

        tileView.setData(tileData);
        return tileView;
    }

    //Returns true if board is in winning position
    public boolean winner() {
    	return mGameBoardData.winner();
    }

};
