<template>
    <div class="login-signup-container">
        <div v-if="showLogin">
            <h2>Login</h2>
            <form @submit.prevent="handleLogin">
                <input type="email" v-model="loginEmail" placeholder="E-Mail">
                <input type="password" v-model="loginPassword" placeholder="Passwort">
                <button type="submit">Login</button>
            </form>
            <p>Noch kein Konto? <button @click="toggleForm">Registrieren</button></p>
        </div>

        <div v-else>
            <h2>Registrieren</h2>
            <form @submit.prevent="handleSignup">
                <input type="email" v-model="signupEmail" placeholder="E-Mail">
                <input type="password" v-model="signupPassword" placeholder="Passwort">
                <button type="submit">Registrieren</button>
            </form>
            <p>Bereits registriert? <button @click="toggleForm">Login</button></p>
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
            showLogin: true,
            loginEmail: '',
            loginPassword: '',
            signupEmail: '',
            signupPassword: ''
        };
    },
    methods: {
        toggleForm() {
            this.showLogin = !this.showLogin;
        },
        handleLogin() {
            // Implementieren Sie die Login-Logik hier
            console.log('Login mit:', this.loginEmail, this.loginPassword);
        },
        handleSignup() {
            // Implementieren Sie die Signup-Logik hier
            console.log('Registrierung mit:', this.signupEmail, this.signupPassword);
        },
        close() {
            this.$emit('close');
        }
    }
};
</script>
  
<style scoped>

input[type="email"],
input[type="password"] {
    /* Stilisierung der Input-Felder */
}

button {
    /* Stilisierung der Buttons */
}
</style>
  