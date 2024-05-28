<template>
  <div class="col" v-for="outfit in state.outfits" :key="outfit.id">
    <Card :outfit="outfit" @deleted="load"/> <!-- outfit이라는 이름으로 outfit객체를 전달-->
    <!-- Card 컴포넌트에서 발생한 deleted 이벤트를 수신 -->
  </div>
  <Write v-if="modalStatus" @sendClose="closeModal" @sendLoad="load"></Write>
  <button type="button" @click="openModal">작성하기</button>
</template>

<script>
import axios from "axios";
import {reactive, ref} from "vue";
import Card from "@/components/Card.vue";
import Write from "@/components/Write.vue";

export default {
  name: "Home",
  components: {Card, Write},
  setup() {
    const state = reactive({
      outfits: []
    })

    const load = () => {
      axios.get("/api/outfits").then((res) => {
        state.outfits = res.data;
      })
    }

    load();

    const modalStatus = ref(false); //반응형

    const openModal = () => {
      modalStatus.value = true;
    }

    const closeModal = () => {
      modalStatus.value = false;
    }

    return {state, load, modalStatus, openModal, closeModal}
  }
}
</script>

<style scoped>

</style>