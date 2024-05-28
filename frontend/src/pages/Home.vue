<template>
  <div class="col" v-for="outfit in state.outfits" :key="outfit.id">
    <Card :outfit="outfit" @deleted="load"/> <!-- outfit이라는 이름으로 outfit객체를 전달-->
    <!-- Card 컴포넌트에서 발생한 deleted 이벤트를 수신 -->
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
      outfits: []
    })

    const load = () => {
      axios.get("/api/outfits").then((res) => {
        state.outfits = res.data;
      })
    }

    load();

    return {state, load}
  }
}
</script>

<style scoped>

</style>