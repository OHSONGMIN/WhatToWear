<template>

  <Weather v-if="state.weatherData" :weatherData="state.weatherData" :address="state.address"/>
<!--  현재 위치 : {{ state.address.city }} {{ state.address.borough }}-->

  <div class="col" v-for="outfit in state.outfits" :key="outfit.id">
    <Card :outfit="outfit" @deleted="load"/> <!-- outfit이라는 이름으로 outfit객체를 전달-->
    <!-- Card 컴포넌트에서 발생한 deleted 이벤트를 수신 -->
  </div>
  <Write v-if="modalStatus" @sendClose="closeModal" @sendLoad="load"></Write>
  <button type="button" @click="openModal">작성하기</button>
</template>

<script>
import axios from "axios";
import {reactive, ref} from "vue";
import Card from "@/components/Card.vue";
import Write from "@/components/Write.vue";
import Weather from "@/components/Weather.vue";

export default {
  name: "Home",
  components: {Card, Write, Weather},
  setup() {
    const state = reactive({
      outfits: [],
      address: {
        //borough: "", //신봉동
        //city: "", //용인시
      },
      weatherData: null,
    })

    const load = () => {
      axios.get("/api/outfits").then((res) => {
        console.log(res.data);
        state.outfits = res.data;
      })
    }

    load();

    const getWeather = () => {
      navigator.geolocation.getCurrentPosition(position => {
        const lat = position.coords.latitude; //위도
        const lon = position.coords.longitude; //경도
        console.log("lat = " + lat + "lon = " + lon); //찍힘!!

        //Reverse Geocoding API 호출
        axios.get(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lon}`).then(({data}) => {
          state.address = data.address;
        })

        axios.get(`/api/weather?lat=${lat}&lon=${lon}`).then((res) => {
          /* POP, SKY, TMP 1시간 단위로 출력됨
           * POP	강수확률	% ★
           * SKY	하늘상태	코드값 ★
           * TMP	1시간 기온	℃ ★
           * TMN	일 최저기온	℃
           * TMX	일 최고기온	℃
           */
          if (res.status === 200) {
            //console.log(res.data.response.body);
            const weatherItems = res.data.response.body.items.item;
            const weather = {};

            weatherItems.forEach(item => {

              const category = item.category;
              // const fcstTime = item.fcstTime;
              // const fcstDate = item.fcstDate;
              // const baseTime = item.baseTime;
              // const baseDate = item.baseTime;

              if (category === "TMP") {
                weather.currentTemp = item.fcstValue;
              }
              if (category === "TMN") {
                weather.minTemp = item.fcstValue;
              }
              if (category === "TMX") {
                weather.maxTemp = item.fcstValue;
              }
              if (category === "POP") {
                weather.pop = item.fcstValue;
              }
              if (category === "SKY") {
                weather.sky = item.fcstValue;
              }
            });

            state.weatherData = weather;
          }
          console.log(JSON.stringify(res.data));
        })
      })
    };

    getWeather();

    const modalStatus = ref(false); //반응형

    const openModal = () => {
      modalStatus.value = true;
    }

    const closeModal = () => {
      modalStatus.value = false;
    }

    return {state, load, modalStatus, openModal, closeModal, getWeather}
  }
}
</script>

<style scoped>

</style>