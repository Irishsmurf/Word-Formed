package com.paddez.wordformed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@ExperimentalCoroutinesApi
class GameViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: GameViewModel

    @Before
    fun setUp() {
        viewModel = GameViewModel()
    }

    @Test
    fun testInitialState() {
        assertEquals(0, viewModel.score)
        // Initial state should be close to 180,000ms
        assertTrue(viewModel.millisRemaining > 170000L)
        assertFalse(viewModel.isTimeDone)
        assertFalse(viewModel.isGameOver)
        assertEquals(7, viewModel.tiles.size)
    }

    @Test
    fun testSubmitInvalidWord() {
        viewModel.tiles.clear()
        val score = viewModel.submitWord()
        assertEquals(0, score)
        assertEquals(0, viewModel.score)
    }

    @Test
    fun testSnapping() {
        // Mock boundaries
        viewModel.updateBoxBoundaries(BoxType.NEW_LETTERS, Rect(0f, 0f, 1000f, 100f))
        viewModel.updateBoxBoundaries(BoxType.HOLD_LETTERS, Rect(0f, 200f, 1000f, 300f))

        val tileId = viewModel.tiles[0].id
        
        // Drag to Hold Letters (Rect is 0f, 200f, 1000f, 300f)
        // Tile position + 70f center offset should fall within this.
        // If pos is 100, 180 -> center is 170, 250 -> Inside!
        viewModel.onTileMoved(tileId, Offset(100f, 180f))
        viewModel.onTileDragEnded(tileId)

        val updatedTile = viewModel.tiles.first { it.id == tileId }
        assertEquals(BoxType.HOLD_LETTERS, updatedTile.currentBox)
    }
}
