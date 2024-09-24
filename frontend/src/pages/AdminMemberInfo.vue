<template>
  <div class="admin-container">
    <h4>회원 정보 관리</h4>
    <div v-if="state.member" class="admin-member-info">
      <table>
        <tr>
          <td>이메일</td>
          <td>{{ state.member.email }}</td>
        </tr>
        <tr>
          <td>가입일자</td>
          <td>{{ state.member.regdate.slice(0, 10) }}</td>
        </tr>
        <tr>
          <td>탈퇴여부</td>
          <td>
            {{ state.member.delStatus }}
            <button @click="forcedDelete()">강제 탈퇴</button>
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
import {useRoute} from "vue-router";
import axios from "axios";
import {reactive} from "vue";

export default {
  name: "AdminMemberInfo",
  setup() {
    const route = useRoute();
    const state = reactive({
      member: "",
    })

    // 이메일로 회원 상세 정보 조회
    const getMemberInfo = (email) => {
      axios.get(`/api/admin/memberInfo`, {
        params: {email}
      })
          .then((res) => {
            state.member = res.data;
          })
          .catch((error) => {
            console.error("회원 정보 불러오는 중 에러 발생: ", error);
          });
    };

    getMemberInfo(route.params.email);

    const forcedDelete = () => {
      alert("탈퇴할래?");

    }

    return { state, forcedDelete };
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

.admin-member-info {
  margin-top: 20px;
}

table {
  border-collapse: collapse;
  margin: 25px 0;
  border: 1px solid #D5D5D5;
  min-width: 250px;
}

td {
  padding: 5px 5px;
  border-right: 1px solid #D5D5D5;
}
</style>