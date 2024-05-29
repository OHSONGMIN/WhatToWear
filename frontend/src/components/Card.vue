<template>
  <div class="card p-3 mb-3 shadow-sm">
    <div class="d-flex justify-content-between align-items-center mb-2">
      <div>
        <span class="text-muted">{{ formatDate(outfit.regdate) }}</span> &nbsp;
        <strong>{{ outfit.review }}</strong>
      </div>
      <span v-if="$store.state.account.id === outfit.memberId" class="cursor-pointer" @click="deleteOutfit(outfit.id)">
        <i class="fa fa-times"></i>
      </span>
    </div>

    <div>
      <span v-for="item in parsedOutfit.overcoat" :key="item" class="badge bg-primary badge-lg me-2 mb-2">{{ item }}</span>
      <span v-for="item in parsedOutfit.top" :key="item" class="badge bg-warning text-dark badge-lg me-2 mb-2">{{ item }}</span>
      <span v-for="item in parsedOutfit.bottom" :key="item" class="badge bg-success badge-lg me-2 mb-2">{{ item }}</span>
      <span v-for="item in parsedOutfit.accessory" :key="item" class="badge bg-secondary badge-lg me-2 mb-2">{{ item }}</span>
    </div>
  </div>
</template>
<!--<template>-->
<!--  <div>-->
<!--    <div>-->
<!--      {{ formatDate(outfit.regdate) }} &nbsp; {{ outfit.review }}-->
<!--      <span v-if="$store.state.account.id === outfit.memberId" @click="deleteOutfit(outfit.id)">삭제</span>-->
<!--    </div>-->


<!--    <span v-for="item in parsedOutfit.overcoat" :key="item">-->
<!--        <span class="badge bg-primary">{{ item }}</span>-->
<!--      </span>-->

<!--    <span v-for="item in parsedOutfit.top" :key="item">-->
<!--        <span class="badge bg-warning text-dark">{{ item }}</span>-->
<!--      </span>-->

<!--    <span v-for="item in parsedOutfit.bottom" :key="item">-->
<!--        <span class="badge bg-success">{{ item }}</span>-->
<!--      </span>-->

<!--    <span v-for="item in parsedOutfit.accessory" :key="item">-->
<!--        <span class="badge bg-secondary">{{ item }}</span>-->
<!--      </span>-->

<!--  </div>-->
<!--</template>-->

<script>
import axios from "axios";
import {format} from "date-fns";

export default {
  name: "Card",
  props: {
    outfit: Object
  },

  setup(props, {emit}) {
    const deleteOutfit = (outfitId) => {
      axios.patch(`/api/outfit/${outfitId}`).then(() => {
        emit('deleted'); //deleted 이벤트를 발생
      })
    }

    const formatDate = (dateString) => {
      return format(new Date(dateString), "yy/MM/dd HH:mm");
    };

    const parseOutfit = (outfit) => {
      return {
        overcoat: JSON.parse(outfit.overcoat),
        top: JSON.parse(outfit.top),
        bottom: JSON.parse(outfit.bottom),
        accessory: JSON.parse(outfit.accessory)
      };
    };

    const parsedOutfit = parseOutfit(props.outfit);

    return {deleteOutfit, formatDate, parsedOutfit}
  }
}
</script>

<style scoped>
.badge-lg {
  font-size: 12px;
  padding: 0.5em 0.8em;
}

.cursor-pointer {
  cursor: pointer;
}
</style>