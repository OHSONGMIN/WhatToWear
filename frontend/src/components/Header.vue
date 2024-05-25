<template>
  This is Header!!!
  <div>
    <li>
      <router-link class="text-black" to="/">메인 화면</router-link>
    </li>
    <li>
      <!-- store에 접근할 땐 $를 써서... -->
      <router-link to="/login" class="text-black" v-if="!$store.state.account.id">로그인</router-link>
      <a to="/login" class="text-black" @click="logout()" v-else>로그아웃</a>
    </li>
  </div>
</template>

<script>
import store from "@/scripts/store";
import router from "@/scripts/router";

export default {
  name: 'Header',
  setup() {
    const logout = () => {
      store.commit("setAccount", 0);
      sessionStorage.removeItem("id");
      router.push({path: "/"});
    }

    return {logout}
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
