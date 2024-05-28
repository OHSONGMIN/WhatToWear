<template>
  {{item.regdate}}
  {{item.overcoat}}
  {{item.top}}
  {{item.bottom}}
  {{item.accessory}}
  {{item.review}}
</template>

<script>
export default {
  name: "Card",
  props: {
    item: Object
  },
  setup() {
    // const addToCart = (itemdId) => {
    //   axios.post(`/api/cart/items/${itemdId}`).then(() => {
    //     console.log("success");
    //   })
    // };
    //
    // return {addToCart}
  }
}
</script>

<style scoped>

</style>