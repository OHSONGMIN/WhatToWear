<template>
  <div class="container">
    <div class="form-signin w-100 m-auto">
      <h1 class="h3 mb-3 fw-normal">Please sign in</h1>

      <div class="form-floating">
        <input type="email" class="form-control" id="floatingInput" placeholder="name@example.com"
               @keyup.enter="submit()"
               v-model="state.form.email">
        <label for="floatingInput">Email address</label>
      </div>
      <div class="form-floating">
        <input type="password" class="form-control" id="floatingPassword" placeholder="Password"
               @keyup.enter="submit()"
               v-model="state.form.password">
        <label for="floatingPassword">Password</label>
      </div>

      <button class="btn btn-primary w-100 py-2 login-button" @click="submit()">Sign in</button>
    </div>
  </div>
</template>

<script>
import {reactive} from "vue";
import axios from "axios";
import store from "@/scripts/store";
import router from "@/scripts/router";

export default {
  setup() {
    const state = reactive({
      form: {
        email: "",
        password: ""
      }
    })

    const submit = () => {
      axios.post("/api/account/login", state.form).then((res) => {
        store.commit('setAccount', res.data); //로그인 하고 받은 id 값을 store에 저장하겠다.
        //sessionStorage.setItem("id", res.data); //지워도 될 듯... sessionStorage 사용하지 않으니까
        router.push({path: "/"});
        window.alert("로그인하였습니다.");
      }).catch(() => {
        window.alert("로그인 정보가 존재하지 않습니다.");
      })
    }

    return {state, submit}
  }
}
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  /* min-height: 400px; */
  height: 70vh;
}

.form-signin {
  max-width: 330px;
  padding: 1rem;
  width: 100%;
}

.form-signin .form-floating:focus-within {
  z-index: 2;
}

.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}

.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

.login-button {
  background-color: #635E4E;
}

.login-button:hover {
  background-color: #B0AB99;
}
</style>