import {ConnectionHandler} from "./ConnectionHandler.js";
import {Chess} from "./chess.js";
import {ChessBoardBuilder} from "./ChessBoardBuilder.js";
import {Chessboard} from "./Chessboard.js";

let playerID = document.getElementById('playerID').innerText
let gameID = document.getElementById('gameID').innerText
document.cookie = "playerID=" + playerID
let connectionHandler = new ConnectionHandler()

function update(move) {
    connectionHandler.sendMove(playerID, move)
    getMove()
}

let chess = new Chess()
let chessboardBuilder
if (!checkIfParameterExists()){
    chessboardBuilder = new ChessBoardBuilder(chess,'w', update)
}
else {
    chessboardBuilder = new ChessBoardBuilder(chess,'b', update)
}

let chessBoard = chessboardBuilder.createChessBoard(document.querySelector("#Chessboard"),true)

if (checkIfParameterExists()){
    getMove()
}

function getMove(){
    connectionHandler.update(playerID, proofMove)
}

function proofMove(move){
    chessBoard.asciiMove(move.substring(0, 4), null, false)
}

function checkIfParameterExists() {
    const pathSegments = window.location.pathname.split('/').filter(segment => segment.trim() !== '');
    const multiplayerIndex = pathSegments.indexOf('online_multiplayer');
    if (multiplayerIndex !== -1 && pathSegments.length > multiplayerIndex + 1) {
        return pathSegments[multiplayerIndex + 1];
    } else {
        return null;
    }
}