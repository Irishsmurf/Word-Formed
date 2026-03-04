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
    var lastStablePosition: Offset = Offset.Zero,
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

    var isValidWord by mutableStateOf(false)
        private set

    val tiles = mutableStateListOf<TileState>()
    
    private var tileSize by mutableStateOf(androidx.compose.ui.unit.IntSize.Zero)

    // Box boundaries (will be set by UI)
    private var boxBoundaries = mutableMapOf<BoxType, androidx.compose.ui.geometry.Rect>()

    private var timerJob: Job? = null

    init {
        startNewGame()
    }

    fun updateTileSize(size: androidx.compose.ui.unit.IntSize) {
        tileSize = size
        // If box boundaries are already set, we can reorganize
        if (boxBoundaries.containsKey(BoxType.NEW_LETTERS)) {
            reorganizeTilesInBox(BoxType.NEW_LETTERS)
        }
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
        if (tileSize == androidx.compose.ui.unit.IntSize.Zero) return

        val tilesInBox = tiles.filter { it.currentBox == boxType && !it.isDragging }
        
        val w = tileSize.width.toFloat()
        val h = tileSize.height.toFloat()
        val spacing = 30f // Increased spacing to prevent touching
        val totalWidth = tilesInBox.size * w + (tilesInBox.size - 1) * spacing
        var startX = rect.center.x - totalWidth / 2
        val centerY = rect.center.y - h / 2

        tilesInBox.sortedBy { it.position.x }.forEachIndexed { index, tile ->
            val tileIndex = tiles.indexOfFirst { it.id == tile.id }
            if (tileIndex != -1) {
                val newPos = Offset(startX + index * (w + spacing), centerY)
                tiles[tileIndex] = tiles[tileIndex].copy(position = newPos)
            }
        }
        
        if (boxType == BoxType.FORM_WORD) {
            checkWordValidity()
        }
    }

    private fun checkWordValidity() {
        val answerTiles = tiles.filter { it.currentBox == BoxType.FORM_WORD }.sortedBy { it.position.x }
        val word = answerTiles.joinToString("") { it.letter.toString() }.lowercase()
        isValidWord = word.length >= 2 && Dictionary.isWord(word)
    }

    fun onTileDragStarted(tileId: Int) {
        val index = tiles.indexOfFirst { it.id == tileId }
        if (index != -1) {
            tiles[index] = tiles[index].copy(isDragging = true, lastStablePosition = tiles[index].position)
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
            val w = tileSize.width.toFloat()
            val h = tileSize.height.toFloat()
            val center = Offset(tile.position.x + w / 2, tile.position.y + h / 2)

            var targetBox: BoxType? = null
            for ((boxType, rect) in boxBoundaries) {
                if (rect.contains(center)) {
                    targetBox = boxType
                    break
                }
            }

            if (targetBox != null) {
                val oldBox = tile.currentBox
                tiles[index] = tiles[index].copy(isDragging = false, currentBox = targetBox)
                reorganizeTilesInBox(targetBox)
                if (oldBox != targetBox) {
                    reorganizeTilesInBox(oldBox)
                }
            } else {
                // Snap back to the box it was already in
                tiles[index] = tiles[index].copy(isDragging = false)
                reorganizeTilesInBox(tile.currentBox)
            }
            checkWordValidity()
        }
    }

    fun onTileFling(tileId: Int) {
        val index = tiles.indexOfFirst { it.id == tileId }
        if (index != -1) {
            val oldBox = tiles[index].currentBox
            tiles.removeAt(index)
            refillTiles()
            reorganizeTilesInBox(oldBox)
            reorganizeTilesInBox(BoxType.NEW_LETTERS)
        }
    }

    private fun refillTiles() {
        var nextId = (tiles.maxOfOrNull { it.id } ?: 0) + 1
        while (tiles.size < 7) {
            val letter = TileGenerator.nextTile()
            tiles.add(TileState(
                id = nextId++,
                letter = letter,
                value = TileGenerator.getValue(letter),
                position = Offset(0f, 0f), // Will be positioned by reorganize call below
                currentBox = BoxType.NEW_LETTERS
            ))
        }
        reorganizeTilesInBox(BoxType.NEW_LETTERS)
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
            tiles.removeAll { it.id in submittedIds }
            
            refillTiles()
            
            reorganizeTilesInBox(BoxType.FORM_WORD)
            reorganizeTilesInBox(BoxType.HOLD_LETTERS)
            return wordScore
        }
        return 0
    }
}
