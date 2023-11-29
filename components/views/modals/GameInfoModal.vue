<template>
    <div class="h-full w-full flex justify-center items-center">
        <div class="p-2 overflow-y-auto rounded-lg shadow-lg max-w-md w-full" style="position: sticky; top: 0; z-index: 1000;">
            <div class="flex justify-around border-b">
                <button class="pb-2" :class="{ 'border-b-2 border-blue-500': currentTab === 'rules' }"
                    @click="currentTab = 'rules'">
                    Rules
                </button>
                <button class="pb-2" :class="{ 'border-b-2 border-blue-500': currentTab === 'about' }"
                    @click="currentTab = 'about'">
                    About
                </button>
            </div>
            <div class="p-4">
                <div v-if="currentTab === 'rules'">
                    <h2 class="text-lg font-bold underline mb-2">Objective</h2>
                    <p>The primary objective in chess is to checkmate your opponent's king. Checkmate occurs when the king
                        is in a position to be captured (in check) and cannot escape from capture.</p>

                    <h2 class="text-lg font-bold underline mb-2 mt-4">Setup</h2>
                    <p>The game is played on an 8x8 square board with alternating light and dark squares. Each player starts
                        with 16 pieces: one king, one queen, two rooks, two knights, two bishops, and eight pawns.</p>

                    <h2 class="text-lg font-bold underline mb-2 mt-4">Moves</h2>
                    <ul class="list-disc pl-5">
                        <li><strong>Pawns:</strong> Move forward one square, but capture diagonally. On their first move,
                            they can move forward two squares.</li>
                        <li><strong>Rooks:</strong> Move horizontally or vertically any number of squares.</li>
                        <li><strong>Knights:</strong> Move in an L-shape: two squares in one direction and then one square
                            perpendicular.</li>
                        <li><strong>Bishops:</strong> Move diagonally any number of squares.</li>
                        <li><strong>Queen:</strong> Move diagonally, horizontally, or vertically any number of squares.</li>
                        <li><strong>King:</strong> Move one square in any direction.</li>
                    </ul>

                    <h2 class="text-lg font-bold underline mb-2 mt-4">Special Moves</h2>
                    <ul class="list-disc pl-5">
                        <li><strong>Castling:</strong> A move involving the king and either rook, moving the king two
                            squares towards the rook, and the rook to the square the king crossed.</li>
                        <li><strong>En passant:</strong> A special pawn capture that can only occur immediately after a pawn
                            moves two squares forward from its starting position and an opposing pawn captures it as if it
                            had moved only one square.</li>
                        <li><strong>Promotion:</strong> When a pawn reaches the farthest row from its starting position, it
                            can be promoted to any other piece, usually a queen.</li>
                    </ul>

                    <h2 class="text-lg font-bold underline mb-2 mt-4">End of the Game</h2>
                    <p>The game ends in checkmate, a draw, or resignation. A draw can occur in several ways, including
                        stalemate, insufficient material, threefold repetition, or the fifty-move rule.</p>

                </div>
                <div v-if="currentTab === 'about'">
                    <p>Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua...</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        isVisible: Boolean
    },
    data() {
        return {
            currentTab: 'rules'
        };
    },
    methods: {
        close() {
            this.$emit('close');
        },
    }
};
</script>
  