package com.google.corrigan.owen.wordformed;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GameOverActivity extends ListActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over);
		TextView t=(TextView)findViewById(R.id.score_result);
		t.setText("Congratulations, you acheived a score of " + SinglePlayerGameView.getScore());
		
		List<Word> values = SinglePlayerGameView.getWords();

		// Use the SimpleCursorAdapter to show the
		// elements in a ListView
		WordAdapter adapter = new WordAdapter(this, values);
		setListAdapter(adapter);
	}
}
