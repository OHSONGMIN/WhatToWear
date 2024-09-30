<template>
  <div class="admin-container">
    <h2>리뷰 관리</h2><br>
    <h6>조회 기간을 선택하세요.</h6>
    <div>
      <VueDatePicker
          v-model="dateRange"
          :range="true"
          locale="ko"
          :enable-time-picker="false"
          @update:model-value="handleDate"
          class="datepicker"
      />
      <div class="btn-class">
        <button @click.prevent="search()">검색</button>
      </div>
    </div>

    <div v-if="state.searchRange">
      <div class="searchResult">
        <p>{{ state.searchRange }} <br> 동안 작성된 리뷰입니다.</p>
      </div>

      <div v-if="state.outfits.length > 0">
        <div class="col" v-for="outfit in state.outfits" :key="outfit.id">
          <Card :outfit="outfit" @deleted="search()"/>
        </div>
      </div>
      <div v-else class="noResults">
        <br>
        <h6>검색 결과가 없습니다.</h6>
      </div>

    </div>


  </div>
</template>

<script>
import {reactive, ref} from "vue";
import axios from "axios";
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';
import Card from "@/components/Card.vue";

export default {
  name: "AdminOutfit",
  components: {
    VueDatePicker,
    Card,
  },
  setup() {
    const state = reactive({
      searchRange: "",
      outfits: [],
    })
    const dateRange = ref(null);
    const formattedDateRange = ref("");

    const handleDate = (modelDate) => {
      dateRange.value = modelDate;
      //formattedDateRange.value = dateRangeFormat(modelDate);
    }

    const search = () => {
      if (dateRange.value) {
        axios.get("/api/admin/searchOutfit", {
          params: {
            startDate: dateRange.value[0],
            endDate: dateRange.value[1]
          }
        })
            .then((res) => {
              state.outfits = res.data;
              state.searchRange = dateRangeFormat(dateRange.value);
            })
            .catch((error) => {
              console.error(error);
            });
      } else {
        console.error("날짜를 선택하세요.")
      }
    }

    const dateRangeFormat = (value) => {
      const startDate = value[0];
      const endDate = value[1];

      if (startDate && endDate) {
        const format = (date) => {
          const year = date.getFullYear();
          const month = (date.getMonth() + 1).toString().padStart(2, "0");
          const day = date.getDate().toString().padStart(2, "0");
          return `${year}-${month}-${day}`;
        };

        return `${format(startDate)}~${format(endDate)}`;
      }

      return "";
    };
    return {state, search, dateRange, handleDate, dateRangeFormat, formattedDateRange};
  }
}
</script>

<style scoped>
.admin-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 50px;
}


button {
  cursor: pointer;
}

p {
  font-size: 20px;
  margin: 20px;
}

.btn-class {
  text-align: center;
}

.searchResult {
  text-align: center;
  color: #635E4E;
}

.noResults {
  text-align: center;
  color: #635E4E;
}

</style>