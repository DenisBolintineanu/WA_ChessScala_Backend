<script setup>
</script>
<template>
    <div @click="toggleThemeSwitch" class="z-10 w-full h-0">
        <div v-if="darkModeEnabled">
            <i id="themetoggle"
                :class="[`fa-regular fa-lightbulb text-white drop-shadow-icon sm:text-xl md:text-3xl 2xl:text-5xl hover:scale-110 hover:cursor-pointer`, 'relative', 'left-[90%]', 'top-10', 'hover:cursor-pointer', 'hover:drop-shadow-icon-bulb z-10']" />
        </div>
        <div v-else>
            <i id="themetoggle"
                :class="[`fa-solid fa-lightbulb text-gray-800 drop-shadow-icon sm:text-xl md:text-3xl 2xl:text-5xl hover:scale-110 hover:cursor-pointer hover:drop-shadow-icon-dark`, 'relative', 'left-[90%]', 'top-10', 'hover:cursor-pointer', 'hover:drop-shadow-icon-dark z-10']" />
        </div>
    </div>
</template>
<script>
export default {
    computed: {
        darkModeEnabled() {
            return this.$store.state.darkModeEnabled;
        }
    },
    methods: {
        toggleThemeSwitch() {
            this.$store.state.darkModeEnabled = !this.$store.state.darkModeEnabled;
        }
    }
}
</script>