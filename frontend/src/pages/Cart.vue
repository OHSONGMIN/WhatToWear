<template>
  <div class="cart">
    카트 페이지
    <div class="container">
      <ul>
        <li v-for="(i, idx) in state.items" :key="idx">
          <span>{{i.name}}</span>
          <span>{{i.test}}</span>
          <i class="fa fa-trash" @click="remove(i.id)"></i>
        </li>
      </ul>
      <router-link to="/order" class="btn btn-primary">구입하기</router-link>
    </div>
  </div>

</template>

<script>
import axios from "axios";
import {reactive} from "vue";

export default {
  setup() {
    const state = reactive({
      items:[]
    })

    const load = () => {
      axios.get("/api/cart/items").then(({data}) => {
        console.log(data);
        state.items = data;
      })
    };

    const remove = (itemId) => {
      axios.delete(`/api/cart/items/${itemId}`).then(() => {
        //삭제하는 게 성공하면 다시 load
        load();
      })
    }

    load();

    return {state, remove};
  }
}
</script>

<style scoped>
.cart ul li i {
  float: right;
}

.cart .btn {
  padding: 30px 50px;
  font-size: 20px;
}
</style>