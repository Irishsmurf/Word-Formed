package com.paddez.wordformed

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class HiScoreActivity : ComponentActivity() {
    private val viewModel: HiScoreViewModel by viewModels {
        HiScoreViewModelFactory(AppDatabase.getDatabase(this).hiScoreDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setFullscreen()

        setContent {
            WordFormedTheme {
                HiScoreScreen(viewModel)
            }
        }
    }
}

@Composable
fun HiScoreScreen(viewModel: HiScoreViewModel) {
    val scores by viewModel.hiScores.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF38546E))
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.high_score_title),
            fontSize = 32.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(scores) { index, hiScore ->
                HiScoreRow(index + 1, hiScore)
            }
        }
    }
}

@Composable
fun HiScoreRow(rank: Int, hiScore: HiScore) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$rank. ${hiScore.name}",
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = hiScore.score.toString(),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
