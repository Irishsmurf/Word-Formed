# Word-Formed

Word-Formed is a Scrabble-inspired word game for Android. Test your vocabulary and speed by forming words from a pool of letter tiles before the timer runs out!

## Features

- **Dynamic Gameplay:** Drag and drop letter tiles to form words.
- **Scoring:** Points awarded based on word length and letter rarity.
- **High Scores:** Track your best performances with a local persistent leaderboard.
- **Timer:** Race against the clock to maximize your score.
- **Dictionary Validation:** Real-time word verification using a built-in dictionary.

## Technical Details

- **Platform:** Android 7.0+ (API Level 24+)
- **Language:** 100% Kotlin
- **Build System:** Gradle (Kotlin DSL)
- **UI:** Jetpack Compose & Material 3
- **Architecture:** MVVM (Model-View-ViewModel)
- **Storage:** Room Database for persistent high scores
- **Concurrency:** Kotlin Coroutines & Flow

## Setup & Build

1. **Clone the repository:**
   ```bash
   git clone <repo-url>
   cd Word-Formed
   ```

2. **Open with Android Studio:**
   Import the project into Android Studio (Hedgehog or newer recommended).

3. **Build from CLI:**
   ```bash
   ./gradlew assembleDebug
   ```

4. **Run Tests:**
   ```bash
   ./gradlew test
   ```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details (or your preferred license).
