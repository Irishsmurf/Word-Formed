package com.google.corrigan.owen.wordformed;

import android.util.Log;

public class HiScore implements Comparable<HiScore>
	{
		private long id;
		private String name;
		private int score;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		// Will be used by the ArrayAdapter in the ListView
		@Override
		public String toString() {
			return name;
		}
		
		public void setScore(int score)
		{
			this.score = score;
		}
		
		public int getScore()
		{
			return score;
		}

		public int compareTo(HiScore other)
		{
			return other.score - score;
		}
	}