<template>
    <div class="h-200 w-200">
        <button class="bg-blue-800 rounded-lg py-4 px-2" @click="startGame('online')">
            Play online
        </button>
        <button class="bg-blue-800 rounded-lg py-4 px-2" @click="startGame('local')">
            Local Multiplayer
        </button>
        <button class="bg-blue-800 rounded-lg py-4 px-2" @click="startGame('singleplayer')">
            Against the AI
        </button>
    </div>
</template>
  
<script>
export default {
    props: {
        isVisible: Boolean
    },
    data() {
        return {
            color: '',
            gameId: '',
            gameMode: '',
        };
    },
    methods: {
        close() {
            this.$emit('close');
        },
        startGame(gameMode) {
            this.$store.state.activeModal = null;
            this.$store.state.isPlaying = true;
            this.$store.state.gameMode = gameMode;
        }
    }
};
</script>
  
<style scoped>

</style>
  