# Word-Formed

A modern, Scrabble-inspired mobile word game for Android. Form words, score points, and challenge yourself in a fast-paced tile-based environment.

[![Android CI/CD](https://github.com/Irishsmurf/Word-Formed/actions/workflows/android.yml/badge.svg)](https://github.com/Irishsmurf/Word-Formed/actions/workflows/android.yml)

## 🎮 Features

- **Dynamic Tile Generation:** Letters generated based on English frequency distributions.
- **Multilingual Support:** Fully localized in **English**, **Irish (Gaeilge)**, **Japanese**, **Spanish**, and **German**.
- **Real-time Scoring:** Instant word validation and score calculation.
- **Persistent High Scores:** Local storage using Room Database.
- **Modern UI:** Built entirely with **Jetpack Compose** and Material 3.

## 🛠 Tech Stack

- **Language:** Kotlin 1.9.22
- **UI Framework:** Jetpack Compose (Material 3)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Database:** Room Persistence Library
- **Build System:** Gradle 8.10.2 (Kotlin DSL)
- **AGP Version:** 8.8.2
- **Target SDK:** 34 (Android 14) / Compile SDK: 35 (Android 15)

## 📂 Documentation

- [Architecture Overview](docs/ARCHITECTURE.md) - Deep dive into the MVVM structure and logic.
- [Release Process](docs/RELEASE_PROCESS.md) - Automated CI/CD and Play Store deployment guide.
- [Manual Release Playbook](docs/playbooks/MANUAL_RELEASE.md) - Step-by-step guide for manual releases.
- [Testing Strategy](docs/TESTING.md) - Unit testing and validation procedures.
- [Project Roadmap](docs/ROADMAP.md) - Future features and planned improvements.

## 🚀 Getting Started

### Prerequisites

- Android Studio Ladybug (2024.2.1) or newer.
- JDK 17.

### Building from Source

```bash
# Clone the repository
git clone https://github.com/Irishsmurf/Word-Formed.git

# Build the debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
