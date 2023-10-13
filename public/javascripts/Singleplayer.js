import {Chess} from "./chess.js";
import {ChessBoardBuilder} from "./ChessBoardBuilder.js"

function update() {
    if (chess.turn() === 'b')
        setTimeout(doAIMove, 2000)
}



const stockfish = new Worker("assets/javascripts/stockfish.js")
let chess = new Chess()
let chessBoardBuilder = new ChessBoardBuilder(chess, 'w', update)
let chessBoard = chessBoardBuilder.createChessBoard(document.querySelector("#chessboard2"))

function doAIMove(){
    stockfish.postMessage('position fen ' + chess.fen());
    stockfish.postMessage("go depth 1")
}

stockfish.onmessage = function(event) {
    const message = event.data || event.data.data;
    if (message.includes('bestmove')) {
        const move = message.split(' ')[1];
        if (move.length === 4){
            chessBoard.moveFunction(move.substring(0, 4))
        }
        else {
            chessBoard.moveFunction(move.substring(0, 4), move.substring(4,5))
        }
    }
};

document.getElementById("newGameButton").addEventListener('click', () => {
    chessBoard.newGameFunction()
})

document.getElementById("resignButton").addEventListener('click', () => {
    chessBoardBuilder.gameOver()
})