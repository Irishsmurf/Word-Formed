package com.paddez.wordformed

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class SinglePlayerGame : ComponentActivity() {
    private val viewModel: GameViewModel by viewModels()

    companion object {
        @JvmStatic
        var wordList: ArrayList<Word> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        volumeControlStream = AudioManager.STREAM_MUSIC
        setFullscreen()

        wordList = ArrayList()

        setContent {
            WordFormedTheme {
                GameScreen(viewModel) {
                    startActivity(Intent(this, GameOverActivity::class.java))
                    finish()
                }
            }
        }
    }
}

@Composable
fun GameScreen(viewModel: GameViewModel, onGameOver: () -> Unit) {
    if (viewModel.isGameOver) {
        LaunchedEffect(Unit) {
            onGameOver()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF38546E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HUD
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val timeLeftText = if (viewModel.isTimeDone) {
                stringResource(R.string.time_done)
            } else {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(viewModel.millisRemaining)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(viewModel.millisRemaining) -
                        TimeUnit.MINUTES.toSeconds(minutes)
                stringResource(R.string.time_label, String.format("%d:%02d", minutes, seconds))
            }
            Text(text = timeLeftText, color = Color.White, fontSize = 20.sp)
            Text(text = stringResource(R.string.score_label, viewModel.score), color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Game Board Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Placeholder for the three boxes (Create, Hold, Answer)
            // In a full implementation, we'd define drop zones here.
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                GameBox(stringResource(R.string.new_letters_label), Color(0x44000000))
                GameBox(stringResource(R.string.hold_letters_label), Color(0x44000000))
                GameBox(stringResource(R.string.form_word_label), Color(0x44000000))
            }

            // Render Tiles
            viewModel.tiles.forEach { tile ->
                DraggableTile(tile) { newOffset ->
                    viewModel.onTileMoved(tile.id, newOffset)
                }
            }
        }

        Button(
            onClick = { viewModel.submitWord() },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
        ) {
            Text(stringResource(R.string.submit_word_label))
        }
    }
}

@Composable
fun GameBox(label: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = Color.White.copy(alpha = 0.3f))
    }
}

@Composable
fun DraggableTile(tile: TileState, onMove: (androidx.compose.ui.geometry.Offset) -> Unit) {
    var offsetX by remember { mutableStateOf(tile.position.x) }
    var offsetY by remember { mutableStateOf(tile.position.y) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .size(50.dp)
            .background(Color.White, RoundedCornerShape(4.dp))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                    onMove(androidx.compose.ui.geometry.Offset(offsetX, offsetY))
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = tile.letter.toString(), color = Color.Black, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
    }
}
