import {Chessboard2} from "../chessboard2.js";

describe("The chessboard builder should", () => {
    test("build a chessboard correctly", () => {
        const div = document.createElement("div")
        div.id = "chessboard"
        let chessboard = new Chessboard2()
        expect(chessboard.chessboard).toBe(null)
        chessboard.assign(div)
        expect(chessboard.chessboard.innerHTML).toBe("")
        chessboard.createChessboard(false)
        let innerHTML = chessboard.chessboard.innerHTML.toString()
        expect(innerHTML.includes("chess-field")).toBe(true)
        expect(innerHTML.includes("white-field")).toBe(true)
        expect(innerHTML.includes("black-field")).toBe(true)
    })
    test("build a inverse-chessboard correctly", () => {
        const div = document.createElement("div")
        div.id = "chessboard"
        let chessboard = new Chessboard2()
        chessboard.assign(div)
        chessboard.createChessboard(true)
        let innerHTML = chessboard.chessboard.innerHTML.toString()
        expect(innerHTML.includes("chess-field")).toBe(true)
        expect(innerHTML.includes("white-field")).toBe(true)
        expect(innerHTML.includes("black-field")).toBe(true)
    })
    test("build a chessboard with a rook on every field", () => {
        const div = document.createElement("div")
        div.id = "chessboard"
        let chessboard = new Chessboard2()
        chessboard.assign(div)
        chessboard.createChessboard(true)
        chessboard.updateBoardWithPieces(() => {
            return ""
        })
        chessboard.updateBoardWithPieces(() => {
            return "wr"
        })
        let innerHTML = chessboard.chessboard.innerHTML.toString()
        expect(innerHTML.includes("<img")).toBe(true)
    })
})