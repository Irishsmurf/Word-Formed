import grpc
import time
import uuid
from concurrent import futures
import game_pb2
import game_pb2_grpc

class GameService(game_pb2_grpc.GameServiceServicer):
    def __init__(self):
        self.players = {}  # player_id -> Player object
        self.lobbies = {}  # game_id -> GameState object
        self.player_streams = {} # player_id -> list of response streams

    def JoinLobby(self, request_iterator, context):
        player_id = str(uuid.uuid4())
        
        for request in request_iterator:
            player_name = request.player_name
            player = game_pb2.Player(id=player_id, name=player_name, score=0)
            self.players[player_id] = player
            
            print(f"Player {player_name} ({player_id}) joined.")
            
            # Broadcast "Player Joined" event to others (simplified)
            yield game_pb2.JoinLobbyResponse(player_joined=player)
            
            # Simulate game starting after a short delay
            time.sleep(1)
            yield game_pb2.JoinLobbyResponse(game_started=True)
            
            # Keep the connection alive for more updates
            while True:
                time.sleep(5)
                # Send mock state updates
                state = game_pb2.GameState(
                    players=list(self.players.values()),
                    tiles=[game_pb2.Tile(letter="A", value=1), game_pb2.Tile(letter="B", value=3)],
                    timer_ms=60000,
                    is_started=True
                )
                yield game_pb2.JoinLobbyResponse(state_update=state)

    def SubmitWord(self, request, context):
        player_id = request.player_id
        word = request.word
        print(f"Word submitted: {word} by {player_id}")
        
        # Simple mock validation: words longer than 3 are valid
        if len(word) >= 3:
            score = len(word) * 10
            if player_id in self.players:
                self.players[player_id].score += score
            return game_pb2.SubmitWordResponse(is_valid=True, score_added=score)
        else:
            return game_pb2.SubmitWordResponse(is_valid=False, score_added=0, error_message="Word too short")

    def GetLeaderboard(self, request, context):
        sorted_players = sorted(self.players.values(), key=lambda p: p.score, reverse=True)
        return game_pb2.GetLeaderboardResponse(rankings=sorted_players)

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    game_pb2_grpc.add_GameServiceServicer_to_server(GameService(), server)
    server.add_insecure_port('[::]:50051')
    print("Server started on port 50051...")
    server.start()
    server.wait_for_termination()

if __name__ == '__main__':
    serve()
