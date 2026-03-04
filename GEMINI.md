# Word-Formed Project Context

Word-Formed is a Scrabble-inspired mobile game for Android. It features tile-based word formation, scoring, high scores (Room DB), and a countdown timer.

## Project Overview

- **Type:** Modern Android Application
- **Language:** Kotlin (1.9.22)
- **Target SDK:** 35 (Android 15)
- **Min SDK:** 24 (Android 7.0)
- **Main Package:** `com.paddez.wordformed`
- **Architecture:** 
    - MVVM with Jetpack Compose for UI.
    - ViewModel-based state management.
    - Word validation via a Trie-based dictionary.
    - Persistent storage using Room for high scores.
    - Automated CI/CD with GitHub Actions and Google Play Store integration.

## Key Components

### Activities & Screens
- **`WordFormed`**: Main entry activity. Handles the splash screen and main menu.
- **`SinglePlayerGame`**: Core game activity managing the single-player game mode.
- **`HiScoreActivity`**: Activity for displaying persistent high scores.
- **`GameOverActivity`**: Final score display and post-game navigation.
- **`HowToPlay`**: Instruction screen.
- **`MultiplayerGame`**: (Experimental) Bluetooth/Local multiplayer mode activity.

### Game Logic
- **`GameViewModel`**: Manages game state, countdown timer (ms), scoring, and tile selection logic.
- **`TileGenerator`**: Generates random tiles for the game board based on English frequency distributions.
- **`Dictionary`**: Loads and validates words against the `word_list.txt` file.
- **`Trie`**: Efficient prefix searching and dictionary data structure.
- **`Word`**: Data model representing a word entry and its associated score.

### Data & Resources
- **`AppDatabase`**, **`HiScoreDao`**, **`HiScore`**: Room database components for persisting high scores.
- **`res/raw/word_list.txt`**: Raw text version of the dictionary.
- **`res/values-*/strings.xml`**: Localized strings for English, Irish, Japanese, Spanish, and German.
- **`distribution/whatsnew/`**: Release notes for the Google Play Store.

## CI/CD and Release

The project features a fully automated pipeline in `.github/workflows/android.yml`:
- **Build & Test:** Runs unit tests and builds the App Bundle (AAB).
- **Signing & Artifacts:** Release AAB is signed and uploaded as an artifact ONLY for tagged releases (`v*`).
- **Deployment:** Automatically pushes tagged releases (`v*`) to the Google Play **Internal Track** in `draft` status.

### Release Mandate
- **Always Bump Version:** Before pushing a new tag (`v*`), you MUST increment both `versionCode` and `versionName` in `app/build.gradle.kts`. Failure to do so will cause the Google Play deployment step to fail.

## Building and Running

This project uses the Gradle Build System (Kotlin DSL).

### Prerequisites
- Android SDK with API 35 platform.
- Java 17+.

### Build Commands
- **Build Debug APK:**
  ```bash
  ./gradlew assembleDebug
  ```
- **Build Release Bundle (AAB):**
  ```bash
  ./gradlew bundleRelease
  ```
- **Run Unit Tests:**
  ```bash
  ./gradlew test
  ```

## Development Conventions

- **Testing:** ALWAYS run unit tests (`./gradlew test`) before committing any code changes to ensure no regressions are introduced.
- **UI/UX:** Primarily Jetpack Compose-based UI with Material 3 components.
- **Localization:** All UI strings must be stored in `strings.xml` to support the multilingual setup.
- **Navigation:** Uses Intent-based Activity navigation for main screens, with Compose for layout and internal state.
- **Concurrency:** Kotlin Coroutines for asynchronous tasks (e.g., dictionary loading, DB operations).

## Troubleshooting

- **SDK Warning:** The project uses `compileSdk 35` and is compatible with AGP 8.8.2 and Gradle 8.10.2.
- **Google Play Conflict:** Ensure the `versionCode` in `app/build.gradle.kts` is incremented before every release push.
- **Signing:** Keystore and API secrets are stored in GitHub Secrets; local builds use a placeholder if those environment variables are missing.
