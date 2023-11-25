import './styles/styles.scss'
import './components/html-components/BoxBackgroundComponent'
import { createApp } from 'vue';
import { library } from '@fortawesome/fontawesome-svg-core'
import { faCircleUser, faHouse, faInfoCircle, faMessage, faPlayCircle, faTableList, faLightbulb, faRightToBracket } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
// Directives
import tooltip from './directives/tooltip';
// Components
import LoginSignupModal from './components/views/LoginSignupModal.vue';
import GameSetupModal from './components/views/LoginSignupModal.vue';

library.add(faHouse, faPlayCircle, faInfoCircle, faCircleUser, faMessage, faTableList, faLightbulb, faRightToBracket);

const app = createApp({
    components: {
        LoginSignupModal,
        GameSetupModal,
    },
    data() { 
        return {
            templateShown: true,
            showGameSetupModal: false,
            showLoginSignupModal: false,
            darkModeEnabled: false,
            isLoggedIn: false,
            isPlaying: false,
        }
     },
    computed: { 
        logo_classes() {
            let classes = this.darkModeEnabled ? 'drop-shadow-logo-dark-bg' : 'drop-shadow-logo-light-bg';
            return classes + ` max-w-[40%] lg:max-w-[20%] pb-20 z-10`;
        },
        background_color_classes() {
            return this.darkModeEnabled ? 'bg-custom-dark' : 'bg-custom-light';
        },
        text_color_class() {
            return this.darkModeEnabled ? 'text-white' : 'text-gray-800';
        },
        icon_classes() {
            if (this.darkModeEnabled) {
                return this.text_color_class +  ' drop-shadow-icon ' + ` sm:text-xl md:text-3xl 2xl:text-5xl`;
            } 
            return `${this.text_color_class} sm:text-xl md:text-3xl 2xl:text-5xl`;
        },
        lightbulb_classes() {
            if (this.darkModeEnabled) {
                return `${this.text_color_class} hover:drop-shadow-icon-bulb hover:scale-110 drop-shadow-icon hover:scale-110 hover:cursor-pointer sm:text-xl md:text-3xl 2xl:text-5xl absolute right-10 top-10 hover:cursor-pointer`;
            } 
            return `${this.text_color_class} hover:scale-110 hover:drop-shadow-icon-dark drop-shadow-icon hover:scale-110 hover:cursor-pointer sm:text-xl md:text-3xl 2xl:text-5xl absolute right-10 top-10 hover:cursor-pointer`
        }
    },
    methods: {
        toggleDarkMode() {
            this.darkModeEnabled = !this.darkModeEnabled;
        },
        toggleLoginSignup() {
            if (this.showLoginSignupModal) {
                this.closeAllModals();
            } else {
                this.showLoginSignupModal = true;
            }
        },
        toggleGamesetupModal() {
            if (this.showGameSetupModal) {
                this.closeAllModals();
            } else {
                this.showGameSetupModal = true;
            }
        },
        toggleGame() {
            this.isPlaying = !this.isPlaying;
        },
        closeAllModals() {
            this.showLoginSignupModal = false;
            this.showGameSetupModal = false;
        }
    }
});

app.config.compilerOptions.isCustomElement = tag => tag === 'box-component';

app.directive('tooltip', tooltip);
app.component('loginsignupmodal', LoginSignupModal);
app.component('gamesetupmodal', GameSetupModal);
app.component('font-awesome-icon', FontAwesomeIcon);
app.mount("#app");