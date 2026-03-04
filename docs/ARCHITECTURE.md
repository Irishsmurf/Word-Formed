# Word-Formed Architecture

This project follows the modern Android architectural patterns: **MVVM (Model-View-ViewModel)** with a focus on UI-State separation using **Jetpack Compose**.

## Core Layers

### 1. Presentation Layer (UI & ViewModel)
- **Jetpack Compose:** Defines the entire UI in a declarative manner.
- **`GameViewModel`:** Orchestrates the game state, manages the countdown timer, and handles tile movements. It uses `StateFlow` and `mutableStateOf` to propagate changes to the UI.
- **`HiScoreViewModel`:** Manages the communication between the UI and the Room database for high scores.

### 2. Logic Layer
- **`Dictionary`:** Handles the asynchronous loading of the word list (from `res/raw/word_list.txt`) into memory.
- **`Trie`:** (Data Structure) Used for efficient prefix searches and fast word validation.
- **`TileGenerator`:** Manages the "Bag" of tiles, ensuring a balanced distribution of letters based on standard Scrabble frequencies.

### 3. Data Layer (Persistence)
- **Room Persistence Library:** Stores high scores locally in a SQLite database.
- **`HiScoreDao`:** Interface defining the database operations.
- **`AppDatabase`:** The main entry point for the Room database.

## Key Component Interaction

```text
[Compose UI] <-> [ViewModel] <-> [Repository/Logic] <-> [Data Sources (Room/Raw)]
```

- **User Actions:** (Dragging a tile, submitting a word) are sent from the UI to the ViewModel.
- **State Updates:** The ViewModel modifies its internal state, which triggers a recomposition of the Compose UI.
- **Validation:** When a word is submitted, the ViewModel queries the `Dictionary` (Logic) to determine if the word is valid.
- **Persistence:** If a game is over, the final score is saved to the `AppDatabase` via the `HiScoreViewModel`.
