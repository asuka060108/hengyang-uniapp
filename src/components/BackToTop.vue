<script setup>
defineProps({
  visible: { type: Boolean, default: false },
});

function toTop() {
  uni.pageScrollTo({ scrollTop: 0, duration: 280 });
}
</script>

<template>
  <view v-if="visible" class="back-top" hover-class="back-top--hover" @tap="toTop">
    <text class="back-top__arrow">↑</text>
    <text class="back-top__label">TOP</text>
  </view>
</template>

<style scoped>
.back-top {
  position: fixed;
  right: 16px;
  bottom: calc(66px + env(safe-area-inset-bottom));
  z-index: 30;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 42px;
  height: 42px;
  color: #f8f2e5;
  background: rgba(31, 107, 86, 0.92);
  border-radius: 50%;
  box-shadow: 0 4px 14px rgba(15, 30, 23, 0.22);
}

.back-top--hover {
  opacity: 0.75;
}

.back-top__arrow {
  font-size: 15px;
  line-height: 1;
}

.back-top__label {
  margin-top: 1px;
  font-size: 7px;
  letter-spacing: 0.5px;
}
</style>
