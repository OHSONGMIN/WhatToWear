<template>
  This is App.vue~~~~~~~~~
  <div class="col" v-for="(item, idx) in state.items" :key="idx">
    <Card :item="item"/> <!-- item이라는 이름으로 item객체를 전달-->
  </div>
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

    axios.get("/api/items").then((res) => {
      state.itmes = res.data;
    })

    return {state}
  }
}
</script>

<style scoped>

</style>