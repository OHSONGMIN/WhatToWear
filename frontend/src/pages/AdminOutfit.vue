<template>
  <div class="col" v-for="outfit in state.outfits" :key="outfit.id">
    <Card :outfit="outfit" @deleted="load"/>
    <!-- Card 컴포넌트에서 발생한 deleted 이벤트를 수신 -->
  </div>
</template>

<script>
import axios from "axios";
import {reactive} from "vue";
import Card from "@/components/Card.vue";

export default {
  name: "AdminOutfit",
  components: {Card},

  setup() {
    const state = reactive({
      outfits: [],
    })

    const load = () => {
      axios.get("/api/main/outfits").then((res) => {
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