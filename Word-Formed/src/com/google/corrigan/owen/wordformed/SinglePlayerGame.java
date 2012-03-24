package com.google.corrigan.owen.wordformed;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SinglePlayerGame extends Activity
{
	private static final String TAG = SinglePlayerGame.class.getSimpleName();
	static ArrayList<String> wordList = new ArrayList<String>();
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		setContentView(new SinglePlayerGameView(this));
		//game.requestFocus();
	}
	
	@Override
	protected void onDestroy()
	{
		Log.d(TAG, "Destroyed");
		super.onDestroy();
	}
	
	protected void onStop()
	{
		Log.d(TAG, "Stopped");
		super.onStop();
	}
}