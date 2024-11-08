<template>
  <div class="admin-container">
    <h2>아이템 관리</h2>
    <div>
      <form>
        <div>
          <label for="category">카테고리 &nbsp;</label>
          <select v-model="state.form.category" id="category">
            <option value="1">외투</option>
            <option value="2">상의</option>
            <option value="3">하의</option>
            <option value="4">액세서리</option>
          </select>
        </div>

        <div class="input-name">
          <label for="name">저장할 이름 &nbsp;</label>
          <input type="text" v-model="state.form.name" id="name" placeholder="예: 반팔 티셔츠"/>
        </div>

        <div class="save-button">
          <button @click.prevent="saveItem()" style="width: 180px">아이템 저장</button>
        </div>
      </form>
    </div>

    <hr>

    <div>
      <table style="width:100%">
        <tr>
          <th style="width:26%">카테고리</th>
          <th style="width:35%">이름</th>
          <th style="width:16%">상태</th>
          <th style="width:20%">활/비</th>
        </tr>

        <tr v-for="item in state.items" :key="item.id">
          <td style="text-align: center">
            <span v-if="item.category === 1">외투</span>
            <span v-else-if="item.category === 2">상의</span>
            <span v-else-if="item.category === 3">하의</span>
            <span v-else>액세서리</span>
          </td>
          <td>&nbsp;{{ item.name }}</td>
          <td style="text-align: center">
            <span v-if="item.activeStatus === 1">O</span>
            <span v-else>X</span>
          </td>
          <td>
            <button @click.prevent="changeStatus(item.id)" class="change-btn">전환</button>
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
import {reactive} from "vue";
import axios from "axios";

export default {
  name: "AdminMember",
  setup() {
    const state = reactive({
      items: [],
      form: {
        category: "",
        name: ""
      }
    })

    const saveItem = () => {

      if (state.form.category === "") {
        window.alert("카테고리를 선택해주세요.");
        return;
      }

      if (state.form.name === "") {
        window.alert("저장할 이름을 작성해주세요.");
        return;
      }

      axios.post("/api/admin/item", state.form)
          .then(() => {
            window.alert("아이템 저장 완료.");

            load();
          })
          .catch((error) => {
            console.error("아이템 저장 실패:", error);
          })
    }

    const load = () => {
      axios.get("/api/admin/item")
          .then((res) => {
            state.items = res.data;
          })
          .catch((error) => {
            console.error("관리자 아이템 호출 실패:", error);
          })
    }

    load();

    const changeStatus = (itemId) => {
      axios.patch(`/api/admin/item/${itemId}`)
          .then(() => {
            load();
          })
          .catch((error) => {
            console.error("상태 변경 실패: ", error);
          })
    }
    return {state, saveItem, load, changeStatus}
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

form {
  display: block;
  margin: 20px;
}

button {
  cursor: pointer;
}

.save-button {
  display: flex;
  justify-content: center;
}

input {
  height: 25px;
}

.input-name {
  margin: 10px 0;
}

label {
  width: 105px;
  text-align: right;
}

table {
  border-collapse: collapse;
  margin: 25px 0;
  border: 1px solid #635E4E;
  min-width: 330px;
}

th, td {
  border-collapse: collapse;
  border: 1px solid #C7C6C4;
}

th {
  text-align: center;
}

tr {
  height: 40px;
}

hr {
  width: 330px;
  border: 1px solid #635E4E;
}

.change-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: auto;
}
</style>