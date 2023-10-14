import {Chessboard} from "../Chessboard.js";
import {Chess} from "../chess.js";
import {ChessBoardBuilder} from "../ChessBoardBuilder.js";


let div = document.createElement('div')
div.id = 'Chessboard'
document.body.appendChild(div)

let undoButton = document.createElement('div')
undoButton.id = 'undoButton'
document.body.appendChild(undoButton)

let resignButton = document.createElement('div')
resignButton.id = 'resignButton'
document.body.appendChild(resignButton)

let newGameButton = document.createElement('div')
newGameButton.id = 'newGameButton'
document.body.appendChild(newGameButton)

let chess = new Chess()
let chessboardBuilder = new ChessBoardBuilder(chess)
let chessboard = chessboardBuilder.createChessBoard(div, true)

function getFigure(field, color, figure) {
    let div = "<div class=\"chess-field "
    if (color === 'w') div = div + "white-field\" "
    else div = div + "black-field\" "
    div = div + "id=\"" + field + "\"><img class=\"figure\" src=\"assets/images/chesspieces/" + figure + ".png\" alt=\"\">"
    return div
}

function doMove(start, target){
    chessboard.chessboard.querySelector('#' + start).click()
    chessboard.chessboard.querySelector('#' + target).click()
}

describe("The game should", () => {

    test("show the selected figure", () => {
        expect(chessboard.firstClick).toBe(null)
        chessboard.chessboard.querySelector('#e2').click()
        expect(chessboard.firstClick.id).toBe('e2')
        chessboard.chessboard.querySelector('#e2').click()
        expect(chessboard.firstClick).toBe(null)
        chessboard.chessboard.querySelector('#e7').click()
        expect(chessboard.firstClick).toBe(null)
        chessboard.chessboard.querySelector('#e2').click()
        expect(chessboard.firstClick.id).toBe('e2')
        chessboard.chessboard.querySelector('#d2').click()
        expect(chessboard.firstClick.id).toBe('d2')
        chessboard.chessboard.querySelector('#d2').click()
        expect(chessboard.firstClick).toBe(null)
    })

    test("contain all figures at the beginning of the game", () => {
        expect(chessboard.chessboard.innerHTML.includes(getFigure('a1', 'b', 'whiteRook'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('b1', 'w', 'whiteKnight'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('c1', 'b', 'whiteBishop'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('d1', 'w', 'whiteQueen'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('e1', 'b', 'whiteKing'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('f1', 'w', 'whiteBishop'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('g1', 'b', 'whiteKnight'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('h1', 'w', 'whiteRook'))).toBe(true)

        expect(chessboard.chessboard.innerHTML.includes(getFigure('a8', 'w', 'blackRook'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('b8', 'b', 'blackKnight'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('c8', 'w', 'blackBishop'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('d8', 'b', 'blackQueen'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('e8', 'w', 'blackKing'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('f8', 'b', 'blackBishop'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('g8', 'w', 'blackKnight'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('h8', 'b', 'blackRook'))).toBe(true)

        expect(chessboard.chessboard.innerHTML.includes(getFigure('a2', 'w', 'whitePawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('b2', 'b', 'whitePawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('c2', 'w', 'whitePawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('d2', 'b', 'whitePawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('e2', 'w', 'whitePawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('f2', 'b', 'whitePawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('g2', 'w', 'whitePawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('h2', 'b', 'whitePawn'))).toBe(true)

        expect(chessboard.chessboard.innerHTML.includes(getFigure('a7', 'b', 'blackPawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('b7', 'w', 'blackPawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('c7', 'b', 'blackPawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('d7', 'w', 'blackPawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('e7', 'b', 'blackPawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('f7', 'w', 'blackPawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('g7', 'b', 'blackPawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('h7', 'w', 'blackPawn'))).toBe(true)
    })


    test("do the moves correctly", () => {
        doMove('c2', 'c4')
        expect(chessboard.chessboard.innerHTML.includes(getFigure('c2', 'w', 'whitePawn'))).toBe(false)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('c4', 'w', 'whitePawn'))).toBe(true)
        doMove('d7', 'd5')
        expect(chessboard.chessboard.innerHTML.includes(getFigure('d5', 'w', 'blackPawn'))).toBe(true)
        doMove('c4', 'd5')
        expect(chessboard.chessboard.innerHTML.includes(getFigure('d5', 'w', 'whitePawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('c4', 'w', 'whitePawn'))).toBe(false)
        doMove('c7', 'c5')
        doMove('d5', 'c6')
        expect(chessboard.chessboard.innerHTML.includes(getFigure('d5', 'w', 'whitePawn'))).toBe(false)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('c6', 'w', 'whitePawn'))).toBe(true)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('c5', 'b', 'blackPawn'))).toBe(false)
        doMove('a7','a5')
        doMove('c6', 'c7')
        expect(chessboard.chessboard.innerHTML.includes(getFigure('c7', 'b', 'whitePawn'))).toBe(true)
        doMove('a5', 'a4')
        doMove('c7','b8')
        document.querySelector('.closeModal').click()
        chessboard.chessboard.querySelector('#c7').click()
        expect(chessboard.chessboard.innerHTML.includes(getFigure('c7', 'b', 'whitePawn'))).toBe(true)
    })

    test("handle a checkmate", () => {
        chess = new Chess()
        chessboardBuilder = new ChessBoardBuilder(chess)
        chessboard = chessboardBuilder.createChessBoard(div, true)

        doMove('f2','f4')
        doMove('e7','e6')
        doMove('h2','h3')
        doMove('d8','h4')
        doMove('g2','g3')
        doMove('h4', 'g3')
    })

    test("test the buttons", () => {
        chess = new Chess()
        chessboardBuilder = new ChessBoardBuilder(chess, 'b')
        chessboard = chessboardBuilder.createChessBoard(div, true)

        chessboard.moveFunction('e2e4')
        expect(chessboard.chessboard.innerHTML.includes(getFigure('e4', 'w', 'whitePawn'))).toBe(true)
        doMove('e7', 'e5')
        expect(chessboard.chessboard.innerHTML.includes(getFigure('e5', 'b', 'blackPawn'))).toBe(true)
        document.getElementById('undoButton').click()
        expect(chessboard.chessboard.innerHTML.includes(getFigure('e4', 'w', 'whitePawn'))).toBe(false)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('e5', 'b', 'blackPawn'))).toBe(false)
        document.getElementById('resignButton').click()
        document.getElementById('newGameButton').click()
        expect(chessboard.chessboard.innerHTML.includes(getFigure('e4', 'w', 'whitePawn'))).toBe(false)
        expect(chessboard.chessboard.innerHTML.includes(getFigure('e4', 'w', 'whitePawn'))).toBe(false)
        chessboard.newGameFunction()
    })
})