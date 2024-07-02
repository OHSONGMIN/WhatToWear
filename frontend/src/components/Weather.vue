<template>
  <div class="weather-container">
    <div class="left-weather">
      <div v-if="weatherData">

        <span>
          <span v-if="weatherData.sky <= 5"><i class="bi bi-brightness-high icon-large"></i></span>
          <span v-else-if="weatherData.sky <= 8"><i class="bi bi-cloud-sun"></i></span>
          <span v-else><i class="bi bi-clouds-fill"></i></span>
        </span>

        <span class="icon-large">&nbsp;{{ weatherData.currentTemp }}˚</span>

        <div>
          {{ weatherData.minTemp }}˚ / {{ weatherData.maxTemp }}˚&nbsp; 강수 확률 {{ weatherData.pop }}%
        </div>
      </div>
    </div>

    <div class="right-weather">
      <div v-if="address">
        <p>{{ address.city }} {{ address.borough }}&nbsp;<i class="bi bi-geo-alt-fill"></i></p>
      </div>
      <div v-else>
        <p>현재 위치를 찾을 수 없습니다.</p>
      </div>
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
.weather-container {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: white;
  padding: 3px 10px;
  /* border-radius: 15px; */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid #ddd;
}

.left-weather {
  display: inline-block;
}

.right-weather {
  position: absolute;
  top: 10px;
  right: 10px;
  display: inline-block;
  margin-left: auto;
}

.icon-large {
  font-size: 3em;
}
</style>
