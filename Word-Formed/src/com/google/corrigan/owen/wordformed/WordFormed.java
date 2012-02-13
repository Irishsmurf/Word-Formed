package com.google.corrigan.owen.wordformed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class WordFormed extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Set up click listeners for all the buttons
        View newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(this);
		View multiplayerButton = findViewById(R.id.multiplayer_button);
		multiplayerButton.setOnClickListener(this);
		View how_to_play_button = findViewById(R.id.how_to_play_button);
		how_to_play_button.setOnClickListener(this);
		View highScoresButton = findViewById(R.id.high_scores_button);
		highScoresButton.setOnClickListener(this);
    }

	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.new_game_button:
				break;
			case R.id.multiplayer_button:
				break;
			case R.id.how_to_play_button:
				break;
			case R.id.high_scores_button:
				Intent i = new Intent(this, HiScore.class);
				startActivity(i);
				break;
		}
		
	}
}