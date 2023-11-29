import { createStore } from 'vuex';

const store = createStore({
    state: {
        isLoggedIn: false,
        darkModeEnabled: false,
        activeModal: null,
        isPlaying: false,
        gameMode: '',
    },
    mutations: {
        setLoggedIn(state, status) {
            state.isLoggedIn = status;
        },
        toggleDarkMode(state) {
            state.darkModeEnabled = !state.darkModeEnabled;
        },
        setActiveModal(state, modalName) {
            state.activeModal = modalName;
        },
        closeModal(state) {
            console.log("close now")
            state.activeModal = null;
        },
        setIsPlaying(state, isPlaying) {
            if (isPlaying) {
                state.activeModal = null;
            }
            state.isPlaying = isPlaying;
        },
        setGameMode(state, gameMode) {
            state.gameMode = gameMode;
        }
    },
    actions: {
        login({ commit }) {
            // Login Logik
            commit('setLoggedIn', true);
        },
        logout({ commit }) {
            // Logout Logik
            commit('setLoggedIn', true);
        }
    }
});

export default store;