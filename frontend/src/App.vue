<template>
  <Header/>
  <Home/>
  <Footer/>
</template>

<script>
import Header from "@/components/Header.vue";
import Home from "@/pages/Home.vue";
import Footer from "@/components/Footer.vue";

export default {
  name: 'App',
  components: {
    Footer,
    Home,
    Header
  }
}
</script>

<style scoped>

</style>
