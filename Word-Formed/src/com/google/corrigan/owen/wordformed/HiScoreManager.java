package com.google.corrigan.owen.wordformed;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

public class HiScoreManager
{
	ArrayList<HighScore> hiScoresTable = new ArrayList<HighScore>();
	Context context;
	
	private class HighScore implements Comparable<HighScore>
	{
		private String name;
		private int score;

		public HighScore(String name, int score)
		{
			this.name = name;
			this.score = score;
		}
		
		public int compareTo(HighScore other)
		{
			return score - other.score;
		}
		
		public void write(FileOutputStream fos) throws IOException
		{
			fos.write(name.getBytes());
			fos.write((score+"").getBytes());
		}
		
		public void get(FileInputStream fis) throws IOException
		{
			name = String.valueOf(fis.read());
			score = Integer.parseInt(String.valueOf(fis.read()));
		}
		
		public void synch(Activity activity, int rank)
		{
			Log.d("WORDFORMED", ""+(rank-1)*2);
			 
			((TextView)activity.findViewById(R.id.name1+(rank-1)*2)).setText(rank + ". " + name);
			((TextView)activity.findViewById(R.id.score1+(rank-1)*2)).setText(score + "");
		}
	}
	
	public HiScoreManager(Context context)
	{
		this.context = context;
		for(int i = 0; i < 20; i++)
			hiScoresTable.add(new HighScore("Default", 0));
	}
	
	public void saveScores()
	{
		try
		{
			FileOutputStream fos = context.openFileOutput("scores", Context.MODE_PRIVATE);
			for(int i = 0; i < hiScoresTable.size(); i++)
			{
				hiScoresTable.get(i).write(fos);
			}
		}
		catch(IOException e)
		{
			Log.d("WORDFORMED", e.getMessage());
		}
	}
	
	public void reset()
	{
		hiScoresTable = new ArrayList<HighScore>();
		for(int i = 0; i < 20; i++)
			hiScoresTable.add(new HighScore("Default", 0));
		saveScores();
	}
	
	public void readScores()
	{
		try
		{
			FileInputStream fis =  context.openFileInput("scores");
			//Save to the 20 strings
			for(int i = 0; i < hiScoresTable.size(); i++)
			{
				hiScoresTable.get(i).get(fis);
			}
			fis.close();
		}
		catch(IOException e)
		{
			Log.d("WORDFORMED", e.getMessage());
		}
	}
	
	public boolean add(String name, int score)
	{
		HighScore tmp = new HighScore(name, score);
		hiScoresTable.add(tmp);
		Collections.sort(hiScoresTable);
		return tmp == hiScoresTable.remove(hiScoresTable.size() - 1);
	}
	
	public void synch(Activity activity)
	{
		Log.d("WORDFORMED", "Synching... " + hiScoresTable.size());
		for(int i = 0; i < hiScoresTable.size(); i++)
			hiScoresTable.get(i).synch(activity, i+1);
	}
}
