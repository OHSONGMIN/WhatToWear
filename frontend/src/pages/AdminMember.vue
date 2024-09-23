<template>

  <div class="admin-container">
    <h2>회원 관리</h2>
    <div>
      <form>
        <input type="search"
               placeholder="이메일을 입력하세요"
               v-model="state.email"
        />
        <button @click="search()">검색</button>
      </form>
    </div>

    <div v-if="state.members">
      <h6>"{{ state.email }}"로 검색한 결과입니다</h6>

      <table>
        <tr v-for="member in state.members" :key="member.id">
          <td>{{ member.email }}</td>
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
      keyword: "",
      members: [],
    })

    const search = () => {
      axios.post("/api/admin/searchMember", state.form)
          .then((res) => {
            console.log(res);

            state.members = res.data;
          })
    }
    return {state, search};
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

</style>