import {createApp} from 'vue'
import store from "@/scripts/store"
import router from "@/scripts/router";
import App from './App.vue'

import 'bootstrap-icons/font/bootstrap-icons.css';
import axios from "axios";

createApp(App).use(store).use(router).mount('#app')

axios.interceptors.request.use(
    (config) => {
        const accessToken = sessionStorage.getItem(`access`);

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

            console.log(`Response new111` + newAccessToken);
            //access 토큰을 로컬 스토리지에 저장
            sessionStorage.setItem(`access`, newAccessToken);
        }

        console.log(`Response new222` + newAccessToken);

        return response;
    },
    async (error) => {

        const originalRequest = error.config;

        if (error.response && error.response.status === 401 && !originalRequest._retry) {

            // 401 오류이고 재시도가 아닌 경우
            originalRequest._retry = true;

            try {
                // /api/main/reissue로 access, refresh 재발급 요청
                const reissueResponse = await axios.post(`/api/main/reissue`);

                if (reissueResponse.status === 200) {
                    // 재발급 성공
                    // 새로운 access 토큰을 저장
                    const newAccessToken = reissueResponse.headers[`access`];
                    sessionStorage.setItem(`access`, newAccessToken);

                    // 원래의 요청 헤더를 갱신
                    originalRequest.headers[`access`] = `${newAccessToken}`

                    // 원래의 요청 재시도
                    return axios(originalRequest);
                }
            } catch (reissueError) {

                // 재발급 실패
                console.error(`토큰 재발급 실패:`, reissueError);

                sessionStorage.removeItem(`access`);
                router.push({path: "/login"});

                return Promise.reject(reissueError);


            }
        }
        return Promise.reject(error);

    }


)

