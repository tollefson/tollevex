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

	public ArrayList<Highscore> loadHighscores() throws IOException, JSONException {
		ArrayList<Highscore> highscores = new ArrayList<Highscore>();
		BufferedReader reader = null;
		try {
			InputStream input = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(input));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while((line = reader.readLine())!= null) {
				jsonString.append(line);
			}
			//parse
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
				.nextValue();
			for(int i = 0; i < array.length(); i++)
				highscores.add(new Highscore(array.getJSONObject(i)));

		} catch (FileNotFoundException e) {
			//happens when starting the first time
		} finally {
			if(reader!=null)
				reader.close();
		}
		return highscores;
	}
	public void saveHighscores(ArrayList<Highscore> highscores)
		throws JSONException, IOException {
		JSONArray array = new JSONArray();
		for(Highscore hs : highscores)
			array.put(hs.toJSON());
		
		Writer writer = null;
		try {
			OutputStream out = mContext
					.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		} finally {
			if(writer != null)
				writer.close();
		}
	}
}
