package com.google.corrigan.owen.wordformed;

import java.util.Stack;
import java.util.Random;

public class TileGenerator
{	
	//                     E   A  I  O  N  R  T  L  S  U  D  G  B  C  M  P  F  H  V  W  Y  K  J  X  Q  Z
	//private int[] tiles = {12, 9, 9, 8, 6, 6, 6, 4, 4, 4, 4, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1}; // 98 tiles
	private int[] dist =  { 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
	private int[] tiles = { 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
	private Random rand;
	int place;
	private Stack<DraggableBox> bag;
	
	
	
	
	private void shuffle()
	{
		System.arraycopy(dist, 0, tiles, 0, 26);
		for(int i = 0; i < 98; i++)
		{
			place = rand.nextInt(27);
			if(tiles[place] != 0)
			{
				bag.push(new DraggableBox(place + 'A'));
				tiles[place]--;
			}
		}
	}
	
	public TileGenerator(long seed)
	{
		rand.setSeed(seed);
		shuffle();
	}

	public DraggableBox nextTile()
	{
		if(!bag.empty())
			return bag.pop();
		else
		{
			shuffle();
			return bag.pop();
		}
	}
}

