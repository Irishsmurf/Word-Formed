package com.google.corrigan.owen.wordformed

import android.os.Bundle
import android.view.WindowManager
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_over)

        val scoreResult = findViewById<TextView>(R.id.score_result)
        scoreResult.text = "Congratulations, you achieved a score of ${SinglePlayerGameView.score}"

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val values = SinglePlayerGameView.wordsByScore
        val listView = findViewById<ListView>(android.R.id.list)
        val adapter = WordAdapter(this, values)
        listView.adapter = adapter
    }
}
