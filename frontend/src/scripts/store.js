import {createStore} from 'vuex'

/*

현재 문제점...
isLoggedIn은 access 토큰 여부로 확인하고.. main.js에서 토큰 만료하면 새로 등록하는 과정을 거치기 떄문에???
isLoggedIn 상태가 유지되고...

memberId는 새로고침하면 리셋된다.
유지되지 않는듯.... 이를 어떻게 해결하면 좋을지 토큰에서 가져와야하는가..?

문제 해결... 어떻게 해결했냐면
Vuex가 초기화될 때 상태 복원이 정확하게 이루어지는지 확인하고, 그 값이 컴포넌트에 반영되어야 한다.

1. main.js에서 memberId를 복수하는 코드를 작성하였다.
2. parseInt 하는 과정도 필요했다. (String으로 출력됐었던 듯)

 */
const store = createStore({
    state: {
        access: sessionStorage.getItem(`access`) || ``,
        isLoggedIn: !!sessionStorage.getItem(`access`),
        memberId: sessionStorage.getItem(`memberId`) || 0,
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
            sessionStorage.setItem(`isLoggedIn`, true);
            commit(`setLoginStatus`, true);
        },
        logout( {commit} ) {
            //sessionStorage.removeItem(`access`);
            sessionStorage.removeItem(`memberId`);
            commit(`setLoginStatus`, false);
            commit(`setMemberId`, 0);
        },
        setId( {commit} , memberId) {
            sessionStorage.setItem(`memberId`, memberId);
            commit(`setMemberId`, memberId);
        }

    },
    getters: {
        isLoggedIn: state => state.isLoggedIn,
        getMemberId: state => state.memberId
    }


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