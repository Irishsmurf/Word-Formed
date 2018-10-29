package com.google.corrigan.owen.wordformed;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.graphics.Typeface;

public class WordFormed extends Activity implements OnClickListener {
    private Context context = this;
    public static Typeface tf;
    Dictionary dict;
    protected Dialog mSplashDialog;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        showSplashScreen();
        try {
            new LoadDictClass().execute();
        } catch (Exception e) {
            Log.d("WORDFORMED", e.getMessage());
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        TileGenerator tiles = new TileGenerator(5);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        TextView tv = (TextView) findViewById(R.id.CustomFontText);
        //TODO: Add this, but at the moment it crashes the program
        tv.setTypeface(tf);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_game_button:
                startActivity(new Intent(this, SinglePlayerGame.class));
                break;
            case R.id.multiplayer_button:
                startActivity(new Intent(this, MultiplayerGame.class));
                break;
            case R.id.how_to_play_button:
                startActivity(new Intent(this, HowToPlay.class));
                break;
            case R.id.high_scores_button:
                startActivity(new Intent(this, HiScoreActivity.class));
                break;
        }

    }

    private class LoadDictClass extends AsyncTask<Object, Integer, Long> {
        protected Long doInBackground(Object... objs) {
            dict = new Dictionary(context);
            return 0l;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            removeSplashScreen();
        }
    }

    /**
     * �* Shows the splash screen over the full Activity
     * �
     */
    protected void showSplashScreen() {
        mSplashDialog = new Dialog(this, android.R.style.Theme);
        mSplashDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSplashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mSplashDialog.setContentView(R.layout.splash);
        mSplashDialog.setCancelable(false);
        mSplashDialog.show();

        // Set Runnable to remove splash screen just in case
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                removeSplashScreen();
            }
        }, 3000);
    }

    protected void removeSplashScreen() {
        if (mSplashDialog != null) {
            mSplashDialog.dismiss();
            mSplashDialog = null;
        }
    }
}
