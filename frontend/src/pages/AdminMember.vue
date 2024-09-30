<template>
  <div class="admin-container">
    <h2>회원 관리</h2>
    <div>
      <form>
        <input type="search"
               placeholder="이메일을 입력하세요"
               v-model="state.email"
        />
        <button @click.prevent="search()">검색</button>
      </form>
    </div>

    <!-- 검색어가 있으면 -->
    <div v-if="state.searchKeyword" class="searchResult">
      <h6>"{{ state.searchKeyword }}"로 검색한 결과입니다</h6>

      <!-- 검색 결과가 1개 이상 -->
      <table v-if="state.members.length > 0" class="styled-table">
        <thead>
        <tr>
          <th>이메일</th>
          <th>탈퇴여부</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="member in state.members" :key="member.id">
          <td>
            <router-link :to="{ name: `AdminMemberInfo`, params: { email: member.email }}">
              {{ member.email }}
            </router-link>
          </td>
          <td>{{ member.delStatus === 0 ? member.delStatus : "탈퇴"}}</td>
        </tr>
        </tbody>
      </table>

      <!-- 검색 결과가 0개 -->
      <div v-else>검색 결과가 존재하지 않습니다.</div>
    </div>
  </div>
</template>

<script>
import { reactive } from "vue";
import axios from "axios";

export default {
  name: "AdminMember",
  setup() {
    const state = reactive({
      email: "",
      members: [],
      searchKeyword: ""
    })

    const search = () => {
      axios.get("/api/admin/searchMember", {
        params: { email: state.email }
      })
          .then((res) => {
            state.members = res.data;
            state.searchKeyword = state.email;
          })
          .catch((error) => {
            console.error(error);
          });
    }
    return { state, search };
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

p {
  font-size: 20px;
  margin: 20px;
}

.searchResult {
  text-align: center;
}

table {
  border-collapse: collapse;
  margin: 25px 0;
  border: 1px solid #D5D5D5;
  min-width: 300px;
}

.styled-table thead tr {
  background-color: #D5D5D5;
  font-size: 15px;
}

.styled-table th, .styled-table td {
  padding: 10px 5px;
  border: 1px solid #D5D5D5;
}
</style>