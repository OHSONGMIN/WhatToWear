<template>
  <div class="write">
    <div class="container modal-overlay">
      <div class="modal-content">
        <main>
          <span @click="closeModal"><i class="fa fa-times"></i></span>
          <!-- 외투 -->
          <section>
            외투
            <div class="button-group">
              <button
                  v-for="item in state.overcoatItems"
                  :key="item.id"
                  @click="toggleSelection('selectedOvercoat', item.name)"
                  :class="{ selected: isSelected('selectedOvercoat', item.name) }"
              >
                {{ item.name }}
              </button>
            </div>
          </section>

          <!-- 상의 -->
          <section>
            상의
            <div class="button-group">
              <button
                  v-for="item in state.topItems"
                  :key="item.id"
                  @click="toggleSelection('selectedTop', item.name)"
                  :class="{ selected: isSelected('selectedTop', item.name) }"
              >
                {{ item.name }}
              </button>
            </div>
          </section>

          <!-- 하의 -->
          <section>
            하의
            <div class="button-group">
              <button
                  v-for="item in state.bottomItems"
                  :key="item.id"
                  @click="toggleSelection('seletedBottom', item.name)"
                  :class="{ selected: isSelected('seletedBottom', item.name) }"
              >
                {{ item.name }}
              </button>
            </div>
          </section>

          <!-- 액세서리 -->
          <section>
            액세서리
            <div class="button-group">
              <button
                  v-for="item in state.accessoryItems"
                  :key="item.id"
                  @click="toggleSelection('seletedAcc', item.name)"
                  :class="{ selected: isSelected('seletedAcc', item.name) }"
              >
                {{ item.name }}
              </button>
            </div>
          </section>

          <hr>
          <!-- 리뷰 버튼들 -->
          <section>
            <div class="button-group">
              <button
                  @click="toggleSelectionOne('딱 좋아요')"
                  :class="{ selected: isSelectedOne('딱 좋아요') }"
              >
                딱 좋아요
              </button>
              <button
                  @click="toggleSelectionOne('쌀쌀해요')"
                  :class="{ selected: isSelectedOne('쌀쌀해요') }"
              >
                쌀쌀해요
              </button>
              <button
                  @click="toggleSelectionOne('더워요')"
                  :class="{ selected: isSelectedOne('더워요') }"
              >
                더워요
              </button>
            </div>
          </section>

          <!-- 제출 버튼 -->
          <button class="submit-button" @click="submit">작성하기</button>
        </main>
      </div>
    </div>
  </div>
</template>

<script>
import {reactive} from "vue";
import axios from "axios";

export default {
  setup(props, {emit}) {
    const state = reactive({
      overcoatItems: [],
      topItems: [],
      bottomItems: [],
      accessoryItems: [],
      selectedOvercoat: [],
      selectedTop: [],
      seletedBottom: [],
      seletedAcc: [],
      form: {
        overcoat: "",
        top: "",
        bottom: "",
        accessory: "",
        review: ""
      },
    });

    axios.get("/api/items").then(({data}) => {
      console.log("응답 데이터 확인", data);
      state.overcoatItems = data.filter(item => item.category === 1);
      state.topItems = data.filter(item => item.category === 2);
      state.bottomItems = data.filter(item => item.category === 3);
      state.accessoryItems = data.filter(item => item.category === 4);
    }).catch(error => {
      console.error("데이터를 불러오는 중 에러 발생:", error);
    });

    const submit = () => {
      const args = JSON.parse(JSON.stringify(state.form));
      args.overcoat = JSON.stringify(state.selectedOvercoat);
      args.top = JSON.stringify(state.selectedTop);
      args.bottom = JSON.stringify(state.seletedBottom);
      args.accessory = JSON.stringify(state.seletedAcc);

      axios.post("/api/write", args).then(() => {
        alert("작성 완료!!");
        emit(`sendClose`);
        emit(`sendLoad`);
      }).catch(error => {
        console.error("작성 중 에러 발생:", error);
      });
    };

    //다중 선택
    const toggleSelection = (category, itemName) => {
      const index = state[category].indexOf(itemName);
      if (index === -1) {
        state[category].push(itemName);
      } else {
        state[category].splice(index, 1); //요소를 제거
      }
    };

    //다중 선택
    const isSelected = (category, itemName) => {
      return state[category].includes(itemName);
    }

    //단일 선택
    const toggleSelectionOne = (how) => {
      state.form.review = state.form.review === how ? "" : how;
    }

    //단일 선택
    const isSelectedOne = (how) => {
      return state.form.review === how;
    };

    const closeModal = () => {
      emit('sendClose');
    }

    return {state, submit, toggleSelection, isSelected, closeModal, isSelectedOne, toggleSelectionOne};
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 90%;
  max-width: 600px;
}

button {
  margin-top: 5px;
  padding: 8px;
  border: none;
  border-radius: 5px;
  background-color: #666;
  color: white;
  cursor: pointer;
  font-size: 0.875em;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #444;
}

.button-group {
  display: flex;
  flex-wrap: wrap;
}

.button-group button {
  margin: 5px;
}

.button-group button.selected {
  background-color: yellowgreen;
  color: white;
}

.close-button {
  float: right;
  background-color: red;
}

.submit-button {
  margin-top: 15px;
  background-color: blue;
}
</style>