<template>
  <div class="container mt-5">
    <div class="card w-100 m-auto">
      <div class="card-body w-100 m-auto">
        <h3 class="card-title text-center mb-3 fw-normal">비밀번호 수정</h3><br>

        <div class="form-changeInfo mb-3">
          <div class="form-floating">
            <input type="email" class="form-control" id="floatingInput" placeholder="기존 비밀번호"
                   @keyup.enter="submit()"
                   v-model="state.form.oriPassword">
            <label for="floatingInput">기존 비밀번호</label>
          </div>
        </div>

        <div class="form-floating mb-1">
          <input type="password" class="form-control" id="floatingPassword" placeholder="비밀번호"
                 @keyup.enter="submit()"
                 v-model="state.form.password"
                 @keyup="checkPassword()">
          <label for="floatingPassword">비밀번호</label>
        </div>

        <div class="mb-4">
          <div class="form-floating">
            <input type="password" class="form-control" id="floatingPasswordConfirm" placeholder="비밀번호 확인"
                   @keyup.enter="submit()"
                   v-model="state.form.passwordConfirm"
                   @keyup="checkPassword()">
            <label for="floatingPasswordConfirm">비밀번호 확인</label>
          </div>
          <div class="badge badge-danger mb-1" v-if="!state.equalsPassword">비밀번호가 일치하지 않습니다.</div>
        </div>

        <button class="btn btn-primary w-100 py-2 login-button" @click="submit()">수정하기</button>
      </div>
    </div>
  </div>
</template>

<script>
import {reactive} from "vue";

export default {
  name: "ChangeInfo",
  setup() {
    const state = reactive({
      form: {
        oriPassword: "",
        password: "",
        passwordConfirm: ""
      },
      equalsPassword: true,
    })

    const checkPassword = () => {

      //비밀번호 일치 여부 검증
      state.equalsPassword = (state.form.password === state.form.passwordConfirm);
    }

    const submit = () => {

    }
    return {state, checkPassword, submit}
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
  color: #635E4E;
}

.card-body {
  max-width: 330px;
  padding: 1rem;
  width: 100%;
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