<template>
  <div>
    <Weather :weatherData="state.weatherData" :address="state.address"/>
    <!--  현재 위치 : {{ state.address.city }} {{ state.address.borough }}-->
  </div>

  <div class="col" v-for="outfit in state.outfits" :key="outfit.id">
    <Card :outfit="outfit" @deleted="load"/> <!-- outfit이라는 이름으로 outfit객체를 전달-->
    <!-- Card 컴포넌트에서 발생한 deleted 이벤트를 수신 -->
  </div>

  <div>
    <Write v-if="modalStatus" @sendClose="closeModal" @sendLoad="load" :address="state.address"></Write>
  </div>

  <div v-if="$store.state.account.id">
    <button type="button" class="fixed-button" @click="openModal"><i class="bi bi-pencil"></i></button>
  </div>
  <div v-else>
    <router-link class="fixed-button" to="/login">
      <i class="bi bi-pencil"></i>
    </router-link>
  </div>
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
      address: null,
      weatherData: null,
    })

    const load = () => {
      axios.get("/api/outfits").then((res) => {
        state.outfits = res.data;
      })
    }

    load();

    const getWeather = () => {
      navigator.geolocation.getCurrentPosition(position => {
        const lat = position.coords.latitude; //위도
        const lon = position.coords.longitude; //경도
        console.log("lat = " + lat + " lon = " + lon); //찍힘!!

        //Reverse Geocoding API 호출
        //axios.get(`https://nominatim.openstreetmap.org/reverse?format=json&lat=37.5677544&lon=127.0255591`) //서울 성동구
        axios.get(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lon}`)
            .then(({data}) => {
              //axios.get(`https://nominatim.openstreetmap.org/reverse?format=json&lat=37.514575&lon=127.0495556`).then(({data}) => {
              state.address = data.address;
              console.log("내 주소라고용..", state.address);
            })
            .catch((error) => {
              console.error("Reverse Geocoding API 호출 실패:", error);
              state.address = "위치 정보를 불러올 수 없습니다.";
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
          } else {

            state.weatherData = null;
            console.log(state.weatherData + "null 마잔요?11");

          }

          //console.log(JSON.stringify(res.data));
        })
            .catch((error) => {
              console.error("Weather API 호출 실패:", error);
              state.weatherData = null;
              console.log(state.weatherData + "null 마잔요?22");
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
.fixed-button {
  position: fixed;
  bottom: 20px; /* 화면의 아래에서 20px 위 */
  right: 20px; /* 화면의 오른쪽에서 20px 왼쪽 */
  width: 50px; /* 버튼의 너비 (높이와 동일하게 설정) */
  height: 50px; /* 버튼의 높이 */
  background-color: #635E4E; /* 버튼 색상 */
  color: white; /* 글자 색상 */
  border: none; /* 테두리 없음 */
  border-radius: 50%; /* 완벽한 원형 버튼 */
  display: flex; /* 중앙 정렬을 위한 Flexbox */
  align-items: center; /* 수직 중앙 정렬 */
  justify-content: center; /* 수평 중앙 정렬 */
  font-size: 24px; /* 아이콘 크기 (아이콘을 크게 보여줌) */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
  cursor: pointer; /* 마우스 커서 변경 */
  transition: background-color 0.3s; /* 배경색 변화 애니메이션 */
}

.fixed-button:hover {
  background-color: #B0AB99;
}

</style>