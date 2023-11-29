<template>
    <div class="flex flex-col items-center justify-center w-full h-full">
        <img src="assets/images/chesslogo.png"
            :class="[darkModeEnabled ? 'drop-shadow-logo-dark-bg' : 'drop-shadow-logo-light-bg', `max-w-[40%] lg:max-w-[20%] pb-20 mt-8 z-10`]">
        <img src="assets/images/chess.png" class="drop-shadow-main max-w-[70%] 2xl:max-w-[65%] z-10">
    </div>
</template>
<script>
export default {

    computed: {
        darkModeEnabled() {
            return this.$store.state.darkModeEnabled;
        }
    }

}
</script>