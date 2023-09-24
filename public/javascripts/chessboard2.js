export class Chessboard2 {

    chessboard = null
    assign(div) {
        this.chessboard = div
    }

    createChessboard(inverse) {
        this.chessboard.innerHTML = ""
        for (let i = -8; i <= 0; i++) {
            for (let j = -1; j <= 7; j++) {
                const div = document.createElement("div");

                if (i === 0 && j >= 0) {
                    div.className = "number-field";
                    if (!inverse)
                        div.textContent = String.fromCharCode('A'.charCodeAt(0) + j);
                    else
                        div.textContent = String.fromCharCode('H'.charCodeAt(0) - j);
                } else if (i < 0 && j === -1) {
                    div.className = "number-field";
                    if (!inverse)
                        div.textContent = -i;
                    else
                        div.textContent = 9 + i;
                } else if (i < 0 || j > 0) {
                    div.className = (i + j) % 2 === 0 ? "chess-field white-field" : "chess-field black-field";
                    if (!inverse)
                        div.id = String.fromCharCode('a'.charCodeAt(0) + j) + (-i);
                    else
                        div.id = String.fromCharCode('h'.charCodeAt(0) - j) + (9 + i);
                }

                this.chessboard.appendChild(div);
            }
        }
    }

    updateBoardWithPieces(expression) {
       this.chessboard.querySelectorAll(".chess-field").forEach( field => {
           this.updateFieldWithPiece(field, expression(field.id))
       })
    }
    updateFieldWithPiece(field, piece) {
        if (piece) {
            const src = this.getImage(piece.type, piece.color);
            field.innerHTML = `<img class="figure" src="${src}"  alt=""/>`;
        } else {
            field.innerHTML = "";
        }
    }

    getImage(piece, color) {
        const ColorMap = {
            "w": "white",
            "b": "black"
        };

        const PieceMap = {
            "r": "Rook",
            "n": "Knight",
            "b": "Bishop",
            "k": "King",
            "q": "Queen",
            "p": "Pawn"
        };

        return `assets/images/chesspieces/${ColorMap[color]}${PieceMap[piece]}.png`;
    }
}