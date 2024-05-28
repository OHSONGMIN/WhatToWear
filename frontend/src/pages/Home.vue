<template>
  <div class="col" v-for="item in state.items" :key="item.id">
    <Card :item="item"/> <!-- item이라는 이름으로 item객체를 전달-->
  </div>
  <router-link to="/write" class="btn btn-primary">작성하기</router-link>
</template>

<script>
import axios from "axios";
import {reactive} from "vue";
import Card from "@/components/Card.vue";

export default {
  name: "Home",
  components: {Card},
  setup() {
    const state = reactive({
      items: []
    })

    axios.get("/api/outfits").then((res) => {
      state.items = res.data;
    })

    return {state}
  }
}
</script>

<style scoped>

</style>