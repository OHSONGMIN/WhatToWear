<template>
  <div class="weather-container">
    <div class="left-weather">
      <div v-if="weatherData !== null">

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

      <div v-else class="text-muted">
        <i class="bi bi-exclamation-circle"></i> ERROR<br><br>
        날씨 정보를 불러올 수 없습니다. <br>
      </div>
    </div>

    <div class="right-weather">
      <div v-if="address !== null">
        <p>{{ address.city }} {{ address.borough }}&nbsp;<i class="bi bi-geo-alt-fill"></i></p>
      </div>
      <div v-else class="text-muted">
        <div class="spinner-border" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
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
      //required: false,
      default: null,
    },
    address: {
      type: Object,
      required: false,
      default: null,
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
