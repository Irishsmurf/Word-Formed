package com.google.corrigan.owen.wordformed

import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class SinglePlayerGame : AppCompatActivity() {
    private val TAG = SinglePlayerGame::class.java.simpleName

    companion object {
        @JvmStatic
        var wordList: ArrayList<Word> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        volumeControlStream = AudioManager.STREAM_MUSIC
        
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(SinglePlayerGameView(this))
        
        wordList = ArrayList()
    }

    override fun onDestroy() {
        Log.d(TAG, "Destroyed")
        super.onDestroy()
    }

    override fun onStop() {
        Log.d(TAG, "Stopped")
        super.onStop()
    }
}
