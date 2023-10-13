import {Chess} from "./chess.js";
import {ChessBoardBuilder} from "./ChessBoardBuilder.js"

let chess = new Chess()
let chessBoardBuilder = new ChessBoardBuilder(chess)
let chessBoard = chessBoardBuilder.createChessBoard(document.querySelector("#chessboard2"), true)

document.getElementById("newGameButton").addEventListener('click', () => {
    chessBoard.newGameFunction()
})

document.getElementById("resignButton").addEventListener('click', () => {
    chessBoardBuilder.gameOver()
})