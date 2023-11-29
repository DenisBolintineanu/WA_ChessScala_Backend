import { Chess } from "chess.js";
import { ChessBoardBuilder } from "./ChessboardBuilder";

export class SinglePlayer {
    constructor() {
        this.chess = new Chess();
        this.stockfish = new Worker("/js/stockfish.js");
        this.team = null;
        this.popup = this.createPopup();
        this.chessBoardBuilder = new ChessBoardBuilder(this.chess, 'w', this.update.bind(this));
        this.chessBoard = this.chessBoardBuilder.createChessBoard(document.querySelector("#Chessboard"));
    }

    update() {
        if (this.chess.turn() !== this.team)
            setTimeout(this.doAIMove.bind(this), 1500);
    }

    doAIMove() {
        this.stockfish.postMessage('position fen ' + this.chess.fen());
        this.stockfish.postMessage("setoption name Skill Level value 1");
        this.stockfish.postMessage("go movetime 50");

        this.stockfish.onmessage = (event) => {
            let message = event.data || event.data.data;
            if (message.includes('bestmove')) {
                let move = message.split(' ')[1];
                if (move.length === 4) {
                    this.chessBoard.asciiMove(move.substring(0, 4));
                } else {
                    this.chessBoard.asciiMove(move.substring(0, 4), move.substring(4, 5));
                }
            }
        };
    }

    createPopup() {
        let popup = document.createElement("div");
        popup.classList.add("popup");
        document.body.appendChild(popup);
        let selectTeam = document.createElement('div');
        selectTeam.classList.add('selectTeam');
        popup.appendChild(selectTeam);
        let buttonWhite = document.createElement('div');
        let buttonBlack = document.createElement('div');
        buttonWhite.classList.add("btn");
        buttonBlack.classList.add("btn");
        buttonWhite.innerHTML = "White";
        buttonBlack.innerHTML = "Black";
        selectTeam.appendChild(buttonWhite);
        selectTeam.appendChild(buttonBlack);

        buttonWhite.addEventListener('click', () => {
            this.setTeam('w');
            popup.style.display = 'none';
        });

        buttonBlack.addEventListener('click', () => {
            this.setTeam('b');
            popup.style.display = 'none';
            setTimeout(this.doAIMove.bind(this), 500);
        });

        return popup;
    }

    setTeam(team) {
        this.team = team;
        this.chessBoard = this.chessBoardBuilder.createChessBoard(document.querySelector("#Chessboard"), true, team);
    }
}
