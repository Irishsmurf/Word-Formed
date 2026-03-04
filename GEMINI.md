# Word-Formed Project Context

Word-Formed is a Scrabble-inspired mobile game for Android. It features tile-based word formation, scoring, high scores (Room DB), and a countdown timer.

## Project Overview

- **Type:** Modern Android Application
- **Language:** Kotlin (1.9.22)
- **Target SDK:** 34 (Android 14)
- **Min SDK:** 24 (Android 7.0)
- **Main Package:** `com.google.corrigan.owen.wordformed`
- **Architecture:** 
    - MVVM with Jetpack Compose for UI.
    - ViewModel-based state management.
    - Word validation via a serialized dictionary.
    - Persistent storage using Room for high scores.

## Key Components

### Activities & Screens
- **`WordFormed`**: Main entry activity. Handles the splash screen and main menu.
- **`SinglePlayerGame`**: Core game activity managing the single-player game mode.
- **`HiScoreActivity`**: Activity for displaying persistent high scores.
- **`GameOverActivity`**: Final score display and post-game navigation.
- **`HowToPlay`**: Instruction screen.
- **`MultiplayerGame`**: (Possible future or simplified) multiplayer mode activity.

### Game Logic
- **`GameViewModel`**: Manages game state, timer, scoring, and tile selection logic.
- **`TileGenerator`**: Generates random tiles for the game board.
- **`Dictionary`**: Loads and validates words against the serialized `words.dict` file.
- **`Trie`**: Efficient prefix searching and dictionary data structure.
- **`Word`**: Data model representing a word entry.

### Data & Resources
- **`AppDatabase`**, **`HiScoreDao`**, **`HiScore`**: Room database components for persisting high scores.
- **`res/raw/words.dict`**: Primary dictionary file (serialized Java object).
- **`res/raw/word_list.txt`**: Raw text version of the dictionary.
- **`assets/fonts/roboto_thin.ttf`**: Custom font used for UI elements.

## Building and Running

This project uses the Gradle Build System (Kotlin DSL).

### Prerequisites
- Android SDK with API 34 platform.
- Java 17+.

### Build Commands
- **Build Debug APK:**
  ```bash
  ./gradlew assembleDebug
  ```
- **Run Unit Tests:**
  ```bash
  ./gradlew test
  ```
- **Install to Device/Emulator:**
  ```bash
  ./gradlew installDebug
  ```
- **Clean Project:**
  ```bash
  ./gradlew clean
  ```

## Development Conventions

- **UI/UX:** Primarily Jetpack Compose-based UI.
- **Navigation:** Uses standard Intent-based Activity navigation for main screens, with Compose components for internal layouts.
- **Styling:** Material 3 with custom colors defined in `res/values/colors.xml`.
- **Concurrency:** Kotlin Coroutines for asynchronous tasks (e.g., dictionary loading, DB operations).

## Troubleshooting

- **Dictionary Loading:** The dictionary is loaded asynchronously. Ensure initialization completes before word validation is attempted.
- **Database:** Room schema changes require proper migrations or database version increments in `AppDatabase.kt`.
- **Gradle Version:** The wrapper is at Gradle 8.5; ensure the build environment supports it.
