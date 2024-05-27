<template>
  <div class="write">
    <div class="container">
      <h2>작성 폼</h2>
      <section>
        <div>
          <h3>외투</h3>
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

      <section>
        <div>
          <h3>상의</h3>
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

      <section>
        <div>
          <h3>하의</h3>
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

      <section>
        <div>
          <h3>액세서리</h3>
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

      <section>
        <div>
          <button
              @click="toggleSelection('review', `딱 좋아요`)"
              :class="{ selected: isSelected('review', `딱 좋아요`) }"
          >
            딱 좋아요
          </button>

          <button
              @click="toggleSelection('review', `쌀쌀해요`)"
              :class="{ selected: isSelected('review', `쌀쌀해요`) }"
          >
            쌀쌀해요
          </button>

          <button
              @click="toggleSelection('review', `더워요`)"
              :class="{ selected: isSelected('review', `더워요`) }"
          >
            더워요
          </button>
        </div>
      </section>
      <button @click="submit">작성하기</button>
    </div>
  </div>
</template>

<script>
import {reactive} from "vue";
import axios from "axios";
import router from "@/scripts/router";

export default {
  setup() {
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
    })


    axios.get("/api/items").then(({data}) => {
      //console.log(data);

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
        router.push({path: "/"});
      })
    }

    const toggleSelection = (category, itemId) => {
      state.form[category] = state.form[category] === itemId ? "" : itemId;
    };

    const isSelected = (category, itemId) => {
      return state.form[category] === itemId;
    };

    return {state, submit, toggleSelection, isSelected};
  }
}
</script>

<style scoped>
.selected {
  background-color: yellow;
}
</style>