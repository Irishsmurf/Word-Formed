package com.google.corrigan.owen.wordformed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;
import java.util.Random;

import android.util.Log;

public class TileGenerator
{	
	//                     E   A  I  O  N  R  T  L  S  U  D  G  B  C  M  P  F  H  V  W  Y  K  J  X  Q  Z
	private int[] tiles = {12, 9, 9, 8, 6, 6, 6, 4, 4, 4, 4, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1}; // 98 tiles
	private static char[] dist =  { 'E', 'A','I','O','N','R','T','L','S','U','D','G','B','C', 'M', 'P', 'F', 'H','V','W','Y','K','J','X','Q','Z'};
	private static int[] values = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 8, 8, 10, 10 };
	//private int[] tiles = { 12, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
	private static Random rand = new Random(System.currentTimeMillis());
	int place;
	static ArrayList<Character> bag = new ArrayList<Character>();
	
	
	
	private void shuffle()
	{
		System.arraycopy(dist, 0, tiles, 0, 26);
		for(int i = 0; i < 98; i++)
		{
			place = rand.nextInt(27);
			if(tiles[place] != 0)
			{
				tiles[place]--;
			}
		}
	}
	
	public TileGenerator(long seed)
	{
		Log.d("Size", "Dist size = "+dist.length + ", Tiles size = "+tiles.length);
		for(int j = 0; j < dist.length; j++)
			for(int i = 0; i < tiles[j]; i++ )
				bag.add(dist[j]);
	}

	public static char nextTile()
	{
		char let = bag.get(rand.nextInt(26));
		Log.d("Tiles", let+"");
		return let;
	}

	public static int getValue(char letter)
	{
		// TODO Auto-generated method stub
		return values[Arrays.asList(dist).indexOf(letter)];
		
	}
}

