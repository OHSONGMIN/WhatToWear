import {createStore} from 'vuex'

const store = createStore({
    state: {
        access: sessionStorage.getItem(`access`) || ``,
        isLoggedIn: !!sessionStorage.getItem(`access`),
        memberId: sessionStorage.getItem(`memberId`) || 0
    },
    mutations: {
        setLoginStatus(state, status) {
            state.isLoggedIn = status;
        },
        setMemberId(state, memberId) {
            state.memberId = memberId;
        }
    },
    actions: {
        login( { commit } ) {
            //sessionStorage.setItem(`access`, accessToken);
            commit(`setLoginStatus`, true);
        },
        logout( {commit} ) {
            //sessionStorage.removeItem(`access`);
            commit(`setLoginStatus`, false);
        },
        setId( {commit} , memberId) {
            commit(`setMemberId`, memberId);
        }

    },
    getters: {
        isLoggedIn: state => state.isLoggedIn,
        getMemberId: state => state.memberId
    }

/*
    actions: {
        // 로그인 시도
        login({ dispatch }, loginObj) {
            // 로그인 -> 토큰 반환
            axios.post("/api/account/login", loginObj)
                .then(res => {
                    // 성공 시 token: 실제로는 memberId값을 받아온다
                    // 토큰을 헤더에 포함시켜서 유저 정보를 요청
                    let access = res.headers.get("access")
                    // 토큰을 로컬스토리지에 저장
                    localStorage.setItem("access", access);
                    dispatch("getMemberInfo")
                })
                .catch(() => {
                    alert("이메일과 비밀번호를 확힌하세요.")
                })
            //즉, 토큰을 로컬 스토리지에 저장하고 새로고침 해서 vue application이 실행되었을 때
            //로컬 스토리지에 토큰이 있다면 토큰을 가지고 유저 정보를 요청하자.
        },
        logout({ commit }) {
            commit("logout")
            router.push({path: "/login"})
        },
        getMemberInfo( {commit} ) {
            //로컬 스토리지에 저장된 토큰을 불러온다.
            let local_access = localStorage.getItem("access")
            let config = {
                headers: {
                    "access": local_access
                }
            }
            //반환된 토큰 -> 유저 정보 반환 (이름, id 등)
            //새로고침 -> 토큰만 가지고 유저 정보를 요청
            axios.get("/api/account/memberInfo", config)
                .then(res => {
                    let memberInfo = {
                        id: res.data.id,
                    }
                    commit("loginSuccess", memberInfo)
                })
                .catch(() => {
                    alert("이메일과 비밀번호를 확인해주세요.")
                })
        }
    }*/
})
/*

실제 로그인을 시도하는 것과 토큰을 가지고 유저 정보를 불러오는 것을 분리한 이유는
어플리케이션이 새로고침 될 때
실제 로그인을 하지 않고도 유저 정보를 불러오기 위해서
그래서 getMemberInfo로 유저 정보를 불러왔을 때
유저 정보가 불러와지면 로그인된 유저라고 인정해주는 것....

즉 어플리케이션이 실행될 때 token을 이용해서 getMemberInfo를 진행하면 되는 것

원래는 token을 인증하고 인증된 token으로 유저 정보를 불러오고
그리고 로컬 스토리지에 token이 있는지 없는지 한번더 검증하는 과정을 거치면 좋음

*/

export default store;