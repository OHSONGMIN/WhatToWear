import {createStore} from 'vuex'

const store = createStore({
    state() {
        return {
            account: {
                id: 0
            },
            authToken: localStorage.getItem(`authToken`) || null
        }
    },
    mutations: {
        setAccount(state, payload) {
            state.account.id = payload;
        },
        clearAccount(state) {
            state.account.id = 0;
        },
        setAuthToken(state, token) {
            state.authToken = token;
            localStorage.setItem(`authToken`, token);
        },
        clearAuthToken(state) {
            state.authToken = null;
            localStorage.removeItem(`authToken`);
        }
    }
})

export default store;