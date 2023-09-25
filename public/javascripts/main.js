import {Chess} from "./chess.js";
import {Chessboard2} from "./chessboard2.js";

let chess = new Chess()
let chessboard = new Chessboard2()
chessboard.assign(document.getElementById("chessboard2"))
chessboard.createChessboard(false)
chessboard.updateBoardWithPieces(field => chess.get(field))
chessboard.initializeEventListener(field => {
    return chess.get(field.id).color === chess.turn()
}, move => {
    try {
        const result = chess.move({from: move.substring(0, 2), to: move.substring(2, 4)})
        if (result) {
            chessboard.move(move)
        }
    } catch (exception) {
        chessboard.handleException(move)
    }
})
