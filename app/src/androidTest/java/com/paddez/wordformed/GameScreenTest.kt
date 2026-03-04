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
    fun gameScreen_displaysInitialHUD() {
        // We'll use the real GameViewModel for now as it's simple enough
        val viewModel = GameViewModel()

        composeTestRule.setContent {
            WordFormedTheme {
                GameScreen(viewModel = viewModel, onGameOver = {})
            }
        }

        // Check for HUD elements
        // The score should start at 0
        composeTestRule.onNodeWithText("Score: 0").assertIsDisplayed()
        
        // Time should start at 3:00 formatted
        composeTestRule.onNodeWithText("Time: 3:00").assertIsDisplayed()
        
        // Check for Box labels
        composeTestRule.onNodeWithText("New Letters").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hold Letters").assertIsDisplayed()
        composeTestRule.onNodeWithText("Form Word").assertIsDisplayed()
        
        // Check for Submit button
        composeTestRule.onNodeWithText("SUBMIT WORD").assertIsDisplayed()
    }
}
