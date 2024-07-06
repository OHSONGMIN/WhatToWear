<template>
  <header>
    <div class="header-container">
      <div class="left">
        <router-link class="logo" to="/">오늘 뭐 입지?</router-link>
      </div>

      <div class="right">
        <div v-if="$store.state.account.id" class="nav-item">
          <router-link to="/history">마이페이지</router-link>
        </div>
        <div class="nav-item">
          <router-link to="/login" v-if="!$store.state.account.id">로그인</router-link>
          <a @click="logout()" v-else>로그아웃</a>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import store from "@/scripts/store";
import router from "@/scripts/router";
import axios from "axios";

export default {
  name: 'Header',
  setup() {
    const logout = () => {
      axios.post("/api/account/logout").then(() => {
        store.commit("setAccount", 0);
        router.push({path: "/"});
      })
    }

    return {logout}
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
