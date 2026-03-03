package com.google.corrigan.owen.wordformed

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HiScoreActivity : AppCompatActivity() {
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hiscore)
        
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        listView = findViewById(android.R.id.list)

        loadHighScores()
    }

    private fun loadHighScores() {
        lifecycleScope.launch {
            Log.d("HISCORES", "In HiScoreActivity, reading all scores")
            val scores = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(this@HiScoreActivity).hiScoreDao().getAllHiScores()
            }
            
            val adapter = HiScoreAdapter(this@HiScoreActivity, scores)
            listView.adapter = adapter
        }
    }
}
