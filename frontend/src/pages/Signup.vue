<template>
  <div class="container mt-5">
    <div class="card w-100 m-auto">
      <div class="card-body w-100 m-auto">
        <h3 class="card-title text-center mb-3">Sign Up</h3><br>
        <div class="form-signup mb-3">
          <div class="form-floating">
            <input type="email" class="form-control" id="floatingInput" placeholder="name@example.com"
                   @keyup.enter="submit()"
                   v-model="state.form.email"
                   @blur="checkEmail()">
            <label for="floatingInput">이메일</label>
          </div>
          <div class="badge badge-danger mb-1" v-if="!state.availableEmailForm">올바른 이메일 형식이 아닙니다.</div>
          <div class="badge badge-danger mb-1" v-if="!state.availableEmail">이미 사용중인 이메일입니다.</div>
        </div>

        <div class="form-floating mb-1">
          <input type="password" class="form-control" id="floatingPassword" placeholder="Password"
                 @keyup.enter="submit()"
                 v-model="state.form.password"
                 @keyup="checkPassword()">
          <label for="floatingPassword">비밀번호</label>
        </div>

        <div class="mb-4">
          <div class="form-floating">
            <input type="password" class="form-control" id="floatingPasswordConfirm" placeholder="PasswordConfirm"
                   @keyup.enter="submit()"
                   v-model="state.form.passwordConfirm"
                   @keyup="checkPassword()">
            <label for="floatingPasswordConfirm">비밀번호 확인</label>
          </div>
          <div class="badge badge-danger mb-1" v-if="!state.equalsPassword">비밀번호가 일치하지 않습니다.</div>
        </div>
        <button class="btn btn-primary w-100 py-2 sign-up-button" @click="submit">회원가입</button>
      </div>
    </div>
  </div>
  <br>
</template>

<script>
import {reactive} from "vue";
import axios from "axios";
import router from "@/scripts/router";

export default {
  name: "Signup",
  setup() {
    const state = reactive({
      form: {
        email: "",
        password: "",
        passwordConfirm: ""
      },
      availableEmail: true,
      availableEmailForm: true,
      equalsPassword: true,
    });

    const submit = async () => {
      //availableEmail, availableEmailForm, equalsPassword true 여부 확인
      //비밀번호 글자수 확인
      //비밀번호 암호화

      if (!state.availableEmailForm) {
        window.alert("올바른 이메일 형식이 아닙니다");
        return;
      }

      if (!state.availableEmail) {
        window.alert("이미 사용중인 이메일입니다.");
        return;
      }

      if (!state.equalsPassword) {
        window.alert("비밀번호가 일치하지 않습니다.");
        return;
      }

      if (state.form.password.length < 6) {
        window.alert("비밀번호는 최소 6자 이상이어야 합니다.");
        return;
      }

      try {
        await axios.post("/api/main/signup", state.form);
        window.alert("회원가입 성공!")
        await router.push({path: "/login"});
      }
      catch (error) {
        window.alert("회원 가입 실패! 다시 시도해주세요.");
      }
    };

    const checkPassword = () => {

      //비밀번호 일치 여부 검증
      state.equalsPassword = (state.form.password === state.form.passwordConfirm);
    }

    const checkEmail = async () => {

      state.availableEmailForm = true; //올바름 형식이다
      state.availableEmail = true; //중복이 아니다

      //이메일 유효성 검사
      if (checkInvalidateEmail(state.form.email)) {
        state.availableEmailForm = false;
        return;
      }
      else {
        state.availableEmailForm = true;
      }

      //이메일 중복 검사
      state.availableEmail = await checkDuplicateEmail(state.form.email);
    }

    const checkDuplicateEmail = async (email) => {
      try {
        const response = await axios.post(`/api/main/signup/dupl/${email}`);

        return !response.data;
      } catch (error) {
        console.error("Error checking email:", error);

        return false;
      }
    }

    //이메일 유효성 검사
    const checkInvalidateEmail = (email) => {
      const emailForm = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;

      return !emailForm.test(email)
    }
    //if (checkInvalidateEmail(state.form.email)) {
    //  state.availableEmailForm = false;
    //} else {
    //  state.availableEmailForm = true;
    //}

    return {state, submit, checkEmail, checkPassword}
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
  /* box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 10px; */
  border: none;

}

.card-body {
  max-width: 330px;
  padding: 1rem;
  width: 100%;
}

.card-title {
  /* font-weight: bold; */
  color: #635E4E;
}

.form-control {
  border-radius: 5px;
  padding: 10px;
  font-size: 0.9rem;
}

.btn-primary {
  background-color: #635E4E;
  border-color: #635E4E;
  transition: background-color 0.3s, border-color 0.3s;
}

.btn-primary:hover {
  background-color: #B0AB99;
  border-color: #B0AB99;
}

.badge-danger {
  color: red;
}
</style>
