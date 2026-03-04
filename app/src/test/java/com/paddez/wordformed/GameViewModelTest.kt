package com.paddez.wordformed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
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
        viewModel.answerBoxTiles.clear()
        val score = viewModel.submitWord()
        assertEquals(0, score)
        assertEquals(0, viewModel.score)
    }
}
