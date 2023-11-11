import './node_modules/jquery/dist/jquery.js'

export class ConnectionHandler {

    sendMove(playerID, move) {
        $.post("/do_move", {PlayerID: playerID, move: move})
    }

    update(playerID, handler){
        this.startPolling(playerID,handler)
    }

    startPolling(playerID, handler) {
        const pollingInterval = 1000;
        let intervalId = setInterval(() => {
            $.post("/get_move", { PlayerID: playerID })
                .done((result) => {
                    if (result && $.trim(result) !== "") {
                        clearInterval(intervalId);
                        handler(result.move);
                    }
                })
                .fail(() => {
                    clearInterval(intervalId);
                    this.startPolling(playerID, handler);
                });
        }, pollingInterval);
    }
}