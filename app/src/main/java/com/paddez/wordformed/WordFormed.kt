package com.paddez.wordformed

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
    var visible by remember { mutableStateOf(false) }
    
    // Floating animation for title
    val infiniteTransition = rememberInfiniteTransition(label = "titleFloating")
    val titleOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = SineHalf),
            repeatMode = RepeatMode.Reverse
        ),
        label = "titleOffset"
    )

    LaunchedEffect(Unit) {
        visible = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF38546E), Color(0xFF2A3F54))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.main_title),
                fontFamily = robotoThin,
                fontSize = 56.sp,
                color = Color.White,
                fontWeight = FontWeight.ExtraLight,
                modifier = Modifier
                    .padding(bottom = 48.dp)
                    .offset(y = titleOffset.dp)
            )

            val menuItems = listOf(
                stringResource(R.string.new_game_label) to onNewGame,
                stringResource(R.string.multiplayer_label) to onMultiplayer,
                stringResource(R.string.how_to_play_label) to onHowToPlay,
                stringResource(R.string.high_scores_label) to onHighScores
            )

            menuItems.forEachIndexed { index, item ->
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(500, delayMillis = index * 100)) +
                            slideInVertically(animationSpec = tween(500, delayMillis = index * 100)) { 50 }
                ) {
                    MenuButton(item.first, item.second)
                }
                if (index < menuItems.size - 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "buttonScale")

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .scale(scale),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.95f),
            contentColor = Color(0xFF2A3F54)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 0.dp
        ),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        Text(
            text = text.uppercase(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

private val SineHalf = Easing { x ->
    kotlin.math.sin(x * kotlin.math.PI).toFloat()
}
