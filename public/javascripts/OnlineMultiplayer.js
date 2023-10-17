import {ConnectionHandler} from "./ConnectionHandler.js";
import {Chess} from "./chess.js";
import {ChessBoardBuilder} from "./ChessBoardBuilder.js";
import {Chessboard} from "./Chessboard.js";

let playerID = document.getElementById('playerID').innerText
let gameID = document.getElementById('gameID').innerText
document.cookie = "playerID=" + playerID
let connectionHandler = new ConnectionHandler()

let update = (move) => {
    connectionHandler.sendMove(playerID, move)
    getMove()
}

let chess = new Chess()
let chessboardBuilder = new ChessBoardBuilder(chess,'w', update)
let chessboard = chessboardBuilder.createChessBoard(document.querySelector("#Chessboard"),true)

function getMove(){
    connectionHandler.update(playerID, proofMove)
}

function proofMove(move){
    console.log("it works")
    getMove()
}