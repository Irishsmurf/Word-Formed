package com.paddez.wordformed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paddez.wordformed.proto.GameState
import com.paddez.wordformed.proto.JoinLobbyRequest
import com.paddez.wordformed.proto.JoinLobbyResponse
import com.paddez.wordformed.proto.Player
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MultiplayerViewModel(private val client: GameClient) : ViewModel() {
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    private val _isGameStarted = MutableStateFlow(false)
    val isGameStarted: StateFlow<Boolean> = _isGameStarted.asStateFlow()

    private val joinRequests = MutableSharedFlow<JoinLobbyRequest>()

    init {
        viewModelScope.launch {
            client.joinLobby(joinRequests)
                .onEach { response ->
                    handleEvent(response)
                }
                .catch { e ->
                    // Handle error (e.g., connection lost)
                    println("Multiplayer error: ${e.message}")
                }
                .launchIn(this)
        }
    }

    fun join(playerName: String) {
        viewModelScope.launch {
            joinRequests.emit(JoinLobbyRequest.newBuilder().setPlayerName(playerName).build())
        }
    }

    private fun handleEvent(response: JoinLobbyResponse) {
        when (response.eventCase) {
            JoinLobbyResponse.EventCase.PLAYER_JOINED -> {
                _players.value = _players.value + response.playerJoined
            }
            JoinLobbyResponse.EventCase.GAME_STARTED -> {
                _isGameStarted.value = response.gameStarted
            }
            JoinLobbyResponse.EventCase.STATE_UPDATE -> {
                _gameState.value = response.stateUpdate
                _players.value = response.stateUpdate.playersList
            }
            JoinLobbyResponse.EventCase.GAME_OVER -> {
                // Handle game over (e.g., navigate to leaderboard)
                println("Game over: ${response.gameOver}")
            }
            else -> {}
        }
    }

    override fun onCleared() {
        super.onCleared()
        client.close()
    }
}
