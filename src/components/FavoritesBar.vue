<script setup>
defineProps({
  foods: { type: Array, default: () => [] },
});

defineEmits(['remove', 'clear']);
</script>

<template>
  <view v-if="foods.length" class="favorites">
    <view class="favorites__head">
      <view class="favorites__title-wrap">
        <text class="favorites__title">我的味觉清单</text>
        <text class="favorites__count">{{ foods.length }} 道</text>
      </view>
      <text class="favorites__clear" @tap="$emit('clear')">清空</text>
    </view>
    <scroll-view class="favorites__scroll" scroll-x :show-scrollbar="false">
      <view class="favorites__list">
        <view v-for="food in foods" :key="food.id" class="favorites__chip">
          <text class="favorites__chip-text">{{ food.name }}</text>
          <text class="favorites__remove" @tap.stop="$emit('remove', food.id)">×</text>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<style scoped>
.favorites {
  padding: 14px;
  background: var(--surface);
  border: 1px solid var(--line);
  border-radius: 16px;
}

.favorites__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.favorites__title-wrap {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.favorites__title {
  color: var(--ink);
  font-family: var(--serif);
  font-size: 15px;
  font-weight: 600;
}

.favorites__count {
  color: var(--cinnabar);
  font-size: 10px;
}

.favorites__clear {
  color: var(--faint);
  font-size: 10px;
}

.favorites__scroll {
  width: 100%;
  margin-top: 9px;
  white-space: nowrap;
}

.favorites__list {
  display: inline-flex;
  gap: 7px;
}

.favorites__chip {
  display: inline-flex;
  flex: 0 0 auto;
  align-items: center;
  gap: 6px;
  padding: 6px 9px;
  white-space: nowrap;
  background: var(--jade-light);
  border-radius: 999px;
}

.favorites__chip-text {
  color: var(--jade);
  font-size: 11px;
  white-space: nowrap;
}

.favorites__remove {
  color: var(--jade);
  font-size: 14px;
  line-height: 1;
}
</style>
