import { Chess } from "./chess.js";

const chess = new Chess();
const stockfish = new Worker("assets/javascripts/stockfish.js");
const sounds = {
    notify: document.getElementById("notificationSound"),
    move: document.getElementById("moveSound"),
    capture: document.getElementById("captureSound")
};
const moves = document.querySelector('.moves-field');
const selectPopup = document.getElementById('selectTeam');
let fields = document.querySelectorAll('#chessboard > .chess-field');
let firstClick = null;
let team;

stockfish.onmessage = handleStockfishMessage;

document.addEventListener('DOMContentLoaded', initializeBoard);

function handleStockfishMessage(event) {
    const message = event.data || event.data.data;
    if (message.includes('bestmove')) {
        const move = extractMoveFromMessage(message);
        doMove(move.from, move.to);
    }
}

function extractMoveFromMessage(message) {
    const move = message.split(' ')[1];
    return {
        from: move.substring(0, 2),
        to: move.substring(2, 4)
    };
}

function initializeBoard() {
    fields.forEach(addChessFieldListener);
}

function addChessFieldListener(field) {
    field.addEventListener('click', function() {
        handleFieldClick(field);
    });
}

function handleFieldClick(field) {
    if (isSelectable(field)) {
        if (firstClick) {
            doMove(firstClick.id, field.id);
            deselectField(firstClick);
            firstClick = null;
        } else {
            selectField(field);
            firstClick = field;
        }
    }
}

function isSelectable(field) {
    const piece = chess.get(field.id);
    return piece && piece.color === chess.turn();
}

function selectField(field) {
    field.querySelector('.figure').classList.add('selected');
}

function deselectField(field) {
    field.querySelector('.figure').classList.remove('selected');
}

function doMove(from, to) {
    if (team === 'b' || team === 'none') return;

    const move = (from + to).toLowerCase();
    const captured = chess.get(to);
    const success = chess.move(move);

    if (success) {
        buildBoard();
        displayEnd();
        playSound(captured ? sounds.capture : sounds.move);
    }

    if (chess.turn() !== team) setTimeout(doAIMove, 1000);
}

function doAIMove() {
    stockfish.postMessage('position fen ' + chess.fen());
    stockfish.postMessage("go depth 1");
}

function buildBoard() {
    fields.forEach(field => {
        const piece = chess.get(field.id.toLowerCase());
        updateFieldWithPiece(field, piece);
    });
}

function updateFieldWithPiece(field, piece) {
    if (piece) {
        const src = getImage(piece.type, piece.color);
        field.innerHTML = `<img class="figure" src="${src}" />`;
        if (chess.isCheck() && piece.type === 'k' && piece.color === chess.turn()) {
            field.innerHTML = `<img class="figure attacked" src="${src}" />`;
        }
    } else {
        field.innerHTML = "";
    }
}

function getImage(piece, color) {
    const ColorMap = {
        "w": "white",
        "b": "black"
    };

    const PieceMap = {
        "r": "Rook",
        "n": "Knight",
        "b": "Bishop",
        "k": "King",
        "q": "Queen",
        "p": "Pawn"
    };

    return `assets/images/chesspieces/${ColorMap[color]}${PieceMap[piece]}.png`;
}

function displayEnd() {
    if (chess.isCheckmate()) {
        const looser = chess.turn;
        const winner = looser === 'w' ? 'b' : 'w';
        moves.innerHTML = `Checkmate! ${winner === 'w' ? "White wins!" : "Black wins!"}`;
        playSound(sounds.notify);
    }
}

function playSound(sound) {
    sound.play();
}

function startAsWhite() {
    team = 'w';
    hidePopup();
    playSound(sounds.notify);
}

function startAsBlack() {
    team = 'b';
    hidePopup();
    fields = document.querySelectorAll('#chessboardInverse > .chess-field');
    document.getElementById('chessboard').style.display = 'none';
    document.getElementById('chessboardInverse').style.display = 'grid';
    buildBoard();
    playSound(sounds.notify);
    doAIMove();
}

function hidePopup() {
    selectPopup.style.display = 'none';
}

window.startAsWhite = startAsWhite;
window.startAsBlack = startAsBlack;
buildBoard()
initializeBoard()