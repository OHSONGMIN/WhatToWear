<template>
  <div>
    {{ outfit.regdate }}
    {{ outfit.overcoat }}
    {{ outfit.top }}
    {{ outfit.bottom }}
    {{ outfit.accessory }}
    {{ outfit.review }}
    <div v-if="$store.state.account.id === outfit.memberId" @click="deleteOutfit(outfit.id)">삭제</div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Card",
  props: {
    outfit: Object
  },
  setup() {
    const deleteOutfit = (outfitId) => {
      axios.patch(`/api/outfit/${outfitId}`).then(() => {
        this.emit('deleted'); //deleted 이벤트를 발생
      })
    }

    return {deleteOutfit}
  }
}
</script>

<style scoped>

</style>