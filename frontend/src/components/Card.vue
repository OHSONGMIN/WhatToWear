<template>
  <div class="card p-3 mb-3 shadow-sm">
    <div class="d-flex justify-content-between align-items-center mb-2">
      <div class="d-flex align-items-center">
        <span class="text-muted">{{ formatDate(outfit.regdate) }}</span> &nbsp; &nbsp;
        <span class="text-muted">{{ outfit.region }}</span>
      </div>

      <div class="d-flex justify-content-end align-items-center">
        <span class="review">
          <strong>
            <span v-if="outfit.review === '더워요'" class="review-hot"><i class="bi bi-emoji-angry"></i> 더워요</span>
            <span v-else-if="outfit.review === '좋아요'" class="review-good"><i class="bi bi-emoji-smile"></i> 좋아요</span>
            <span v-else class="review-cold"><i class="bi bi-emoji-grimace"></i> 추워요</span>
          </strong>
        </span>
        <span v-if="$store.state.account.id === outfit.memberId" class="cursor-pointer ms-3" @click="deleteOutfit(outfit.id)">
          <strong><i class="bi bi-x"></i></strong>
        </span>
      </div>
    </div>

    <div>
      <span v-for="item in parsedOutfit.overcoat" :key="item" class="badge custom-bg-overcoat badge-lg me-2 mb-2">{{ item }}</span>
      <span v-for="item in parsedOutfit.top" :key="item" class="badge custom-bg-top text-dark badge-lg me-2 mb-2">{{ item }}</span>
      <span v-for="item in parsedOutfit.bottom" :key="item" class="badge custom-bg-bottom badge-lg me-2 mb-2">{{ item }}</span>
      <span v-for="item in parsedOutfit.accessory" :key="item" class="badge custom-bg-accessory badge-lg me-2 mb-2">{{ item }}</span>
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
.card {
  background-color: #F6F5F2;
  border-radius: 15px;
  margin: 10px;
}

.badge-lg {
  font-size: 12px;
  padding: 0.5em 0.8em;
}

.cursor-pointer {
  cursor: pointer;
}

.custom-bg-overcoat {
  background-color: #EFBC9B;
  color: black;
}

.custom-bg-top {
  background-color: #FBF3D5;
  color: black;
}

.custom-bg-bottom {
  background-color: #D6DAC8;
  color: black;
}

.custom-bg-accessory {
  background-color: #9CAFAA;
  color: black;
}

.review-hot {
  color: #EB5353;
}

.review-good {
  color: #007D50;
}

.review-cold {
  color: #0871B1;
}

.review {
  margin-left: auto;
  text-align: right;
}
</style>