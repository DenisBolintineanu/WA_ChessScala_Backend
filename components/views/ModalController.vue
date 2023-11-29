<template>
    <div class="w-full">
        <div id="iconbar" class="flex justify-center sm:flex-wrap gap-5 md:gap-10 2xl:gap-16 2xl:mb-10 z-10">
            <div v-tooltip="{ text: 'Home', position: 'top' }" @click.stop="resetState">
                <font-awesome-icon :icon="['fas', 'house']" :class="[iconClasses]" />
            </div>
            <div v-tooltip="{ text: 'Play', position: 'top' }" @click.stop="openModal('gamesetup')">
                <font-awesome-icon :icon="['fas', 'circle-play']" :class="[iconClasses]" />
            </div>
            <div v-tooltip="{ text: 'Learn more about the rules of chess and this project', position: 'top' }">
                <font-awesome-icon :icon="['fas', 'circle-info']" :class="[iconClasses]" @click.stop="openModal('gameinfo')"/>
            </div>
            <div>
                <div v-if="isLoggedIn" v-tooltip="{ text: 'User settings', position: 'top' }">
                    <font-awesome-icon :icon="['fas', 'circle-user']" :class="[iconClasses]" />
                </div>
                <div v-else v-tooltip="{ text: 'Login/Signup', position: 'top' }" @click.stop="openModal('login-signin')">
                    <font-awesome-icon :icon="['fas', 'right-to-bracket']" :class="[iconClasses]" />
                </div>
            </div>
            <div v-if="isLoggedIn" v-tooltip="{ text: 'Messages', position: 'top' }">
                <font-awesome-icon :icon="['fas', 'message']" :class="[iconClasses]" />
            </div>
            <div v-if="isLoggedIn" v-tooltip="{ text: 'History', position: 'top' }">
                <font-awesome-icon :icon="['fas', 'table-list']" :class="[iconClasses]" />
            </div>
        </div>
    </div>
</template>
  
<script>
export default {
    computed: {
        darkModeEnabled() {
            return this.$store.state.darkModeEnabled;
        },
        textColorClasses() {
            return this.darkModeEnabled ? 'text-white' : 'text-gray-800';
        },
        iconClasses() {
            return `${this.textColorClasses} drop-shadow-icon sm:text-xl md:text-3xl 2xl:text-5xl hover:scale-110 hover:cursor-pointer hover:drop-shadow-icon-dark z-10`;
        },
    },
    methods: {
        openModal(modalName) {
            console.log(modalName);
            if (this.$store.state.activeModal === modalName) {
                this.$store.commit('setActiveModal', null);
            } else {
                this.$store.commit('setActiveModal', modalName);
                window.addEventListener('click', this.checkClickOutside);            
            }
        },
        checkClickOutside(event) {
            const modalElement = document.querySelector('.modal');
            console.log(event.target);
            if (modalElement && !modalElement.contains(event.target) && event.target.id != 'themetoggle') {
                this.closeModal();
            }
        },
        closeModal() {
            this.$store.commit('setActiveModal', null);
            window.removeEventListener('click', this.checkClickOutside);
        },
        resetState() {
            this.$store.commit('setActiveModal', null);
            this.$store.commit('setIsPlaying', false);
        }
    }
}
</script>

