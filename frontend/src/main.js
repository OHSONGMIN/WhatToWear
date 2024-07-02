import {createApp} from 'vue'
import store from "@/scripts/store"
import router from "@/scripts/router";
import App from './App.vue'

import 'bootstrap-icons/font/bootstrap-icons.css';

createApp(App).use(store).use(router).mount('#app')
