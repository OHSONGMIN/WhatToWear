<template>
  <Header/>
  <RouterView/>
  <Footer/>
</template>

<script>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import store from "@/scripts/store";

export default {
  name: 'App',
  components: {
    Footer,
    Header
  },
  setup() {
    const id = sessionStorage.getItem("id");

    if (id) {
      store.commit("setAccount", id);
    }
  }
}
</script>

<style scoped>

</style>
