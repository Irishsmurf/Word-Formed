package com.paddez.wordformed

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainMenuTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mainMenu_displaysAllButtons() {
        // Start the app with the MainMenuScreen
        composeTestRule.setContent {
            WordFormedTheme {
                MainMenuScreen(
                    onNewGame = {},
                    onMultiplayer = {},
                    onHowToPlay = {},
                    onHighScores = {}
                )
            }
        }

        // Check if the title is displayed
        // Note: Using string resource would be better if we had access to it easily in test,
        // but we can also use tags or just the text if it's unique.
        composeTestRule.onNodeWithText("WordFormed").assertIsDisplayed()

        // Check if all menu buttons are present
        // These labels are from strings.xml
        composeTestRule.onNodeWithText("New Game").assertIsDisplayed()
        composeTestRule.onNodeWithText("Start multiplayer game").assertIsDisplayed()
        composeTestRule.onNodeWithText("How to play").assertIsDisplayed()
        composeTestRule.onNodeWithText("High scores").assertIsDisplayed()
    }

    @Test
    fun mainMenu_clickNewGame_triggersCallback() {
        var newGameClicked = false
        composeTestRule.setContent {
            WordFormedTheme {
                MainMenuScreen(
                    onNewGame = { newGameClicked = true },
                    onMultiplayer = {},
                    onHowToPlay = {},
                    onHighScores = {}
                )
            }
        }

        // Click the New Game button
        composeTestRule.onNodeWithText("New Game").performClick()

        // Verify the callback was triggered
        assert(newGameClicked)
    }
}
