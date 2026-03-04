# Word-Formed Roadmap

The future development of Word-Formed focuses on enhancing the user experience, expanding game modes, and improving code quality.

## Phase 1: Gameplay Enhancements (Ongoing)
- **Interactive Animations:** Improve tile movement and score feedback.
- **Improved Sound Effects:** Add more immersive audio feedback for word validation and game events.
- **Dynamic Themes:** Introduce light/dark mode support and custom color palettes.

## Phase 2: Game Modes
- **Multiplayer Mode:** Complete the implementation of Bluetooth/Local multiplayer support.
- **Timed vs. Endless Mode:** Add game variants to allow players to choose between timed challenges and more relaxed play.
- **Global Leaderboards:** Integrate a centralized backend or Google Play Games Services for cross-platform competition.

## Phase 3: Technical Refactoring
- **Hilt/Dagger Dependency Injection:** Introduce a modern DI framework to simplify component management.
- **Jetpack Navigation:** Migrate from Intent-based Activity navigation to a unified Compose-based navigation structure.
- **Extended Test Suite:** Comprehensive Compose UI and instrumentation tests.

## Phase 4: Platform Expansion
- **Tablet Optimization:** Adapt layouts for larger screens.
- **iOS (Kotlin Multiplatform):** Investigate using KMP to share core game logic (Trie, Dictionary, GameViewModel) with an iOS version.
