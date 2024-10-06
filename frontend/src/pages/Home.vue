<template>
  <div class="scroll-container">
    <div>
      <Weather :weatherData="state.weatherData" :address="state.address"/>
      <!--  현재 위치 : {{ state.address.city }} {{ state.address.borough }}-->
    </div>

    <div class="col" v-for="outfit in outfits" :key="outfit.id">
      <Card :outfit="outfit"/>
      <!-- Card 컴포넌트에서 발생한 deleted 이벤트를 수신 -->
    </div>
    <infinite-loading @infinite="loadMoreData" ref="infiniteLoading"></infinite-loading>
<!--    <InfiniteLoading-->
<!--        ref="infiniteLoading"-->
<!--        @infinite="infiniteHandler"-->
<!--        spinner="spinner"-->
<!--        target=".scroll-container"></InfiniteLoading>-->
    <div>
      <Write v-if="modalStatus" @sendClose="closeModal" @sendLoad="load" :address="state.address"></Write>
    </div>

    <div v-if="isLoggedIn">
      <button type="button" class="fixed-button" @click="openModal"><i class="bi bi-pencil"></i></button>
    </div>
    <div v-else>
      <router-link class="fixed-button" to="/login">
        <i class="bi bi-pencil"></i>
      </router-link>
    </div>
    <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
  </div>

</template>

<script>
import axios from "axios";
import {reactive, ref} from "vue";
import Card from "@/components/Card.vue";
import Write from "@/components/Write.vue";
import Weather from "@/components/Weather.vue";
import {mapGetters} from "vuex";
import {InfiniteLoading} from "vue3-infinite-loading"

export default {
  name: "Home",
  computed: {
    ...mapGetters([`isLoggedIn`]),
  },
  components: {Card, Write, Weather, InfiniteLoading},

  // data() {
  //   return {
  //     page: 1,
  //     outfits: [],
  //   }
  // },
  // methods: {
  //   infiniteHandler($state) {
  //     console.log("무한 스크롤 시작");
  //
  //     axios.get("/api/main/outfits", {
  //       params: {
  //         page: this.page,
  //         perPage: 5 //나중에 올리자
  //       },
  //     })
  //         .then((res) => {
  //           setTimeout(() => {
  //             const data = res.data;
  //
  //             console.log("불러올수" + data);
  //
  //             if (data.length) {
  //               this.page++;
  //               this.outfits.push(...data);
  //               $state.loaded();
  //             } else {
  //               console.log("더 이상 데이터가 없음");
  //               $state.complete();
  //             }
  //           }, 1000)
  //         });
  //   },
  // },
  setup() {
    const state = reactive({
      //outfits: [],
      address: null,
      weatherData: null,
    })

    const outfits = ref([]);
    const page = ref(1);

    const loadMoreData = async ($state) => {

      console.log("무한 스크롤 시작");

      try {
        const response = await axios.get(`/api/main/outfits`, {
          params: {
            page: page.value,
            perPage: 5
          }
        })

        const newItems = response.data;

        if (newItems.length > 0) {
          outfits.value = [...outfits.value, ...newItems];
          page.value++;
          $state.loaded();
        } else {
          $state.complete();
        }
      } catch (error) {
        console.error("API 요청 실패:", error);
        $state.error();
      }
    }
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
              //console.log("내 주소라고용..", state.address);
            })
            .catch((error) => {
              console.error("Reverse Geocoding API 호출 실패:", error);
              state.address = "위치 정보를 불러올 수 없습니다.";
            })

        axios.get(`/api/main/weather?lat=${lat}&lon=${lon}`).then((res) => {
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
            //console.log(state.weatherData + "null 마잔요?11");

          }

          //console.log(JSON.stringify(res.data));
        })
            .catch((error) => {
              console.error("Weather API 호출 실패:", error);
              state.weatherData = null;
              //console.log(state.weatherData + "null 마잔요?22");
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

    return {state, modalStatus, openModal, closeModal, getWeather, loadMoreData}
  },

  mounted() {

    const container = document.querySelector('.scroll-container');
    console.log('scrollHeight:', container.scrollHeight);
    console.log('clientHeight:', container.clientHeight);

    if (container.scrollHeight > container.clientHeight) {
      console.log('스크롤 가능');
    } else {
      console.log('스크롤 불가능');
    }
    //컴포넌트가 처음 로드될 때 infiniteHandler 호출
    // this.infiniteHandler({
    //   loaded: () => {
    //   },
    //   complete: () => {
    //   },
    //   //loaded: this.$refs.infiniteLoading.loaded,
    //   //complete: this.$refs.infiniteLoading.complete,
    // });
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

.scroll-container {
  height: 100vh; /* 필요한 경우 */
  overflow-y: auto;
}

</style>