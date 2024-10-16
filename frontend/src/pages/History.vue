<template>
  <div class="my-title">리뷰 관리</div>
  <div>
    <div class="col" v-for="outfit in state.history" :key="outfit.id">
      <Card :outfit="outfit" @deleted="load"/>
    </div>

    <div class="pagination">
      <button @click="prevGroup" v-show="state.pageGroup !== 0">이전</button>

      <button v-for="page in pageInCurrentGroup"
              :key="page"
              @click="goToPage(page - 1)"
              :class="{ active: page - 1 === state.page}">
        {{ page }}
      </button>

      <button @click="nextGroup" v-show="!isLastGroup">다음</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {computed, reactive} from "vue";
import Card from "@/components/Card.vue";

export default {
  name: "History",
  components: {Card},
  setup() {
    const state = reactive({
      history: [],
      page: 0,        // 현재 페이지 번호
      size: 5,        // 페이지 당 리뷰 수
      totalPages: 1,  // 전체 페이지 수
      pageGroup: 0,   // 현재 페이지 그룹
      groupSize: 5,   // 한 그룹 당 페이지 번호 개수
    });

    // 현재 페이지 그룹에서 보여준 페이지 번호 계산
    // 0번 그룹 -> 1, 2, 3, 4, 5
    // 1번 그룹 -> 6, 7, 8, 9, 10
    const pageInCurrentGroup = computed(() => {
      const startPage = state.pageGroup * state.groupSize + 1;
      const endPage = Math.min(startPage + state.groupSize - 1, state.totalPages);
      return Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i)
    })

    // 마지막 그룹인지 확인 (true -> 다음 버튼 비활성화)
    const isLastGroup = computed(() => {
      return state.pageGroup >= Math.floor((state.totalPages - 1) / state.groupSize);
    });

    const load = () => {
      axios.get("/api/outfit/history", {
        params: {
          page: state.page,
          size: state.size,
        }
      }).then((res) => {
        // console.log ("히스토리: ", res.data.content);
        // console.log ("토딸: ", res.data.totalPages);

        state.history = res.data.content;
        state.totalPages = res.data.totalPages;
      })
    }

    // 처음 로딩될 때
    load();

    const goToPage = (page) => {
      state.page = page;
      load();
    }

    const nextGroup = () => {
      if (!isLastGroup.value) {
        state.pageGroup += 1;
        state.page = state.pageGroup * state.groupSize;
        load();
      }
    }

    const prevGroup = () => {
      if (state.pageGroup > 0) {
        state.pageGroup -= 1;
        state.page = state.pageGroup * state.groupSize;
        load();
      }
    }

    return { state, load, pageInCurrentGroup, isLastGroup, goToPage, nextGroup, prevGroup };
  }
}
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

button {
  padding: 5px 10px;
  margin: 0px 2px;
  background-color: #F6F5F2;
  border: 1px solid #ccc;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #ddd;
}

button.active {
  background-color: #b0ab99;
  color: white;
  border-color: #b0ab99;
}

.my-title {
  padding: 5px;
  margin: 5px;
  text-align: center;
}

/*
button:disabled {
  background-color: #e0e0e0;
  cursor: not-allowed;
}

button:disabled:hover {
  background-color: #e0e0e0;
}
*/
</style>