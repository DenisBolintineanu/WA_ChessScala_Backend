<template>
    <div :class="[modalClasses]">
        <LoginSignupModal class="modal" v-if="activeModal === 'login-signin'" />
        <GameSetupModal class="modal" v-if="activeModal === 'gamesetup'" />
        <GameInfoModal class="modal" v-if="activeModal === 'gameinfo'" />
    </div>
</template>
  
<script>

import LoginSignupModal from './modals/LoginSignupModal.vue';
import GameSetupModal from './modals/GameSetupModal.vue';
import GameInfoModal from './modals/GameInfoModal.vue';

export default {
    components: {
    LoginSignupModal,
    GameSetupModal,
    GameInfoModal
},
    computed: {
        darkModeEnabled() {
            return this.$store.state.darkModeEnabled;
        },
        modalClasses() {
            if (this.activeModal === null) {
                return '';
            } else {
                let classes = this.darkModeEnabled ? 'bg-slate-800/80 text-white border-white-300' : 'bg-white/80 text-gray-800 border-slate-800';
                return `${classes} absolute top-[20%] left-[50%] translate-x-[-50%] z-20 p-8 border-2 rounded-lg shadow-xl min-w-[30%] max-h-[60%] overflow-y-auto`
            }
             
        },
        activeModal() {
            return this.$store.state.activeModal;
        },
    },
};
</script>

<style scoped>

</style>