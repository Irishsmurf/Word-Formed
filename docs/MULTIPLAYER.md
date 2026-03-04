# Multiplayer Documentation

This document outlines the architecture, current implementation status, and configuration requirements for the Word-Formed Multiplayer system.

## Architecture

Word-Formed uses a **Client-Server** architecture powered by **gRPC** and **Protocol Buffers (Protobuf)**. This allows for efficient, strongly-typed, and bi-directional real-time communication.

### Core Components

1.  **gRPC Service (`GameService`):** Defined in `app/src/main/proto/game.proto`.
    *   `JoinLobby`: A bi-directional stream. The client sends player info; the server streams back real-time events (player joins, game start, state updates, game over).
    *   `SubmitWord`: A unary call for authoritative word validation and scoring on the server.
    *   `GetLeaderboard`: A unary call to fetch final standings.
2.  **Android Client (`GameClient.kt`):** A wrapper around the gRPC stub using Kotlin Coroutines and Flows.
3.  **State Management (`MultiplayerViewModel.kt`):** Uses `StateFlow` to provide a reactive UI state based on the gRPC stream.
4.  **Reference Server (`server/server.py`):** A Python-based prototype server for development and testing.

## Current Implementation Status (v1.2.3)

| Feature | Status | Description |
| :--- | :--- | :--- |
| **Protobuf Definitions** | ✅ Done | Core messages and service contracts defined. |
| **Build Configuration** | ✅ Done | gRPC/Protobuf plugins and dependencies integrated. |
| **gRPC Client** | ✅ Done | Coroutine-based client implemented. |
| **Multiplayer ViewModel** | ✅ Done | Logic for handling lobby events and state flows. |
| **Prototype Server** | ✅ Done | Basic Python server with mock lobby/game logic. |
| **Lobby UI** | ⏳ Pending | Compose-based screen for joining and waiting. |
| **Multiplayer Game UI** | ⏳ Pending | Adaptation of SinglePlayer UI for network sync. |
| **Auth/Identity** | ⏳ Pending | Unique player ID persistence and session mgmt. |

## Configuration

### Android Client

The client is configured to use **gRPC OkHttp** with **Protobuf Lite** for optimized performance on mobile devices.

*   **Host/Port:** Currently defaults to `localhost:50051` in development.
*   **Security:** Currently uses `usePlaintext()` for local development. **Must be updated to use TLS/SSL for production.**

### Python Server

To run the prototype server for testing:

1.  **Prerequisites:** Python 3.10+, `pip install grpcio grpcio-tools`.
2.  **Generate Stubs:**
    ```bash
    cd server
    python3 -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. game.proto
    ```
3.  **Run Server:**
    ```bash
    python3 server.py
    ```

## Roadmap & Next Steps

### 1. UI Integration
- Implement `LobbyScreen.kt` to handle player name entry and display the waiting list.
- Implement `MultiplayerGameScreen.kt` which subscribes to `gameState` from the ViewModel.

### 2. Server Refinement
- Implement a `LobbyManager` to handle multiple concurrent games.
- Add authoritative `Dictionary` validation on the server side (Python implementation).
- Implement game timer synchronization to ensure all clients end at the same time.

### 3. Network Resilience
- Implement reconnection logic in `GameClient`.
- Add UI feedback for "Connection Lost" or "Server Offline" states.

### 4. Production Readiness
- Configure ProGuard rules for gRPC/Protobuf.
- Implement secure communication (TLS).
- Deploy the Python server to a cloud provider (e.g., GCP, AWS, or Heroku).
