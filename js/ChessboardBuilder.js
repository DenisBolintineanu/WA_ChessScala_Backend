import {Chessboard} from "./Chessboard.js";
import { Chess } from "chess.js";
import {AsyncPromotionHandler} from "./AsyncPromotionHandler.js";

export class ChessBoardBuilder {
    #chessboard = null
    #promotionHandler = null
    #team = null
    #chess = null
    #updateFunction = null

    constructor(chess, team = "default", update = null) {
        this.#team = team
        this.#chess = chess
        this.#updateFunction = update
    }

    createChessBoard (chessBoardDOM, eventListener, team = "default") {
        this.#chessboard = new Chessboard()
        this.#promotionHandler = new AsyncPromotionHandler(this.#chess)
        this.#chessboard.assign(chessBoardDOM)
        this.#team = team;
        if (this.#team === 'b'){
            this.#chessboard.createChessboard(true)
        }
        else {
            this.#chessboard.createChessboard(false)
        }
        this.#chessboard.highlightFunction = (field) => {
            return this.#chess.moves({verbose: true, square: field})
        }
        this.#chessboard.isAttackedFunction = () => {
            return this.#chess.isCheck()
        }
        this.#chessboard.getKingFunction = () => {
            return this.#getPositionsOfPiece({type: 'k', color: this.#chess.turn()})
        }
        this.#chessboard.updateFunction = field => {
            return this.#chess.get(field)
        }
        this.#chessboard.isSelectable = field => {
            if (this.#team === 'w'){
                return this.#chess.get(field.id).color === 'w' && this.#chess.turn() === 'w'
            }
            if (this.#team === 'b'){
                return this.#chess.get(field.id).color === 'b' && this.#chess.turn() === 'b'
            }
            return this.#chess.get(field.id).color === this.#chess.turn()
        }
        this.#chessboard.asciiMove = this.#serialMoveFunction
        this.#chessboard.moveFunction = this.#asyncMoveFunction
        this.#chessboard.update()
        if (eventListener) {
            this.#chessboard.initializeEventListener()
        }
        this.#chessboard.newGameFunction = this.#newGame
        return this.#chessboard
    }

    #asyncMoveFunction = (move) => {
        let promotion = this.#promotionHandler.doPromotion(move)
        if (promotion) {
            promotion.then(figure => {
                this.#serialMoveFunction(move, figure)
            })
        } else {
            this.#serialMoveFunction(move, null)
        }
    }

    #serialMoveFunction = (move, promotion, update=true) => {
        try {
            let isHit = this.#chess.get(move.substring(2, 4)) !== false
            if (promotion) {
                this.#chess.move({
                    from: move.substring(0, 2),
                    to: move.substring(2, 4),
                    promotion: promotion
                })
            } else {
                this.#chess.move({
                    from: move.substring(0, 2),
                    to: move.substring(2, 4)
                })
            }
            this.#chessboard.update()
            if (this.#checkIfGameIsOver()){
                // this.#playSound(false, true)
            }
            else {
                // this.#playSound(isHit, false)
            }
            if(update) {
                this.#updateFunction(move)
            }
        } catch (exception) {
            this.#chessboard.handleException(move)
        }
    }

    #playSound = (isHit, gameOver) => {
        if (gameOver) {
            this.#sounds.notification.play()
        } else if (isHit) {
            this.#sounds.capture.play()
        } else {
            this.#sounds.move.play()
        }
    }

    #sounds = {
        move: document.getElementById("moveSound"),
        capture: document.getElementById("captureSound"),
        notification: document.getElementById("notificationSound")
    };

    #checkIfGameIsOver = () => {
        if (this.#chess.isThreefoldRepetition()) {
            this.gameOver("")
            return true
        }
        if (this.#chess.isInsufficientMaterial()) {
            this.gameOver("")
            return true
        }
        if (this.#chess.isGameOver()) {
            this.gameOver("")
            return true
        }
        return false
    }

    gameOver = (text) => {
        this.#chessboard.removeEventListener()
        this.#sounds.notification.play()
        //document.getElementById("resignButton").style.display = "none"
        //document.getElementById("newGameButton").style.display = "block"
    }

    #getPositionsOfPiece = (piece) => {
        const positions = [];
        const board = this.#chess.board();
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

     #newGame = () => {
        this.#chess.reset()
        this.#chessboard.update()
        this.#chessboard.initializeEventListener()
        // document.getElementById("game-id").style.display = "none"
        // document.getElementById("resignButton").style.display = "block"
        // document.getElementById("undoButton").style.display = "block"
        // document.getElementById("newGameButton").style.display = "none"
    }
}