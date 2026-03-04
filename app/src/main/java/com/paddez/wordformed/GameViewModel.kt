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

enum class BoxType {
    NEW_LETTERS,
    HOLD_LETTERS,
    FORM_WORD
}

data class TileState(
    val id: Int,
    val letter: Char,
    val value: Int,
    var position: Offset,
    var currentBox: BoxType = BoxType.NEW_LETTERS,
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
    
    // Box boundaries (will be set by UI)
    private var boxBoundaries = mutableMapOf<BoxType, androidx.compose.ui.geometry.Rect>()

    private var timerJob: Job? = null

    init {
        startNewGame()
    }

    private fun startNewGame() {
        score = 0
        isGameOver = false
        tiles.clear()
        TileGenerator.initBag()

        // Initialize 7 tiles
        for (i in 0 until 7) {
            val letter = TileGenerator.nextTile()
            val tile = TileState(
                id = i,
                letter = letter,
                value = TileGenerator.getValue(letter),
                position = Offset(0f, 0f), // Will be positioned by box logic
                currentBox = BoxType.NEW_LETTERS
            )
            tiles.add(tile)
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

    fun updateBoxBoundaries(boxType: BoxType, rect: androidx.compose.ui.geometry.Rect) {
        boxBoundaries[boxType] = rect
        reorganizeTilesInBox(boxType)
    }

    private fun reorganizeTilesInBox(boxType: BoxType) {
        val rect = boxBoundaries[boxType] ?: return
        val tilesInBox = tiles.filter { it.currentBox == boxType && !it.isDragging }
        
        val tileSize = 140f // Adjust to match UI (approx 50dp * density)
        val spacing = 20f
        val totalWidth = tilesInBox.size * tileSize + (tilesInBox.size - 1) * spacing
        var startX = rect.center.x - totalWidth / 2
        val centerY = rect.center.y - tileSize / 2

        tilesInBox.sortedBy { it.position.x }.forEachIndexed { index, tile ->
            val tileIndex = tiles.indexOfFirst { it.id == tile.id }
            if (tileIndex != -1) {
                val newPos = Offset(startX + index * (tileSize + spacing), centerY)
                tiles[tileIndex] = tiles[tileIndex].copy(position = newPos)
            }
        }
    }

    fun onTileDragStarted(tileId: Int) {
        val index = tiles.indexOfFirst { it.id == tileId }
        if (index != -1) {
            tiles[index] = tiles[index].copy(isDragging = true)
        }
    }

    fun onTileMoved(tileId: Int, newOffset: Offset) {
        val index = tiles.indexOfFirst { it.id == tileId }
        if (index != -1) {
            tiles[index] = tiles[index].copy(position = newOffset)
        }
    }

    fun onTileDragEnded(tileId: Int) {
        val index = tiles.indexOfFirst { it.id == tileId }
        if (index != -1) {
            val tile = tiles[index]
            val center = Offset(tile.position.x + 70f, tile.position.y + 70f) // Half of 140f

            var targetBox = tile.currentBox
            for ((boxType, rect) in boxBoundaries) {
                if (rect.contains(center)) {
                    targetBox = boxType
                    break
                }
            }

            val oldBox = tile.currentBox
            tiles[index] = tiles[index].copy(isDragging = false, currentBox = targetBox)
            
            reorganizeTilesInBox(targetBox)
            if (oldBox != targetBox) {
                reorganizeTilesInBox(oldBox)
            }
        }
    }

    fun submitWord(): Int {
        val answerTiles = tiles.filter { it.currentBox == BoxType.FORM_WORD }.sortedBy { it.position.x }
        val word = answerTiles.joinToString("") { it.letter.toString() }.lowercase()
        
        if (word.isNotEmpty() && Dictionary.isWord(word)) {
            val wordScore = answerTiles.sumOf { it.value }
            score += wordScore
            
            SinglePlayerGame.wordList.add(Word(word.lowercase().replaceFirstChar { it.uppercase() }, wordScore))
            
            // Remove submitted tiles
            val submittedIds = answerTiles.map { it.id }.toSet()
            val remainingTiles = tiles.filter { it.id !in submittedIds }.toMutableList()
            
            // Refill to 7 tiles
            var nextId = (tiles.maxOfOrNull { it.id } ?: 0) + 1
            while (remainingTiles.size < 7) {
                val letter = TileGenerator.nextTile()
                remainingTiles.add(TileState(
                    id = nextId++,
                    letter = letter,
                    value = TileGenerator.getValue(letter),
                    position = Offset(0f, 0f),
                    currentBox = BoxType.NEW_LETTERS
                ))
            }
            
            tiles.clear()
            tiles.addAll(remainingTiles)
            
            reorganizeTilesInBox(BoxType.NEW_LETTERS)
            reorganizeTilesInBox(BoxType.FORM_WORD)
            reorganizeTilesInBox(BoxType.HOLD_LETTERS)
            return wordScore
        }
        return 0
    }
}
