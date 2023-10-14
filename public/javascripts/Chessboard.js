export class Chessboard {

    chessboard = null
    inverted = null
    firstClick = null
    isSelectable = null
    moveFunction = null
    updateFunction = null
    highlightFunction = null
    isAttackedFunction = null
    getKingFunction = null
    newGameFunction = null
    asciiMove = null

    assign(div) {
        this.chessboard = div
    }


    createChessboard(inverse) {
        this.inverted = inverse
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

    update() {
        this.updateBoardWithPieces()
        this.highlightAttack()
        this.deselectFields()
    }

    updateBoardWithPieces() {
        this.chessboard.querySelectorAll(".chess-field").forEach(field => {
            this.updateFieldWithPiece(field, this.updateFunction(field.id))
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

    selectField(field) {
        field.querySelector('.figure').classList.add('selected');
        this.firstClick = field
        this.highlightAvailableMoves()
    }

    deselectFields() {
        this.chessboard.querySelectorAll(".selected").forEach(selected => {
            selected.classList.remove('selected');
        })
        this.firstClick = null
        this.removeHighlight()
    }

    handleFieldClick(field) {
        if (this.isSelectable(field) || this.firstClick) {
            if (this.firstClick) {
                this.moveFunction(this.firstClick.id + field.id);
            } else {
                this.selectField(field);
            }
        }
    }

    addChessFieldListener(field) {
        field.addEventListener('click', () => {
            this.handleFieldClick(field);
        });
    }

    initializeEventListener() {
        this.chessboard.querySelectorAll(".chess-field")
            .forEach(field => this.addChessFieldListener(field));
    }

    removeEventListener() {
        this.chessboard.querySelectorAll(".chess-field")
            .forEach(field => field.replaceWith(field.cloneNode(true)))
    }

    handleException(move) {
        const selectedField = this.chessboard.querySelector("#" + move.substring(2, 4))
        if (selectedField === this.firstClick) {
            this.deselectFields()
            return
        }
        this.deselectFields()
        if (this.isSelectable(selectedField))
            this.selectField(selectedField)
    }

    highlightAvailableMoves() {
        let fields = this.highlightFunction(this.firstClick.id)
        fields.forEach(field => {
            let fields = this.chessboard.querySelector('#' + field.to)
            let available = document.createElement('div')
            available.classList.add("available")
            fields.appendChild(available)
        })
    }

    removeHighlight() {
        this.chessboard.querySelectorAll('.chess-field .available').forEach(field => {
            field.remove()
        })
    }

    highlightAttack() {
        if (this.isAttackedFunction()) {
            let field = this.getKingFunction()
            console.log(field)
            let attackedField = document.createElement('div')
            attackedField.classList.add('attacked')
            this.chessboard.querySelector('#' + field).appendChild(attackedField)
        } else {
            let attackedField = this.chessboard.querySelector('.chess-field .attacked')
            if (attackedField) {
                attackedField.remove()
            }
        }
    }
}