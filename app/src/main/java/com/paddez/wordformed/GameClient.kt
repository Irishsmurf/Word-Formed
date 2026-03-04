package com.paddez.wordformed

import com.paddez.wordformed.proto.GameServiceGrpcKt
import com.paddez.wordformed.proto.JoinLobbyRequest
import com.paddez.wordformed.proto.JoinLobbyResponse
import com.paddez.wordformed.proto.SubmitWordRequest
import com.paddez.wordformed.proto.SubmitWordResponse
import com.paddez.wordformed.proto.GetLeaderboardRequest
import com.paddez.wordformed.proto.GetLeaderboardResponse
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.Flow
import java.io.Closeable
import java.util.concurrent.TimeUnit

class GameClient(private val channel: ManagedChannel) : Closeable {
    private val stub: GameServiceGrpcKt.GameServiceCoroutineStub = GameServiceGrpcKt.GameServiceCoroutineStub(channel)

    fun joinLobby(requests: Flow<JoinLobbyRequest>): Flow<JoinLobbyResponse> {
        return stub.joinLobby(requests)
    }

    suspend fun submitWord(playerId: String, word: String): SubmitWordResponse {
        val request = SubmitWordRequest.newBuilder()
            .setPlayerId(playerId)
            .setWord(word)
            .build()
        return stub.submitWord(request)
    }

    suspend fun getLeaderboard(gameId: String): GetLeaderboardResponse {
        val request = GetLeaderboardRequest.newBuilder()
            .setGameId(gameId)
            .build()
        return stub.getLeaderboard(request)
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    companion object {
        fun create(host: String, port: Int): GameClient {
            val channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext() // For development; use secure channel for production
                .build()
            return GameClient(channel)
        }
    }
}
