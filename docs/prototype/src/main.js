import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import './assets/design.css'

const app = createApp(App)
app.use(router)
app.config.globalProperties.$axios = axios
app.config.globalProperties.$api = 'http://localhost:8080'
app.mount('#app')
