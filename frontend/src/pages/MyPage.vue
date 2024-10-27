<template>
  <div class="my-container">
    <div class="empty-box"></div>
    <div class="content my-text">
      <span>로그인 정보</span>
      <span class="my-email">{{ email }}</span>
    </div>
    <div class="empty-box"></div>
    <router-link to="/history" class="my-text">
      <div class="content">
        <span>리뷰 관리</span>
        <span>></span>
      </div>
    </router-link>
    <router-link to="/changeInfo" class="my-text">
      <div class="content no-top-border">
        <span>비밀번호 수정</span>
        <span>></span>
      </div>
    </router-link>
    <div class="empty-box"></div>
    <router-link to="/withdrawMember" class="my-text">
      <div class="content">
        <span>회원 탈퇴</span>
        <span>></span>
      </div>
    </router-link>
  </div>
</template>

<script>
import axios from "axios";
import {ref} from "vue";

export default {
  name: "MyPage",
  setup() {
    const email = ref("");

    const getEmail = () => {
      axios.get("/api/account/getEmail")
          .then((res) => {
        email.value = res.data;
      })
    };

    getEmail();

    return { email }
  },
}
</script>

<style scoped>
.content {
  border-top: 1px solid #ccc;
  border-bottom: 1px solid #ccc;
  width: 100%;
  height: 60px;
  align-items: center;
  display: flex;
  padding: 0 10px;
  background-color: #F6F5F2;
  justify-content: space-between;
}

.my-text {
  text-decoration: none;
  color: #333333;
}

.no-top-border {
  border-top: none;
}

.empty-box {
  height: 25px;
  border: none;
  background-color: white;
  width: 100%;
}

.my-email {
  color: #EB5353;
}
</style>