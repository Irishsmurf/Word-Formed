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
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
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
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val timeLeftText = if (viewModel.isTimeDone) {
                    stringResource(R.string.time_done)
                } else {
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(viewModel.millisRemaining)
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(viewModel.millisRemaining) -
                            TimeUnit.MINUTES.toSeconds(minutes)
                    String.format("%d:%02d", minutes, seconds)
                }
                
                Column {
                    Text(text = "TIME", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = timeLeftText, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "SCORE", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = viewModel.score.toString(), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Game Board Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                GameBox(
                    label = stringResource(R.string.new_letters_label),
                    color = Color(0x22000000),
                    onPositioned = { rect -> viewModel.updateBoxBoundaries(BoxType.NEW_LETTERS, rect) }
                )
                GameBox(
                    label = stringResource(R.string.hold_letters_label),
                    color = Color(0x22000000),
                    onPositioned = { rect -> viewModel.updateBoxBoundaries(BoxType.HOLD_LETTERS, rect) }
                )
                GameBox(
                    label = stringResource(R.string.form_word_label),
                    color = Color(0x33000000),
                    height = 120.dp,
                    onPositioned = { rect -> viewModel.updateBoxBoundaries(BoxType.FORM_WORD, rect) }
                )
            }

            // Render Tiles
            viewModel.tiles.forEach { tile ->
                DraggableTile(
                    tile = tile,
                    onMove = { newOffset -> viewModel.onTileMoved(tile.id, newOffset) },
                    onDragStarted = { viewModel.onTileDragStarted(tile.id) },
                    onDragEnded = { viewModel.onTileDragEnded(tile.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = { viewModel.submitWord() },
            enabled = viewModel.isValidWord,
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (viewModel.isValidWord) Color(0xFF4CAF50) else Color.White.copy(alpha = 0.1f),
                contentColor = if (viewModel.isValidWord) Color.White else Color.White.copy(alpha = 0.3f)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.submit_word_label),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun GameBox(
    label: String,
    color: Color,
    height: androidx.compose.ui.unit.Dp = 100.dp,
    onPositioned: (androidx.compose.ui.geometry.Rect) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .onGloballyPositioned { layoutCoordinates ->
                onPositioned(layoutCoordinates.boundsInRoot())
            }
            .background(color, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label.uppercase(),
            color = Color.White.copy(alpha = 0.15f),
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun DraggableTile(
    tile: TileState,
    onMove: (androidx.compose.ui.geometry.Offset) -> Unit,
    onDragStarted: () -> Unit,
    onDragEnded: () -> Unit
) {
    var offsetX by remember { mutableStateOf(tile.position.x) }
    var offsetY by remember { mutableStateOf(tile.position.y) }

    // Update local state when ViewModel state changes (for snapping)
    LaunchedEffect(tile.position) {
        if (!tile.isDragging) {
            offsetX = tile.position.x
            offsetY = tile.position.y
        }
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .size(60.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onDragStarted() },
                    onDragEnd = { onDragEnded() },
                    onDragCancel = { onDragEnded() },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                        onMove(androidx.compose.ui.geometry.Offset(offsetX, offsetY))
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(2.dp)) {
            Text(
                text = tile.letter.toString(),
                color = Color(0xFF2A3F54),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                text = tile.value.toString(),
                color = Color(0xFF2A3F54).copy(alpha = 0.6f),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 4.dp, bottom = 2.dp)
            )
        }
    }
}
