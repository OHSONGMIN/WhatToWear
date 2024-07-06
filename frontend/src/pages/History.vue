<template>
  <div>
    작성 내역<br>
    <div class="col" v-for="outfit in state.history" :key="outfit.id">
      <Card :outfit="outfit" @deleted="load"/>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {reactive} from "vue";
import Card from "@/components/Card.vue";

export default {
  components: {Card},
  setup() {
    const state = reactive({
      history: [],
    })

    const load = () => {
      axios.get("/api/history").then((res) => {
        state.history = res.data;
      })
    }

    load();

    // axios.get("/api/history").then(({data}) => {
    //   state.history = [];
    //
    //   for (let d of data) {
    //     if(d.items) {
    //       d.items = JSON.parse(d.items);
    //     }
    //     state.history.push(d);
    //   }
    // })

    return {state};
  }
}
</script>

<style scoped>

</style>