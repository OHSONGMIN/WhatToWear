import {createApp} from 'vue'
import store from "@/scripts/store"
import router from "@/scripts/router";
import App from './App.vue'

import 'bootstrap-icons/font/bootstrap-icons.css';
import axios from "axios";

//sessionStorage에서 memberId를 가져와 Vuex에 저장
const saveMemberId = sessionStorage.getItem(`memberId`);
if (saveMemberId) {
    store.commit(`setMemberId`, parseInt(saveMemberId));
}

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

        console.log(`newAccessToken은 없음`);

        return response;

    },
    async (error) => {

        console.log("request 하다가 에러 발생");
        const originalRequest = error.config;

        if (error.response) {
            const status = error.response.status;

            //console.log("재시도 전" + originalRequest._retry);

            // if (status === 419) {
            //
            //     router.push({path: "/login"});
            // }

            // 401 에러 && 재시도가 아닌 경우
            if (status === 401 && !originalRequest._retry) {
                originalRequest._retry = true;

                console.log("재시도 시작" + originalRequest._retry);

                try {
                    // refresh, access Token 재발급
                    console.log("reissue 호출");
                    const reissueResponse = await axios.post(`/api/main/reissue`);
                    console.log("reissue 호출 완료");


                    if (reissueResponse.status === 200) {
                        // 재발급 성공
                        // 새로운 access 토큰을 저장
                        const newAccessToken = reissueResponse.headers[`access`];
                        sessionStorage.setItem(`access`, newAccessToken);

                        // 원래의 요청 헤더를 갱신
                        originalRequest.headers[`access`] = `${newAccessToken}`

                        console.log("토큰 재발급 성공");

                        // 원래의 요청 재시도
                        return axios(originalRequest);
                    } else {
                        console.log("상태 코드가 200이 아님");
                        sessionStorage.removeItem(`access`);
                        return Promise.reject(reissueResponse);
                    }
                } catch (reissueError) {
                    // 재발급 실패
                    console.error(`토큰 재발급 실패:`, reissueError);

                    sessionStorage.removeItem(`access`);
                    router.push({path: "/login"});
                    return Promise.reject(reissueError);
                }
            }

            // 403 에러 Forbidden
            if (status === 403) {
                console.error(`403 Error`, error);
                alert('이 리소스에 접근할 권한이 없습니다.');

                router.push({path: "/"});
            }

            // 404 에러 Not Found
            if (status === 404) {
                console.error(`404 Error`, error);
                alert(`요청한 리소스를 찾을 수 없습니다.`)
            }

            // 500 에러 Internal Server Error
            if (status === 500) {
                console.error(`500 Error`, error);
                //alert('서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.');
            }
        }
        else {
            // 네트워크 또는 다른 이유로 응답을 받지 못한 경우
            console.error(`응답을 받지 못했습니다:`, error);
            alert('서버와의 연결이 원활하지 않습니다. 네트워크 상태를 확인해주세요.');
        }
        return Promise.reject(error);

    }


)

