import {Chess} from "./chess.js";
import {Chessboard2} from "./chessboard2.js";

let chess = new Chess()
let chessboard = new Chessboard2()
chessboard.assign(document.getElementById("chessboard2"))
chessboard.createChessboard(false)
chessboard.updateBoardWithPieces(field => chess.get(field))
