import {createStore} from 'vuex'
import Cookies from 'js-cookie'

const store = createStore({
    state() {
        return {
            account: {
                id: Cookies.get(`id`) || 0
            },
            authToken: localStorage.getItem(`authToken`) || null
        }
    },
    mutations: {
        setAccount(state, payload) {
            state.account.id = payload;
            Cookies.set(`id`, payload, {expires: 7})
            console.log(state.account.id + "넘어왔나요?")
        },
        clearAccount(state) {
            state.account.id = 0;
            Cookies.remove(`id`);
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