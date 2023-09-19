// chess.js

import { Chess } from "./chess.js";
const chess = new Chess()
const stockfish = new Worker("assets/javascripts/stockfish.js")
let team



stockfish.onmessage = function(event) {
    var message = event.data || event.data.data;
    if (message.includes('bestmove')) {
        var move = message.split(' ')[1];
        if (move.length == 4){
            doMove(move.substring(0, 2), move.substring(2, 4))
        }
        else {
            doMove(move.substring(0, 2), move.substring(2, 5)) 
        }
    }
};


const notifySound = document.getElementById("notificationSound")
const moveSound = document.getElementById("moveSound")
const captureSound = document.getElementById("captureSound")
//const fields = document.querySelectorAll('.chess-field');
let fields = document.querySelectorAll('#chessboard > .chess-field')
const moves = document.querySelector('.moves-field');
const selectPopup = document.getElementById('selectTeam')
let firstClick = null;

buildBoard()

document.addEventListener('DOMContentLoaded', function() {
    let fields = document.querySelectorAll('.chess-field')
    fields.forEach(field => {
        field.addEventListener('click', function() {
            try{
                if (chess.get(field.id).color == chess.turn() && field != firstClick){
                    firstClick.querySelector('.figure').classList.remove('selected')  
                    firstClick = null
                } 
            } catch (exception) {}

            if (!firstClick) {
                if (!chess.get(field.id)) return
                if (chess.get(field.id).color != chess.turn()) return
                firstClick = field;
                field.querySelector('.figure').classList.add('selected');
            } else {
                firstClick.querySelector('.figure').classList.remove('selected')
                if (firstClick != field)
                    doMove(firstClick.id, field.id);
                firstClick = null;
            }
            
        });
    });
});


function doMove(from, to) {
    const move = (from + to).toLowerCase(); 
    try{
        const captured = chess.get(to)
        const success = chess.move(move)
        if (success){
            buildBoard()
            displayEnd()
            if (captured)
                captureSound.play()
            else
                moveSound.play()
        }
    }
    catch(exception){}
    if (chess.turn() != team)
        setTimeout(doAIMove, 1000)
}

function doAIMove(){
    stockfish.postMessage('position fen ' + chess.fen());
    stockfish.postMessage("go depth 1")
}

function sendMove(from, to) {
    const move = (from + to).toLowerCase();

    fetch('/game/id=' + playerID, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'type=move&move=' + move
    })
    .then(response => {
        if (response.ok) {
            // Wenn der Server eine erfolgreiche Antwort sendet, laden Sie die Seite neu
            location.reload();
        } else {
            console.error('Error sending move:', move);
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function buildBoard() {
    fields.forEach(field => {
        const piece = chess.get(field.id.toLowerCase())
        if (piece) {
            const type = piece.type
            const color = piece.color
            const src = getImage(type, color)
            field.innerHTML = "<img class=\"figure\" src=\"" + src + "\" />"
            if (chess.isCheck() && type == 'k' && color == chess.turn()){
                field.innerHTML = "<img class=\"figure attacked\" src=\"" + src + "\" />"
            }
        }
        else {
            field.innerHTML = ""
        }
    })
}


function getImage(piece, color) {
    
    let ColorMap = {
        "w" : "white",
        "b" : "black"
    }

    let PieceMap = {
        "r": "Rook",
        "n": "Knight",
        "b": "Bishop",
        "k": "King",
        "q": "Queen",
        "p": "Pawn"
    }
    return "assets/images/chesspieces/" + ColorMap[color] + PieceMap[piece] + ".png"
}

function displayEnd(){
    if (chess.isCheckmate()){
        const looser = chess.turn
        const winner = looser === 'w' ? 'b' : 'w'
        moves.innerHTML = "Checkmate! " + (winner === 'w'? "White wins!" : "Black wins!")
        notifySound.play()
    }
}

function startAsWhite(){
    team = 'w'
    selectPopup.style.display = 'none'
    notifySound.play() 
}

function startAsBlack(){
    team = 'b'
    selectPopup.style.display = 'none'
    fields = document.querySelectorAll('#chessboardInverse > .chess-field')
    document.getElementById('chessboard').style.display = 'none'
    document.getElementById('chessboardInverse').style.display = 'grid'
    buildBoard()
    notifySound.play()
    doAIMove()
}

window.startAsWhite = startAsWhite
window.startAsBlack = startAsBlack