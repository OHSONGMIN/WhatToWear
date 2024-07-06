<template>
  <div class="container mt-5">
    <div class="card w-100 m-auto">
      <div class="card-body w-100 m-auto">
        <h3 class="card-title text-center mb-3">Sign Up</h3><br>
        <div class="form-signup">
          <div class="form-floating mb-3">
            <input type="email" class="form-control" id="floatingInput" placeholder="name@example.com"
                   @keyup.enter="submit()"
                   v-model="state.form.email"
                   @blur="checkDuplicate()">
            <label for="floatingInput">이메일</label>
            <span class="badge badge-danger mt-1" v-if="!availableEmail">이미 사용중인 이메일입니다.</span>
            <span class="badge badge-danger mt-1" v-if="!availableEmailForm">올바른 이메일 형식이 아닙니다.</span>
          </div>
        </div>

        <div class="form-floating mb-1">
          <input type="password" class="form-control" id="floatingPassword" placeholder="Password"
                 @keyup.enter="submit()"
                 v-model="state.form.password">
          <label for="floatingPassword">비밀번호</label>
        </div>

        <div class="form-floating mb-4">
          <input type="password" class="form-control" id="floatingPasswordConfirm" placeholder="PasswordConfirm"
                 @keyup.enter="submit()"
                 v-model="state.form.passwordConfirm">
          <label for="floatingPasswordConfirm">비밀번호 확인</label>
        </div>

        <button class="btn btn-primary w-100 py-2 sign-up-button" @click="submit">회원가입</button>
      </div>
    </div>
  </div>
  <br>
</template>

<script>
import {reactive} from "vue";

export default {
  name: "Signup",
  setup() {
    const state = reactive({
      form: {
        email: "",
        password: "",
        passwordConfirm: ""
      },
      availableEmail: "",
      availableEmailForm: "",

    });

    const submit = () => {
      // 회원가입 로직을 여기에 추가
    };

    const checkDuplicate = () => {
      //일단 true로 초기화
      this.availableEmail = true;

      function checkDuplicateEmail(email) {
        return false;
      }

      //이메일 중복 검사
      if (checkDuplicateEmail(this.email)) { //이메일이 중복이라면
        this.availableEmail = false;
      } else {
        this.availableEmail = true;
      }

      this.availableEmailForm = true;

      //이메일 유효성 검사
      if (checkValidateEmail(this.email)) { //이메일이 유효하다면
        this.availableEmailForm = true;
      } else {
        this.availableEmailForm = false;
      }
    }
    return {state, submit, checkDuplicate};
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
</style>
