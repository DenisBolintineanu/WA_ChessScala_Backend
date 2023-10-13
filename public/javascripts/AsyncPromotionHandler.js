export class AsyncPromotionHandler {

    #promotionModal
    #closeModalBtn
    #promotionButtons
    #chess

    #WHITE = 'w'

    #pieces = [
        {name: 'queen', abbreviation: 'q'},
        {name: 'rook', abbreviation: 'r'},
        {name: 'bishop', abbreviation: 'b'},
        {name: 'knight', abbreviation: 'n'}
    ]

    constructor(chess) {
        this.#chess = chess
        this.#initializeDOMElements()
    }

    doPromotion(move) {
        let result = this.#isPromotion(move)
        if (!result) return false
        return this.#createPromotionModal()
    }

    #getStartRow = () => {return this.#chess.turn() === this.#WHITE ? '7' : '2'}
    #getTargetRow = () => {return this.#chess.turn() === this.#WHITE ? '8' : '1'}

    #isPromotion(move) {
        if (move.substring(1, 2) !== this.#getStartRow()) return false
        if (move.substring(3, 4) !== this.#getTargetRow()) return false
        if (this.#chess.get(move.substring(0,2)).type !== 'p') return false
        return this.#chess
            .moves({verbose: true, square: move.substring(0, 2)})
            .map(field => field.to)
            .includes(move.substring(2, 4))
    }

    async #handleUserChoice(promotionButtons) {
        return new Promise((resolve) => {
            promotionButtons.forEach(button => {
                button.addEventListener("click", function (event) {
                    resolve(event.target.getAttribute("data-piece"))
                }, {once: true});
            });
        })
    }

    #createPromotionModal(){
        this.#promotionModal.style.display = "flex";
        let promotion = this.#handleUserChoice(this.#promotionButtons)
        promotion.then(() => this.#promotionModal.style.display = 'none')
        return promotion
    }

    #initializePromotionModal(){
        this.#promotionModal = document.createElement('div');
        this.#promotionModal.classList.add('promotionModal');
        this.#promotionModal.style.display = "none"
        document.body.appendChild(this.#promotionModal);
    }

    #initializeCloseButton(){
        this.#closeModalBtn = document.createElement('button');
        this.#closeModalBtn.classList.add('closeModal');
        this.#closeModalBtn.textContent = 'Close';
        this.#closeModalBtn.onclick = () => { this.#promotionModal.style.display = "none"; };
        this.#promotionModal.appendChild(this.#closeModalBtn);
    }

    #initializePromotionButtons(){
        this.#promotionButtons = this.#pieces.map(piece => {
            const button = document.createElement('button');
            button.setAttribute('data-piece', piece.abbreviation);
            button.textContent = piece.name;
            button.classList.add('modal-content')
            this.#promotionModal.appendChild(button);
            return button;
        });
    }

    #initializeDOMElements() {
        this.#initializePromotionModal()
        this.#initializeCloseButton()
        this.#initializePromotionButtons()
    }
}