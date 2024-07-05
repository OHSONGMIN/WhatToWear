<template>
<div>
  주문내역.....!!
</div>
</template>

<script>
import axios from "axios";
import {reactive} from "vue";

export default {
  setup() {
    const state = reactive({
      history: [],
    })

    axios.get("/api/history").then(({data}) => {
      state.history = [];

      for (let d of data) {
        if(d.items) {
          d.items = JSON.parse(d.items);
        }
        state.history.push(d);
      }
    })

    return {state};
  }
}
</script>

<style scoped>

</style>