<template>
  <div id="app">
    <Header/>
    <main>
      <RouterView/>
    </main>
    <Footer/>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import store from "@/scripts/store";
import axios from "axios";
import {useRoute} from "vue-router";
import {watch} from "vue";

export default {
  name: 'App',
  components: {
    Footer,
    Header
  },
  setup() {
    const check = () => {
      axios.get("/api/account/check").then((res) => {

        if (res.data) {
          store.commit("setAccount", res.data);
        } else {
          store.commit("setAccount", 0);
        }
      })
    };

    const route = useRoute(); //url 관련된 정보를 가져오고

    watch(route, () => { //경로가 바뀔 때마다 감시하는 watch
      check(); // 즉... route가 바뀔 때마다 check 메서드를 실행하라
    })
  }
}
</script>

<style scoped>
#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

main {
  flex: 1;
}
</style>
