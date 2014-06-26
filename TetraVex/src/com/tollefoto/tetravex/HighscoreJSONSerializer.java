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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

public class HighscoreJSONSerializer {
	private Context mContext;
	private String mFilename;
	
	public HighscoreJSONSerializer(Context context, String filename) {
		mContext = context;
		mFilename = filename;
	}

	public Context getContext() {
		return mContext;
	}

	public String getFilename() {
		return mFilename;
	}

	public ArrayList<Highscore> loadHighscores() throws IOException, JSONException {
		ArrayList<Highscore> highscores = new ArrayList<Highscore>();
		BufferedReader buffReader = null;
		try {
			InputStream input = getContext().openFileInput(getFilename());
			buffReader = new BufferedReader(new InputStreamReader(input));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while((line = buffReader.readLine())!= null) {
				jsonString.append(line);
			}

			JSONArray jarray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			for(int i = 0; i < jarray.length(); i++)
				highscores.add(new Highscore(jarray.getJSONObject(i)));

		} catch (FileNotFoundException e) {
			//if the first time
		} finally {
			if(buffReader!=null)
				buffReader.close();
		}
		return highscores;
	}

	public void saveHighscores(ArrayList<Highscore> highscores)
		throws JSONException, IOException {
		JSONArray jarray = new JSONArray();
		for(Highscore hs : highscores)
			jarray.put(hs.toJSON());
		
		Writer writer = null;
		try {
			OutputStream outStream = getContext().openFileOutput(getFilename(), Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(outStream);
			writer.write(jarray.toString());
		} finally {
			if(writer != null)
				writer.close();
		}
	}
}
