import {createApp} from 'vue'
import store from "@/scripts/store"
import router from "@/scripts/router";
import App from './App.vue'

import 'bootstrap-icons/font/bootstrap-icons.css';
import axios from "axios";

createApp(App).use(store).use(router).mount('#app')

axios.interceptors.request.use(
    (config) => {
        const accessToken = localStorage.getItem(`access`);

        if (accessToken) {
            config.headers[`access`] = `${accessToken}`
        }

        return config;
    },
    (error) => {
        console.log(error);

        return Promise.reject(error);
    });


axios.interceptors.response.use(
    (response) => {

        //응답에 access 토큰이 있는지 확인
        const newAccessToken = response.headers[`access`];

        if (newAccessToken) {

            //access 토큰을 로컬 스토리지에 저장
            localStorage.setItem(`access`, newAccessToken);
        }
        return response;
    },
    (error) => {
        // 404에러 처리
        if (error.response.status === 404) {
            
            console.log(`404페이지만들까만까만들까말까`);
        }
        
        return Promise.reject(error); //에러 다시 던지기
    }


)

