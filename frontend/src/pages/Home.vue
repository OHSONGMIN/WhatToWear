<template>
  This is App.vue~~~~~~~~~
  <div class="col" v-for="i in 12" :key="i">
    <Card/>
  </div>
</template>

<script>
import Card from "@/components/Card.vue";

export default {
  name: "Home",
  components: {Card}
}
</script>

<style scoped>

</style>