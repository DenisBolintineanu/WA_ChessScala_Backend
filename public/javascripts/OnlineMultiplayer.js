import {ConnectionHandler} from "./ConnectionHandler.js";
import {Chess} from "./chess.js";
import {ChessBoardBuilder} from "./ChessBoardBuilder.js";
import {Chessboard} from "./Chessboard.js";

let connectionHandler = new ConnectionHandler()
let chess = new Chess()
let chessBoard
let playerID
let gameID
let color


if (connectionHandler.getCookie("PlayerID") && connectionHandler.getCookie("GameID") && connectionHandler.getCookie("color"))
    use_existing_game().then(() => {
        console.log(color)
        if (color !== chess.turn())
            getMove()
    })
else {
    start_new_game().then()
}

async function start_new_game(){
    const player_info = await connectionHandler.requestPlayerId()
    playerID = player_info.playerId
    gameID = player_info.gameId
    color = player_info.color
    let chessboardBuilder = new ChessBoardBuilder(chess,'w', update)
    document.cookie += "; color=w"
    chessBoard = chessboardBuilder.createChessBoard(document.querySelector("#Chessboard"),true)
}

async function use_existing_game(){
    const game_info = await connectionHandler.requestGameSession()
    playerID = connectionHandler.getCookie("PlayerID")
    gameID = connectionHandler.getCookie("GameID")
    color = connectionHandler.getCookie("color")
    if (game_info.Success === false){
        start_new_game()
        return
    }
    chess.load(game_info.FEN)
    let chessboardBuilder = new ChessBoardBuilder(chess, color, update)
    chessBoard = chessboardBuilder.createChessBoard(document.querySelector("#Chessboard"),true)
}
function update(move) {
    connectionHandler.sendMove(playerID, chess.history({verbose: true}).pop().lan, chess.fen())
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

function fetchTranslation(key) {
    return fetch('/api/getMessage/' + key)
        .then(response => response.text())
        .catch(error => {
            console.error('Error fetching translation:', error);
            return '';
        });
}

let gameIdClipboard = document.getElementById('game-id')
gameIdClipboard.style.display = "block"
gameIdClipboard.addEventListener('click', function() {
    copyGameID();
});

function copyGameID() {
    const path_for_invite_link = window.location.href + "/join_game/"
    fetchTranslation('gameID.copied').then(msg => {
        navigator.clipboard.writeText(path_for_invite_link +gameID).then(() => {
            alert(msg)
        });
    });
}