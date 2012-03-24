package com.google.corrigan.owen.wordformed;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;

public class TileGenerator
{	
	private int[] tiles = {12, 9, 9, 8, 8, 8, 8, 6, 6, 6, 6, 5, 6, 5, 6, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3}; // 98 tiles
	private static char[] dist =  { 'E', 'A','I','O','N','R','T','L','S','U','D','G','B','C', 'M', 'P', 'F', 'H','V','W','Y','K','J','X','Q','Z'};
	private static int[] values = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 8, 8, 10, 10 };
	private static Random rand = new Random(System.currentTimeMillis());
	static ArrayList<Character> bag = new ArrayList<Character>();
	
	

	public TileGenerator(long seed)
	{
		for(int j = 0; j < dist.length; j++)
			for(int i = 0; i < tiles[j]; i++ )
			{
				Log.d("Distrib", dist[j] + "");
				bag.add(dist[j]);
			}
	}

	public static char nextTile()
	{
		int ran = rand.nextInt(bag.size());
		char let = bag.get(ran);
		return let;
	}

	//Returns the value of a Letter Tile
	public static int getValue(char letter)
	{
		for(int i = 0; i < dist.length; i++)
		{
			if(dist[i] == letter)
			{
				return values[i];
			}
		}
		
		return -1;
		
	}
}

