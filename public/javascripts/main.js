import {Chess} from "./chess.js";
import {Chessboard2} from "./chessboard2.js";
import {AsyncPromotionHandler} from "./AsyncPromotionHandler.js";

let chess = new Chess()
let chessboard = new Chessboard2()
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

function checkIfGameIsOver() {
    if (chess.isThreefoldRepetition()){
        chessboard.removeEventListener()
    }
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

function serialMoveFunction(move, promotion) {
    try {
        let result
        if (promotion){
            result = chess.move({from: move.substring(0, 2),
                                       to: move.substring(2, 4),
                                       promotion: promotion})
        }
        else {
            result = chess.move({from: move.substring(0, 2),
                to: move.substring(2, 4)})
        }
        if (result) {
            chessboard.update()
        }
        checkIfGameIsOver()
    } catch (exception) {
        chessboard.handleException(move)
    }
}