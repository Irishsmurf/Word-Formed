package com.paddez.wordformed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

data class TileState(
    val id: Int,
    val letter: Char,
    val value: Int,
    var position: Offset,
    var isDragging: Boolean = false,
    var targetPosition: Offset? = null
)

class GameViewModel : ViewModel() {
    var score by mutableStateOf(0)
        private set

    var millisRemaining by mutableStateOf(180000L)
        private set

    var isTimeDone by mutableStateOf(false)
        private set

    var isGameOver by mutableStateOf(false)
        private set

    val tiles = mutableStateListOf<TileState>()
    
    // Boxes (Simplified logic for now)
    val createBoxTiles = mutableStateListOf<TileState>()
    val holdBoxTiles = mutableStateListOf<TileState>()
    val answerBoxTiles = mutableStateListOf<TileState>()

    private var timerJob: Job? = null
    private var animationJob: Job? = null

    init {
        startNewGame()
    }

    private fun startNewGame() {
        score = 0
        isGameOver = false
        tiles.clear()
        createBoxTiles.clear()
        holdBoxTiles.clear()
        answerBoxTiles.clear()
        TileGenerator.initBag()

        // Initialize 7 tiles
        for (i in 0 until 7) {
            val letter = TileGenerator.nextTile()
            val tile = TileState(
                id = i,
                letter = letter,
                value = TileGenerator.getValue(letter),
                position = Offset(i * 60f + 20f, 60f)
            )
            tiles.add(tile)
            createBoxTiles.add(tile)
        }

        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            millisRemaining = 180000L
            isTimeDone = false
            while (millisRemaining > 0) {
                delay(1000)
                millisRemaining -= 1000
            }
            isTimeDone = true
            isGameOver = true
        }
    }

    fun onTileMoved(tileId: Int, newOffset: Offset) {
        val index = tiles.indexOfFirst { it.id == tileId }
        if (index != -1) {
            tiles[index] = tiles[index].copy(position = newOffset)
        }
    }

    fun submitWord(): Int {
        val word = answerBoxTiles.joinToString("") { it.letter.toString() }
        if (Dictionary.isWord(word)) {
            val wordScore = answerBoxTiles.sumOf { it.value }
            score += wordScore
            
            // Log for GameOverActivity (legacy static usage for now)
            SinglePlayerGame.wordList.add(Word(word.lowercase().replaceFirstChar { it.uppercase() }, wordScore))
            
            // Remove tiles and generate new ones or move them
            // Simplified: just clear answer box for now
            answerBoxTiles.clear()
            return wordScore
        }
        return 0
    }
}
