# Word-Formed Testing Strategy

This project maintains a robust testing environment focusing on unit tests for core game logic and UI state management.

## 🧪 Unit Tests (`app/src/test/java`)

The unit tests are located in the `test` directory and focus on the following components:

- **`GameViewModelTest`:** Validates the game initialization, state updates, and score calculation logic.
- **`TileGeneratorTest`:** Ensures that the tile bag generates the expected frequencies and values.
- **`TrieTest`:** Tests the prefix searching and word validation functionality of the Trie data structure.

### Running Unit Tests

To execute the unit tests from the command line:

```bash
./gradlew test
```

## 📱 UI Tests (`app/src/androidTest/java`)

The UI tests use **Jetpack Compose Testing** and are located in the `androidTest` directory:

- **`MainMenuTest`:** Verifies the presence and functionality of menu buttons.
- **`GameScreenTest`:** Ensures HUD elements and game boxes are displayed correctly.

### Running UI Tests

UI tests require a connected Android device or an emulator. To run them:

```bash
./gradlew connectedAndroidTest
```

## 🛠️ Validation Framework

- **InstantTaskExecutorRule:** Used in tests to execute tasks synchronously.
- **MainDispatcherRule:** A custom JUnit Rule used to manage Kotlin Coroutines in a testing environment (swap `Dispatchers.Main` with a test dispatcher).

## 🚀 CI/CD Integration

The **GitHub Actions** pipeline (`android.yml`) runs the full test suite on every push and Pull Request. A successful test run is a prerequisite for generating a signed release bundle or deploying to the Google Play Store.

## 📝 Future Testing Roadmap

- **Compose UI Tests:** Add tests for UI interactions and component layout.
- **Room Database Tests:** Implement instrumentation tests to verify migrations and DAO operations.
- **End-to-End (E2E) Tests:** Integrate Espresso for full game-flow validation.
