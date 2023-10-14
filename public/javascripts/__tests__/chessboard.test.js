import {Chessboard} from "../Chessboard.js";

describe("The chessboard builder should", () => {
    test("build a chessboard correctly", () => {
        const div = document.createElement("div")
        div.id = "chessboard"
        let chessboard = new Chessboard()
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
        let chessboard = new Chessboard()
        chessboard.assign(div)
        chessboard.createChessboard(true)
        let innerHTML = chessboard.chessboard.innerHTML.toString()
        expect(innerHTML.includes("chess-field")).toBe(true)
        expect(innerHTML.includes("white-field")).toBe(true)
        expect(innerHTML.includes("black-field")).toBe(true)
    })
    test("build a chessboard with no figure", () => {
        const div = document.createElement("div")
        div.id = "chessboard"
        let chessboard = new Chessboard()
        chessboard.assign(div)
        chessboard.createChessboard(true)
        chessboard.updateBoardWithPieces(() => {
            return ""
        })
        let innerHTML = chessboard.chessboard.innerHTML.toString()
        expect(innerHTML.includes("<img")).toBe(false)
    })
    test("build a chessboard with a rook on every field", () => {
        const div = document.createElement("div")
        div.id = "chessboard"
        let chessboard = new Chessboard()
        chessboard.assign(div)
        chessboard.createChessboard(true)
        chessboard.updateBoardWithPieces(() => {
            return {color: 'w', type: 'r'}
        })
        let innerHTML = chessboard.chessboard.innerHTML.toString()
        expect(innerHTML.includes("<img")).toBe(true)
    })
})

describe("The chessboard should", () => {
    test("move a figure from a position to another", () => {
        const div = document.createElement("div")
        div.id = "chessboard"
        let chessboard = new Chessboard()
        chessboard.assign(div)
        chessboard.createChessboard(false)
        chessboard.updateBoardWithPieces(field => {
            if (field === "e2")
                return { type:'p' , color:'w' };
            else return ""
        })
        let expectedString = "<div class=\"chess-field white-field\" id=\"e2\"><img class=\"figure\" src=\"assets/images/chesspieces/whitePawn.png\" alt=\"\">"
        expect(chessboard.chessboard.innerHTML.includes(expectedString)).toBe(true)
        chessboard.move("e2e4")
        expect(chessboard.chessboard.innerHTML.includes(expectedString)).toBe(false)
        expectedString = "<div class=\"chess-field white-field\" id=\"e4\"><img class=\"figure\" src=\"assets/images/chesspieces/whitePawn.png\" alt=\"\">"
        expect(chessboard.chessboard.innerHTML.includes(expectedString)).toBe(true)
    })
})
describe("The event listener should", () => {
    test("let all fields be selectable", () => {
        const div = document.createElement("div")
        div.id = "chessboard"
        let chessboard = new Chessboard()
        chessboard.assign(div)
        chessboard.createChessboard(false)
        chessboard.updateBoardWithPieces(field => {
            if (field === "e2")
                return { type:'p' , color:'w' };
            else return ""
        })
        chessboard.initializeEventListener(() => {return true}, () => chessboard.move);
        chessboard.handleFieldClick(chessboard.chessboard.querySelector("#e2"));
        expect(chessboard.firstClick).not.toBe(null);
        chessboard.handleFieldClick(chessboard.chessboard.querySelector("#e4"));

    })
    test("let only define fields be selectable", () => {
        const div = document.createElement("div")
        div.id = "chessboard"
        let chessboard = new Chessboard()
        chessboard.assign(div)
        chessboard.createChessboard(false)
        chessboard.updateBoardWithPieces(field => {
            if (field === "e2")
                return { type:'p' , color:'w' };
            else return ""
        })
        chessboard.initializeEventListener(field => {
            return field.innerHTML !== ""
        });
        chessboard.handleFieldClick(chessboard.chessboard.querySelector("#e1"));
        expect(chessboard.firstClick).toBe(null)
        chessboard.handleFieldClick(chessboard.chessboard.querySelector("#e2"));
        expect(chessboard.firstClick).not.toBe(null)
    })
})

describe("The click handler should", () => {
    test("do a move", () => {
        const div = document.createElement("div")
        div.id = "chessboard"
        let chessboard = new Chessboard()
        chessboard.assign(div)
        chessboard.createChessboard(false)
        chessboard.updateBoardWithPieces(field => {
            if (field === "e2")
                return { type:'p' , color:'w' };
            else return ""
        })
        chessboard.initializeEventListener(field => {
            return field.innerHTML !== ""
        }, chessboard.move);
        chessboard.handleFieldClick(chessboard.chessboard.querySelector("#e2"));
        chessboard.handleFieldClick(chessboard.chessboard.querySelector("#e4"));
        let expectedString = "<div class=\"chess-field white-field\" id=\"e4\"><img class=\"figure\" src=\"assets/images/chesspieces/whitePawn.png\" alt=\"\">"
        expect(chessboard.chessboard.innerHTML.includes(expectedString)).toBe(true)
    })
})