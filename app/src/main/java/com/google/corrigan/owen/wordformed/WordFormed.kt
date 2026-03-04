package com.google.corrigan.owen.wordformed

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordFormed : ComponentActivity() {
    private var mSplashDialog: Dialog? = null

    private val robotoThin = FontFamily(
        Font(resId = R.font.roboto_thin) // We need to move the font to res/font
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        showSplashScreen()
        
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                Dictionary.load(this@WordFormed)
            }
            removeSplashScreen()
        }

        setContent {
            WordFormedTheme {
                MainMenuScreen(
                    onNewGame = { startActivity(Intent(this, SinglePlayerGame::class.java)) },
                    onMultiplayer = { startActivity(Intent(this, MultiplayerGame::class.java)) },
                    onHowToPlay = { startActivity(Intent(this, HowToPlay::class.java)) },
                    onHighScores = { startActivity(Intent(this, HiScoreActivity::class.java)) }
                )
            }
        }
    }

    private fun showSplashScreen() {
        mSplashDialog = Dialog(this, android.R.style.Theme).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setFullscreen()
            setContentView(R.layout.splash)
            setCancelable(false)
            show()
        }

        lifecycleScope.launch {
            delay(5000)
            removeSplashScreen()
        }
    }

    private fun removeSplashScreen() {
        mSplashDialog?.let {
            if (it.isShowing) it.dismiss()
            mSplashDialog = null
        }
    }
}

@Composable
fun WordFormedTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF38546E),
            background = Color(0xFF38546E),
            surface = Color(0xFF38546E)
        ),
        content = content
    )
}

@Composable
fun MainMenuScreen(
    onNewGame: () -> Unit,
    onMultiplayer: () -> Unit,
    onHowToPlay: () -> Unit,
    onHighScores: () -> Unit
) {
    val robotoThin = FontFamily(Font(R.font.roboto_thin))
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF38546E))
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.main_title),
            fontFamily = robotoThin,
            fontSize = 48.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        MenuButton(stringResource(R.string.new_game_label), onNewGame)
        Spacer(modifier = Modifier.height(20.dp))
        MenuButton(stringResource(R.string.multiplayer_label), onMultiplayer)
        Spacer(modifier = Modifier.height(20.dp))
        MenuButton(stringResource(R.string.how_to_play_label), onHowToPlay)
        Spacer(modifier = Modifier.height(20.dp))
        MenuButton(stringResource(R.string.high_scores_label), onHighScores)
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(text = text, fontSize = 17.sp)
    }
}
