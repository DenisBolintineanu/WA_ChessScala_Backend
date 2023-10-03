import {Chessboard2} from "../chessboard2.js";
import {Chess} from "../chess.js";

const chessboard = new Chessboard2()
const chess = new Chess()
const div = document.createElement("div")
div.id = "chessboard"
chessboard.assign(div)
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
function expectedString(field) {
    return "<div class=\"chess-field white-field\" id=\"" + field + "\"><img class=\"figure\""

}
describe("The game should", () => {
    test("be playable by entering the moves", () => {
        expect(chessboard.chessboard.innerHTML.includes(expectedString("e2"))).toBe(true)
        expect(chessboard.firstClick).toBe(null)
        chessboard.handleFieldClick(chessboard.chessboard.querySelector("#e2"));
        expect(chessboard.firstClick).not.toBe(null)
        chessboard.handleFieldClick(chessboard.chessboard.querySelector("#e4"));
        expect(chessboard.chessboard.innerHTML.includes(expectedString("e2"))).toBe(false)
        chessboard.handleFieldClick(chessboard.chessboard.querySelector("#e2"));
        expect(chessboard.firstClick).toBe(null)
        chessboard.handleFieldClick(chessboard.chessboard.querySelector("#e7"));
        expect(chessboard.firstClick).not.toBe(null)
        chessboard.handleFieldClick(chessboard.chessboard.querySelector("#g5"));
        expect(chessboard.firstClick).toBe(null)
        chessboard.handleFieldClick(chessboard.chessboard.querySelector("#e7"));
        chessboard.handleFieldClick(chessboard.chessboard.querySelector("#f7"));
        expect(chessboard.firstClick).not.toBe(null)
        chessboard.handleFieldClick(chessboard.chessboard.querySelector("#f7"));
        expect(chessboard.firstClick).toBe(null)

    })
})