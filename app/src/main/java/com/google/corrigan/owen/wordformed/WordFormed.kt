package com.google.corrigan.owen.wordformed

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordFormed : AppCompatActivity(), View.OnClickListener {
    private var mSplashDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        
        showSplashScreen()
        
        // Load Dictionary using Coroutines
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                Dictionary.load(this@WordFormed)
            }
            removeSplashScreen()
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.main)
        
        // Initialize UI
        setupUI()
    }

    private fun setupUI() {
        val tv = findViewById<TextView>(R.id.CustomFontText)
        try {
            val tf = Typeface.createFromAsset(assets, "fonts/Roboto-Thin.ttf")
            tv.typeface = tf
        } catch (e: Exception) {
            Log.e("WORDFORMED", "Error loading custom font", e)
        }

        volumeControlStream = AudioManager.STREAM_MUSIC
        
        findViewById<View>(R.id.new_game_button).setOnClickListener(this)
        findViewById<View>(R.id.multiplayer_button).setOnClickListener(this)
        findViewById<View>(R.id.how_to_play_button).setOnClickListener(this)
        findViewById<View>(R.id.high_scores_button).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.new_game_button -> startActivity(Intent(this, SinglePlayerGame::class.java))
            R.id.multiplayer_button -> startActivity(Intent(this, MultiplayerGame::class.java))
            R.id.how_to_play_button -> startActivity(Intent(this, HowToPlay::class.java))
            R.id.high_scores_button -> startActivity(Intent(this, HiScoreActivity::class.java))
        }
    }

    private fun showSplashScreen() {
        mSplashDialog = Dialog(this, android.R.style.Theme).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            setContentView(R.layout.splash)
            setCancelable(false)
            show()
        }

        // Safety timeout to ensure splash is removed even if dictionary load fails
        lifecycleScope.launch {
            delay(5000)
            removeSplashScreen()
        }
    }

    private fun removeSplashScreen() {
        mSplashDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
            mSplashDialog = null
        }
    }
}
