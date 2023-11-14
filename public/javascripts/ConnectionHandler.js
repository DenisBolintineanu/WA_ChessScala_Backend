import './node_modules/jquery/dist/jquery.js'

export class ConnectionHandler {

    sendMove(playerID, move, fen) {
        $.post("/online_multiplayer/do_move", {PlayerID: playerID, UCI: move, FEN: fen})
    }

    async requestPlayerId() {
        const result = await $.get("/online_multiplayer/new_game")
        document.cookie = "PlayerID=" + result.PlayerID + "; path=/";
        document.cookie = "GameID=" + result.GameID + "; path=/";
        document.cookie = "color=w" + "; path=/";
        return {
            playerId: result.PlayerID,
            gameId: result.GameID,
            color: 'w'
        };
    }

    async requestGameSession(){
        const playerID = this.getCookie("PlayerID")
        return await $.post("online_multiplayer/join_game", {PlayerID: playerID})
    }

    update(playerID, handler){
        this.startPolling(playerID,handler)
    }

    startPolling(playerID, handler) {
        const pollingInterval = 1000;
        let intervalId = setInterval(() => {
            $.post("/online_multiplayer/get_move", { PlayerID: playerID })
                .done((result) => {
                    if (result && $.trim(result) !== "") {
                        clearInterval(intervalId);
                        handler(result.UCI);
                    }
                })
                .fail(() => {
                    clearInterval(intervalId);
                    this.startPolling(playerID, handler);
                });
        }, pollingInterval);
    }

    getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null
    }
}