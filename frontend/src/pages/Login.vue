<template>
  <div class="container mt-5">
    <div class="card w-100 m-auto">
      <div class="form-signin w-100 m-auto">
        <h3 class="card-title mb-3 fw-normal">Welcome back,</h3><br>

        <div class="form-floating mb-1">
          <input type="email" class="form-control" id="floatingInput" placeholder="name@example.com"
                 @keyup.enter="submit()"
                 v-model="state.form.email">
          <label for="floatingInput">이메일</label>
        </div>
        <div class="form-floating mb-4">
          <input type="password" class="form-control" id="floatingPassword" placeholder="Password"
                 @keyup.enter="submit()"
                 v-model="state.form.password">
          <label for="floatingPassword">비밀번호</label>
        </div>

        <button class="btn btn-primary w-100 py-2 login-button" @click="submit()">로그인</button>

        <div class="sign-up">
          <br>
          아직 계정이 없나요?
          <router-link to="/signup" style="color: #635E4E;"> 회원가입</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {reactive} from "vue";
import axios from "axios";
import router from "@/scripts/router";
import {mapActions, mapGetters} from "vuex";
import store from "@/scripts/store";
// import store from "@/scripts/store";
// import router from "@/scripts/router";
// import {jwtDecode} from "jwt-decode";

export default {
  name: "Login",
  computed: {
    ...mapGetters([`isLoggedIn`]), //Vuex의 isLoggedIn 상태 사용
  },
  methods: {
    ...mapActions([`logout`]), //Vuex의 logout 액션 사용
  },
  setup() {
    const state = reactive({
      form: {
        email: "",
        password: ""
      }
    })

    const submit = () => {
      axios.post("/api/main/login", state.form, {
        headers: {
          'Content-Type': 'application/json',
        }
      }).then(() => {

        store.dispatch(`login`);

        // member Entity의 Id 가져오기
        axios.get("/api/account/info")
            .then((userInfoResponse) => {
              console.log("userInfoResponse " + userInfoResponse.data);

              const { memberId } = userInfoResponse.data;
              store.dispatch(`setId`, memberId);
              console.log("memberId는....." + memberId);

              if (memberId === 1) {

                //관리자 권한 확인
                axios.get("/api/account/check_admin")
                    .then((res) => {
                      const isAdmin = res.data;
                      console.log("isAdmin " + isAdmin);

                      if (isAdmin) {
                        router.push({path: "/admin"});
                        window.alert("관리자로 접속하였습니다.");
                      }
                      else {
                        router.push({path: "/"});
                        window.alert("로그인 되었습니다.");
                      }
                    })
                    .catch((error) => {
                      console.error("권한 확인 실패", error);
                      window.alert("권환 확인 중 오류가 발생했습니다.");
                    })
              }
              // 일반 유저일 경우
              else {
                router.push({path: "/"});
                window.alert("로그인 되었습니다.")
              }
            })
            .catch((error) => {
              console.error("memberInfo 가져오기 실패", error);
              window.alert("회원 데이터를 가져오는 데 실패했습니다.");
            })


      }).catch((error) => {
        // if (error.response && error.response.status === 401) {
        //   window.alert("로그인 정보가 없습니다. 다시 확인해 주세요.")
        // }
        // else {
        //   window.alert("오류가 발생했습니다. 다시 시도해주세요.");
        // }
        console.log(error);
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

}

.card {
  border: none;
}

.card-title {
  font-weight: bold;
  color: #635E4E;
}

.form-signin {
  max-width: 330px;
  padding: 1rem;
  width: 100%;
}

.form-signin .fw-normal {
  text-align: center;
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

.sign-up {
  text-align: center;
}
</style>