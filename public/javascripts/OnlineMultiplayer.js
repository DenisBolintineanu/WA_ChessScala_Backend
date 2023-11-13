import {ConnectionHandler} from "./ConnectionHandler.js";
import {Chess} from "./chess.js";
import {ChessBoardBuilder} from "./ChessBoardBuilder.js";
import {Chessboard} from "./Chessboard.js";

let playerID = document.getElementById('playerID').innerText
let gameID = document.getElementById('gameID').innerText
document.cookie = "playerID=" + playerID
let connectionHandler = new ConnectionHandler()

function update(move) {
    connectionHandler.sendMove(playerID, chess.history({verbose: true}).pop().lan)
    getMove()
}

let chess = new Chess()
let chessboardBuilder
if (!checkIfParameterExists()){
    chessboardBuilder = new ChessBoardBuilder(chess,'w', update)
    document.cookie += "; color=w"
}
else {
    chessboardBuilder = new ChessBoardBuilder(chess,'b', update)
    history.replaceState(null, "", ".")
    document.cookie += "; color=b"
    getMove()
}

let chessBoard = chessboardBuilder.createChessBoard(document.querySelector("#Chessboard"),true)

if (checkIfParameterExists()){
    getMove()
}

function getMove(){
    connectionHandler.update(playerID, proofMove)
}

function proofMove(move){
    if (move.length  === 4) {
        chessBoard.asciiMove(move.substring(0, 4), null, false)
    }
    else {
        chessBoard.asciiMove(move.substring(0, 4), move.substring(4,5), false)
    }
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