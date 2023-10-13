import {Chess} from "./chess.js";
import {ChessBoardBuilder} from "./ChessBoardBuilder.js"

function update() {
    if (chess.turn() !== team)
        setTimeout(doAIMove, 1500)
}

const stockfish = new Worker("assets/javascripts/stockfish.js")
let chess = new Chess()
let chessBoardBuilder = new ChessBoardBuilder(chess, 'w', update)
let chessBoard = chessBoardBuilder.createChessBoard(document.querySelector("#chessboard2"))
let team

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
    popup.style.display = 'block'
})

document.getElementById("resignButton").addEventListener('click', () => {
    chessBoardBuilder.gameOver()
})

let popup = document.createElement("div")
popup.classList.add("popup")
document.body.appendChild(popup)
let selectTeam = document.createElement('div')
selectTeam.classList.add('selectTeam')
popup.appendChild(selectTeam)
let buttonWhite = document.createElement('div')
let buttonBlack = document.createElement('div')
buttonWhite.classList.add("btn")
buttonBlack.classList.add("btn")
buttonWhite.innerHTML = "White"
buttonBlack.innerHTML = "Black"
selectTeam.appendChild(buttonWhite)
selectTeam.appendChild(buttonBlack)

buttonWhite.addEventListener('click', () => {
    chessBoardBuilder = new ChessBoardBuilder(chess, 'w', update)
    chessBoard = chessBoardBuilder.createChessBoard(document.querySelector("#chessboard2"), true)
    team = 'w'
    popup.style.display = 'none'
})

buttonBlack.addEventListener('click', () => {
    chessBoardBuilder = new ChessBoardBuilder(chess, 'b', update)
    chessBoard = chessBoardBuilder.createChessBoard(document.querySelector("#chessboard2"), true)
    team = 'b'
    popup.style.display = 'none'
    setTimeout(doAIMove, 500)
})