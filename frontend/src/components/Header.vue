<template>
  <header>
    <div class="header-container">
      <div class="left">
        <router-link class="logo" to="/">오늘 뭐 입지?</router-link>
      </div>

      <div class="right">
        <div v-if="isLoggedIn" class="nav-item">
          <div v-if="getMemberId !== 1">
            <router-link to="/history">마이페이지</router-link>
          </div>
          <div v-else>
            <router-link to="/admin">[관리자 페이지]</router-link>
          </div>
        </div>

        <div class="nav-item">
          <router-link to="/login" v-if="!isLoggedIn">로그인</router-link>
          <a @click="logout()" v-else>로그아웃</a>
        </div>
      </div>
      <!--      <div>-->
      <!--        스토아 스토아 {{store.state.memberId}}-->
      <!--      </div>-->
    </div>
  </header>
</template>

<script>
import router from "@/scripts/router";
import axios from "axios";
import {mapActions, mapGetters} from "vuex";
import store from "@/scripts/store";

export default {
  name: 'Header',
  computed: {
    ...mapGetters([`isLoggedIn`]), //Vuex의 isLoggedIn 상태 사용
    ...mapGetters([`getMemberId`]), //Vuex의 isLoggedIn 상태 사용
  },
  methods: {
    ...mapActions([`logout`]), //Vuex의 logout 액션 사용
  },
  setup() {
    // //상태 관리
    // const state = reactive({
    //   isLoggedIn: !!sessionStorage.getItem(`access`)
    // });

    const logout = () => {
      axios.post("/api/main/logout").then(() => {

        // access 토큰 제거, 상태 업데이트
        sessionStorage.removeItem(`access`);
        store.dispatch(`logout`);

        // 메인 페이지로 이동
        router.push({path: "/"});
      }).catch(error => {

        console.error("로그아웃 실패: " + error);
      })
    }

    return {logout};
  }
}
</script>

<style scoped>
header {
  padding: 25px 20px;
  background-color: #F6F5F2;
  border-bottom: 1px solid #f0f0f0;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.header-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: nowrap; /* Prevent items from wrapping to the next line */
}

.left {
  flex-shrink: 1;
}

.right {
  display: flex;
  align-items: center;
  flex-shrink: 1;
  margin-left: auto; /* Push the navigation items to the right */
}

.logo {
  font-size: 24px; /* Reduced font size for better fit on smaller screens */
  font-weight: bold;
  color: #333333;
  text-decoration: none;
  white-space: nowrap; /* Prevent text from wrapping */
  transition: color 0.3s;
}

.nav-item {
  color: #333333; /* Same color as the logo */
  cursor: pointer;
  margin-left: 5px; /* Reduced margin for better fit on smaller screens */
  font-size: 14px; /* Reduced font size for better fit on smaller screens */
  white-space: nowrap; /* Prevent text from wrapping */
  transition: color 0.3s;
}

.nav-item a {
  color: inherit; /* Inherit color from parent */
  text-decoration: none; /* Remove underline */
}

.nav-item a:hover {
  /* color: #B0AB99;  Change color on hover */
  text-decoration: none;
}

@media (max-width: 768px) {
  header {
    padding: 10px; /* Further reduced padding for very small screens */
  }

  .header-container {
    flex-direction: row;
    flex-wrap: nowrap;
    align-items: center;
    justify-content: space-between;
  }

  .logo {
    font-size: 16px; /* Further reduce font size for very small screens */
    margin-bottom: 0; /* Remove bottom margin */
  }

  .right {
    margin-left: auto;
    flex-direction: row;
    align-items: center;
  }

  .nav-item {
    margin-left: 5px; /* Further reduce left margin */
    margin-bottom: 0; /* Remove bottom margin */
    font-size: 12px; /* Further reduce font size for very small screens */
  }
}
</style>
