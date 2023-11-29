<script setup>
import { Chess } from "chess.js";
import { ChessBoardBuilder } from "/js/ChessboardBuilder.js"
import { SinglePlayer } from '/js/SinglePlayer'

</script>
<template>
    <div class="z-10 absolute top-[50%] left-[50%] translate-x-[-50%] translate-y-[-50%]" style="min-width: 40vw; max-width: 80vw; max-height: 80vh; min-height: 40vh;">
        <div id="Chessboard" class="chessboard"></div>
    </div>
</template>
<script>
export default {
    mounted() {
        if (this.$store.state.gameMode === 'singleplayer') {
            let singlePlayer = new SinglePlayer()
        } else if (this.$store.state.gameMode === 'local') {
            let chess = new Chess()
            let chessBoardBuilder = new ChessBoardBuilder(chess)
            let chessBoard = chessBoardBuilder.createChessBoard(document.querySelector("#Chessboard"), true)
        }
    },
};
</script>
  
<style scoped></style>
  