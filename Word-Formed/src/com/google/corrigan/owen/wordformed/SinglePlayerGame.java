package com.google.corrigan.owen.wordformed;

import android.app.Activity;
import android.os.Bundle;

public class SinglePlayerGame extends Activity
{
	private SinglePlayerGameView game;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		game = new SinglePlayerGameView(this);
		setContentView(game);
		game.requestFocus();
	}
}