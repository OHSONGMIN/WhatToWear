<template>
  <div class="container mt-5">
    <div class="card w-100 m-auto">
      <div class="card-body w-100 m-auto">
        <h3 class="card-title text-center mb-3 fw-normal">회원 탈퇴</h3><br>

        <div class="form-changeInfo mb-3">
          <div class="form-floating">
            <input type="password" class="form-control" id="floatingInput" placeholder="기존 비밀번호"
                   @keyup.enter="submit()"
                   v-model="state.form.password">
            <label for="floatingInput">비밀번호</label>
          </div>
        </div>
        <h6 class="card-message text-center"><i class="bi bi-exclamation-triangle-fill"></i>
          탈퇴 후 동일한 이메일로<br>
          재가입이 불가합니다.</h6>
        <br>
        <button class="btn btn-primary w-100 py-2 login-button" @click="submit()">회원 탈퇴</button>
      </div>
    </div>
  </div>
</template>

<script>
import {reactive} from "vue";
import axios from "axios";
import router from "@/scripts/router";
import store from "@/scripts/store";

export default {
  name: "WithdrawMember",
  setup() {
    const state = reactive({
      form: {
        password: "",
      },
    });

    const submit = async () => {

      if (window.confirm("정말 탈퇴하시겠습니까? 회원 탈퇴 시 리뷰가 모두 삭제 처리됩니다.")) {

        try {
          await axios.patch("/api/account/withdraw", state.form);

          window.alert("회원 탈퇴되었습니다.");

          // access 토큰 제거, 상태 업데이트
          sessionStorage.removeItem(`access`);
          await store.dispatch(`logout`);

          await  router.push({path: "/"});
        }
        catch (error) {
          window.alert("회원 탈퇴에 실패했습니다. 비밀번호를 다시 확인해주세요.");
        }
      }
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
  color: #635E4E;
}

.card-message {
  color: #EB5353;
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

.badge-danger {
  color: red;
}
</style>