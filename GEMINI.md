# Word-Formed Project Context

Word-Formed is a Scrabble-inspired mobile game for Android. It features tile-based word formation, scoring, high scores (SQLite), and a countdown timer.

## Project Overview

- **Type:** Android Application (Legacy)
- **Language:** Java (compatible with Java 1.6/1.7)
- **Target API:** Android API Level 10 (Gingerbread 2.3.3)
- **Main Package:** `com.google.corrigan.owen.wordformed`
- **Architecture:** 
    - Standard Android Activity-based navigation.
    - Custom game engine using `SurfaceView` and `GameThread` for rendering and game logic.
    - Word validation via a serialized dictionary.
    - Persistent storage using SQLite for high scores.

## Key Components

### Activities
- **`WordFormed`**: Main entry point. Handles the splash screen, background loading of the dictionary via `AsyncTask`, and the main menu.
- **`SinglePlayerGame`**: Container activity for the single-player game mode.
- **`SinglePlayerGameView`**: The core game engine. Extends `SurfaceView`. Manages the game loop, touch events for dragging tiles, rendering the board/UI, and the game timer.
- **`GameOverActivity`**: Displays the final score and allows navigation back to the menu.
- **`HiScoreActivity`**: Displays high scores retrieved from the database.

### Game Logic
- **`DraggableBox`**: Represents a letter tile. Handles its own position, animation, and rendering.
- **`Dictionary`**: Loads a serialized `ArrayList<String>` from `res/raw/words`. Uses `Collections.binarySearch` for fast word validation.
- **`Trie`**: An alternative (possibly unused or for future features) dictionary implementation using a trie data structure for prefix searches and censorship.
- **`TileGenerator`**: Likely generates random tiles for the game.

### Data & Resources
- **`MySQLiteHelper` & `HiScoreDataSource`**: Manage the `hiscores` table in SQLite.
- **`res/raw/words`**: The primary dictionary file (serialized Java object).
- **`res/raw/word_list.txt`**: The raw text version of the dictionary.
- **`assets/fonts/Roboto-Thin.ttf`**: Custom font used for UI elements.

## Building and Running

This project uses the legacy Android Ant build system.

### Prerequisites
- Android SDK with API 10 platform installed.
- Apache Ant.

### Build Commands
- **Build Debug APK:**
  ```bash
  ant debug
  ```
- **Install to Device/Emulator:**
  ```bash
  adb install Word-Formed/bin/Word-Formed-debug.apk
  ```
- **Clean Project:**
  ```bash
  ant clean
  ```

## Development Conventions

- **UI/UX:** The game is designed for portrait orientation (`android:screenOrientation="portrait"`).
- **Styling:** Uses custom themes defined in `res/values/themes.xml` (e.g., `Theme.NoTitleBar`).
- **Logging:** Uses standard Android `Log` with tags like `WORDFORMED`, `HISCORES`, and `SINGLEPLAYERGAMEVIEW`.
- **Assets:** Bitmaps are provided for multiple densities (`hdpi`, `mdpi`, `ldpi`) in `res/drawable-*`.

## Troubleshooting

- **Dictionary Loading:** The dictionary is loaded on a background thread during the splash screen. If the game crashes on startup, check `Dictionary.doLoad`.
- **Custom Font:** Note that `WordFormed.java` has a TODO regarding the custom font crashing the program; verify this before enabling.
- **Build Failures:** Ensure `local.properties` (if present) points to the correct `sdk.dir`.
