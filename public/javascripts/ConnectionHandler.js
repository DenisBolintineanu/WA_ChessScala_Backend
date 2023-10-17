import './node_modules/jquery/dist/jquery.js'

export class ConnectionHandler {

    sendMove(playerID, move) {
        $.post("/do_move", {PlayerID: playerID, move: move})
    }

    update(playerID, handler){
        $.post("/get_move", {PlayerID: playerID})
            .done((result) => {
                setTimeout(() => { handler(result) } ,750)
            })
    }
}