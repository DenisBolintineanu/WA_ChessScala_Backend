import {Chess} from "./chess.js";
import {Chessboard} from "./Chessboard.js";
import {AsyncPromotionHandler} from "./AsyncPromotionHandler.js";

let chess = new Chess()
let chessboard = new Chessboard()
let promotionHandler = new AsyncPromotionHandler(chess)


chessboard.assign(document.getElementById("chessboard2"))
chessboard.createChessboard(false)
chessboard.highlightFunction = (field) => {
    return chess.moves({verbose: true, square: field})
}
chessboard.isAttackedFunction = () => { return chess.isCheck() }
chessboard.getKingFunction = () => {return getPositionsOfPiece({type: 'k', color: chess.turn()})}
chessboard.updateFunction = field => {return chess.get(field)}
chessboard.isSelectable = field => {return chess.get(field.id).color === chess.turn()}
chessboard.moveFunction = asyncMoveFunction
chessboard.update()
chessboard.initializeEventListener()
document.getElementById("undoButton").addEventListener('click', () => {
    chess.undo()
    chessboard.update()
});

let sounds = {
    move: document.getElementById("moveSound"),
    capture: document.getElementById("captureSound"),
    notification: document.getElementById("notificationSound")
};

function checkIfGameIsOver() {
    if (chess.isThreefoldRepetition()){
        gameOver("")
        return true
    }
    if (chess.isInsufficientMaterial()){
        gameOver("")
        return true
    }
    if (chess.isGameOver()){
        gameOver("")
        return true
    }
    return false
}

const getPositionsOfPiece = (piece) => {
    const positions = [];
    const board = chess.board();

    for (let row = 0; row < 8; row++) {
        for (let col = 0; col < 8; col++) {
            const square = board[row][col];
            if (square && square.type === piece.type && square.color === piece.color) {
                positions.push(String.fromCharCode('a'.charCodeAt(0) + col) + (8 - row));
            }
        }
    }

    return positions;
}

function asyncMoveFunction(move){
    let promotion = promotionHandler.doPromotion(move)
    if (promotion) {
        promotion.then(figure => {
            serialMoveFunction(move, figure)
        })
    }
    else {
        serialMoveFunction(move, null)
    }
}
function gameOver(text){
    chessboard.removeEventListener()
    sounds.notification.play()
    document.getElementById("resignButton").style.display = "none"
    document.getElementById("undoButton").style.display = "none"
    document.getElementById("newGameButton").style.display = "block"
}

function serialMoveFunction(move, promotion) {
    try {
        let result
        let isHit = chess.get(move.substring(2,4)) !== false
        if (promotion){
            result = chess.move({
                from: move.substring(0, 2),
                to: move.substring(2, 4),
                promotion: promotion})
        }
        else {
            result = chess.move({
                from: move.substring(0, 2),
                to: move.substring(2, 4)})
        }
        chessboard.update()
        let gameOver = checkIfGameIsOver()
        playSound(isHit, gameOver)
    } catch (exception) {
        chessboard.handleException(move)
    }
    function playSound(isHit, gameOver){
        if (gameOver){
            sounds.notification.play()
        }
        else if (isHit) {
            sounds.capture.play()
        }
        else {
            sounds.move.play()
        }
    }
}

document.getElementById('resignButton').addEventListener('click', () => {
    gameOver("resigned")
});

document.getElementById("newGameButton").addEventListener('click', () => {
    newGame()
})

function newGame(){
    chess.reset()
    chessboard.update()
    chessboard.initializeEventListener()
    document.getElementById("resignButton").style.display = "block"
    document.getElementById("undoButton").style.display = "block"
    document.getElementById("newGameButton").style.display = "none"
}