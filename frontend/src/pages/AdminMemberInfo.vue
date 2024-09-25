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
          <td class="withdrawBtn">
            {{ state.member.delStatus === 0 ? state.member.delStatus : "탈퇴" }}
            <button @click="adminWithdraw(state.member.email)">강제 탈퇴</button>
          </td>
        </tr>
      </table>
    </div>

    <button @click="getMemberOutfits(state.member.email)">작성한 리뷰 목록</button>
  </div>
  <div v-if="state.outfits">
    <div class="col" v-for="outfit in state.outfits" :key="outfit.id">
      <Card :outfit="outfit" @deleted="getMemberOutfits(state.member.email)"/>
    </div>
  </div>
</template>

<script>
import {useRoute} from "vue-router";
import axios from "axios";
import {reactive} from "vue";
import Card from "@/components/Card.vue";

export default {
  name: "AdminMemberInfo",
  components: {Card},
  setup() {
    const route = useRoute();
    const state = reactive({
      member: "",
      outfits: [],
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

    const adminWithdraw = (email) => {
      const confirmed = confirm("강제로 탈퇴시키겠습니까?");

      if (confirmed) {

        if (state.member.delStatus === 0) {
          axios.patch(`/api/admin/withdraw/${email}`)
              .then(() => {
                alert("탈퇴 완료");

                //업데이트
                state.member.delStatus = 1;
              })
              .catch((error) => {
                console.error("탈퇴 실패: " + error);
              })
        } else {
          alert("이미 탈퇴된 회원입니다.")
        }
      } else {
        alert("취소되었습니다.");
      }
    }

    const getMemberOutfits = (email) => {
      axios.get(`/api/admin/getOutfits/${email}`)
          .then((res) => {
            state.outfits = res.data;
            console.log(res);
          })
          .catch((error) => {
            console.error("리뷰 목록을 불러오지 못했습니다: ", error);
          })
    }

    return {state, adminWithdraw, getMemberOutfits};
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

.withdrawBtn {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>