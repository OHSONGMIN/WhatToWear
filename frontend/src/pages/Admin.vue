<template>

  <div class="admin-container">
    <h2>관리자 페이지</h2>
    <div class="admin-actions">
      <router-link class="btn btn-primary" to="/adminMember">회원 관리</router-link>
      <router-link class="btn btn-primary" to="/adminOutfit">리뷰 관리</router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "Admin",

}
</script>

<style scoped>
.admin-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 50px;
}

.btn-primary {
  display: block;
  padding: 10px 20px;
  font-size: 18px;
  cursor: pointer;
  background-color: #B0AB99;
  border: none;
  margin: 20px;
}
</style>