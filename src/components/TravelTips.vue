<script setup>
defineProps({
  tips: { type: Array, default: () => [] },
});
</script>

<template>
  <view class="tips">
    <view v-for="(tip, index) in tips" :key="tip.title" class="tips__item">
      <view class="tips__index">{{ index < 9 ? '0' + (index + 1) : index + 1 }}</view>
      <view class="tips__content">
        <text class="tips__title">{{ tip.title }}</text>
        <text class="tips__text">{{ tip.text }}</text>
      </view>
    </view>
  </view>
</template>

<style scoped>
.tips {
  display: flex;
  flex-direction: column;
  gap: 9px;
}

.tips__item {
  display: flex;
  gap: 13px;
  padding: 14px;
  background: var(--surface);
  border: 1px solid var(--line);
  border-radius: 15px;
}

.tips__index {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  color: var(--gold);
  font-family: var(--serif);
  font-size: 13px;
  background: var(--gold-light);
  border-radius: 50%;
}

.tips__content {
  flex: 1;
  min-width: 0;
}

.tips__title {
  display: block;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 15px;
  font-weight: 600;
}

.tips__text {
  display: block;
  margin-top: 3px;
  color: var(--muted);
  font-size: 11px;
  line-height: 1.7;
}
</style>
