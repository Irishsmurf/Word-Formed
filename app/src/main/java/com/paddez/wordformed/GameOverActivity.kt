package com.paddez.wordformed

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class GameOverActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setFullscreen()

        setContent {
            WordFormedTheme {
                GameOverScreen(
                    score = SinglePlayerGame.wordList.sumOf { it.score },
                    words = SinglePlayerGame.wordList.sortedByDescending { it.score }
                )
            }
        }
    }
}

@Composable
fun GameOverScreen(score: Int, words: List<Word>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF38546E))
            .padding(16.dp)
    ) {
        Text(
            text = "Game Over",
            fontSize = 32.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Congratulations, you achieved a score of $score",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Words Used:",
            fontSize = 18.sp,
            color = Color.LightGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(words) { word ->
                WordRow(word)
            }
        }
    }
}

@Composable
fun WordRow(word: Word) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = word.word,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = word.score.toString(),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
