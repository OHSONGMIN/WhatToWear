<template>
  Card........
  {{item.name}}
  <button class="btn btn-primary" @click="addToCart(item.id)">
    <i class="fa fa-shopping-cart" aria-hidden="true"></i>
  </button>
</template>

<script>
import axios from "axios";

export default {
  name: "Card",
  props: {
    item: Object
  },
  setup() {
    const addToCart = (itemdId) => {
      axios.post(`/api/cart/items/${itemdId}`).then(() => {
        console.log("success");
      })
    };

    return {addToCart}
  }
}
</script>

<style scoped>

</style>