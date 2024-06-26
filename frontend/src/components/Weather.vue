<template>
  <div>
    <div v-if="weatherData">
      <p>현재 기온: {{ weatherData.currentTemp }}</p>
      <p>최저 기온: {{ weatherData.minTemp }}</p>
      <p>최고 기온: {{ weatherData.maxTemp }}</p>
      <p>강수 확률: {{ weatherData.pop }}</p>
      <p>하늘 상태: {{ weatherData.sky }}</p>
    </div>

    <div v-if="address">
      <p>현재 지역: {{ address.city }} {{ address.borough }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "Weather",
  props: {
    weatherData: {
      type: Object,
      required: false,
    },
    address: {
      type: Object,
      required: false,
    }
  }

  }
</script>

<style scoped>

</style>