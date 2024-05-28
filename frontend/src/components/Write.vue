<template>
  <div class="write">
    <div class="container modal-overlay">
      <div class="modal-content">
        <main>
          <h2>작성 폼</h2>

          <button type="button" @click="closeModal">닫기</button>
          <!-- 외투 버튼들 -->
          <section>
            <h3>외투</h3>
            <div class="button-group">
              <button
                  v-for="item in state.overcoatItems"
                  :key="item.id"
                  @click="toggleSelection('overcoat', item.id)"
                  :class="{ selected: isSelected('overcoat', item.id) }"
              >
                {{ item.name }}
              </button>
            </div>
          </section>

          <!-- 상의 버튼들 -->
          <section>
            <h3>상의</h3>
            <div class="button-group">
              <button
                  v-for="item in state.topItems"
                  :key="item.id"
                  @click="toggleSelection('top', item.id)"
                  :class="{ selected: isSelected('top', item.id) }"
              >
                {{ item.name }}
              </button>
            </div>
          </section>

          <!-- 하의 버튼들 -->
          <section>
            <h3>하의</h3>
            <div class="button-group">
              <button
                  v-for="item in state.bottomItems"
                  :key="item.id"
                  @click="toggleSelection('bottom', item.id)"
                  :class="{ selected: isSelected('bottom', item.id) }"
              >
                {{ item.name }}
              </button>
            </div>
          </section>

          <!-- 액세서리 버튼들 -->
          <section>
            <h3>액세서리</h3>
            <div class="button-group">
              <button
                  v-for="item in state.accessoryItems"
                  :key="item.id"
                  @click="toggleSelection('accessory', item.id)"
                  :class="{ selected: isSelected('accessory', item.id) }"
              >
                {{ item.name }}
              </button>
            </div>
          </section>

          <!-- 리뷰 버튼들 -->
          <section>
            <h3>리뷰</h3>
            <div class="button-group">
              <button
                  @click="toggleSelection('review', '딱 좋아요')"
                  :class="{ selected: isSelected('review', '딱 좋아요') }"
              >
                딱 좋아요
              </button>
              <button
                  @click="toggleSelection('review', '쌀쌀해요')"
                  :class="{ selected: isSelected('review', '쌀쌀해요') }"
              >
                쌀쌀해요
              </button>
              <button
                  @click="toggleSelection('review', '더워요')"
                  :class="{ selected: isSelected('review', '더워요') }"
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
      form: {
        overcoat: "",
        top: "",
        bottom: "",
        accessory: "",
        review: ""
      },
    });

    axios.get("/api/items").then(({data}) => {
      state.overcoatItems = data.filter(item => item.category === 1);
      state.topItems = data.filter(item => item.category === 2);
      state.bottomItems = data.filter(item => item.category === 3);
      state.accessoryItems = data.filter(item => item.category === 4);
    }).catch(error => {
      console.error("데이터를 불러오는 중 에러 발생:", error);
    });

    const submit = () => {
      const args = JSON.parse(JSON.stringify(state.form));
      axios.post("/api/write", args).then(() => {
        alert("작성 완료!!");
        emit(`sendClose`);
        emit(`sendLoad`);
      });
    };

    const toggleSelection = (category, itemId) => {
      state.form[category] = state.form[category] === itemId ? "" : itemId;
    };

    const isSelected = (category, itemId) => {
      return state.form[category] === itemId;
    };

    const closeModal = () => {
      emit('sendClose');
    }

    return {state, submit, toggleSelection, isSelected, closeModal};
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
  margin-top: 10px;
  padding: 10px;
  border: none;
  border-radius: 5px;
  background-color: #666;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #444;
}

.button-group button.selected {
  background-color: yellowgreen;
  color: white;
}
/*
div {
  box-sizing: border-box;
}


.black-bg {
  width: 100%; height: 100%;
  background: rgba(0, 0, 0, 0.5);
  position: fixed; padding: 20px;
}

.white-bg {
  width: 100%;
  background: white;
  border-radius: 8px;
  padding: 20px;
}


.write {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

h2 {
  margin-bottom: 20px;
  color: #333;
}

section {
  margin-bottom: 20px;
}

h3 {
  margin-bottom: 10px;
  color: #555;
}

.button-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

button {
  padding: 8px 16px;
  border: 1px solid #888;
  background-color: #eee;
  color: #333;
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s, color 0.3s;
}

button.selected {
  background-color: yellowgreen;
  color: #fff;
}

.submit-button {
  margin-top: 20px;
  padding: 8px 16px;
  background-color: #666;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.submit-button:hover {
  background-color: #444;
} */
</style>