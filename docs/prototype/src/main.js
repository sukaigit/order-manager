import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import api from './utils/api'
import './assets/design.css'

const app = createApp(App)
app.config.globalProperties.$api = api
window.api = api
app.use(router).mount('#app')
