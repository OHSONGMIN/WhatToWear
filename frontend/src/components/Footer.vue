<template>
  <footer>
    <nav>
      <router-link class="logo" to="/">Home</router-link> |
      <a href="https://beginnerdevdiary.tistory.com/" target='_blank'>Blog</a> |
      <a href="https://github.com/OHSONGMIN/WhatToWear" target='_blank'>Github</a>
    </nav>
    <p>
      <span>Email : dhthdals@gmail.com</span><br>
      <span>&copy; 2024. Oh Song Min</span>
    </p>
  </footer>
</template>

<script>
export default {
  name: 'Footer'
}
</script>

<style scoped>
footer {
  background: #F6F5F2;
  border-top: 1px solid #eee;
  padding: 25px;
  text-align: center;
  width: 100%;
}

</style>