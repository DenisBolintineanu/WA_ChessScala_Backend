import './styles/styles.scss'
import './components/html-components/BoxBackgroundComponent'
import { createApp } from 'vue';
import { library } from '@fortawesome/fontawesome-svg-core'
import { faCircleUser, faHouse, faInfoCircle, faMessage, faPlayCircle, faTableList, faLightbulb, faRightToBracket } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import tooltip from './directives/tooltip';

library.add(faHouse, faPlayCircle, faInfoCircle, faCircleUser, faMessage, faTableList, faLightbulb, faRightToBracket);

const app = createApp({
    data() { 
        return {
            darkModeEnabled: false,
            isLoggedIn: false, // retrieve from server
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
        }
    }
});

app.config.compilerOptions.isCustomElement = tag => tag === 'box-component';

app.directive('tooltip', tooltip);
app.component('font-awesome-icon', FontAwesomeIcon);
app.mount("#app");