package com.paddez.wordformed

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun gameScreen_displaysBoxesAndTiles() {
        val viewModel = GameViewModel()

        composeTestRule.setContent {
            WordFormedTheme {
                GameScreen(viewModel = viewModel, onGameOver = {})
            }
        }

        // Check for Box tags
        composeTestRule.onNodeWithTag("box_NEW_LETTERS").assertIsDisplayed()
        composeTestRule.onNodeWithTag("box_HOLD_LETTERS").assertIsDisplayed()
        composeTestRule.onNodeWithTag("box_FORM_WORD").assertIsDisplayed()
        
        // Check that there are 7 tiles initially
        for (i in 0 until 7) {
            composeTestRule.onNodeWithTag("tile_$i", useUnmergedTree = true).assertIsDisplayed()
        }
    }
}
