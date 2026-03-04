package com.paddez.wordformed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat

class HowToPlay : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordFormedTheme {
                HowToPlayScreen()
            }
        }
    }
}

@Composable
fun HowToPlayScreen() {
    val scrollState = rememberScrollState()
    val instructionsHtml = stringResource(R.string.how_to_play_text)
    val instructionsText = HtmlCompat.fromHtml(instructionsHtml, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF38546E))
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = stringResource(R.string.how_to_play_title),
            fontSize = 32.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = instructionsText,
            fontSize = 18.sp,
            color = Color.White,
            lineHeight = 28.sp
        )
    }
}
