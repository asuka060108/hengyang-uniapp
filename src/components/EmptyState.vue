<script setup>
defineProps({
  title: { type: String, default: '没有找到结果' },
  description: { type: String, default: '换一个关键词或分类试试。' },
  mark: { type: String, default: '空' },
});
</script>

<template>
  <view class="empty-state">
    <view class="empty-state__mark">{{ mark }}</view>
    <text class="empty-state__title">{{ title }}</text>
    <text class="empty-state__desc">{{ description }}</text>
    <view v-if="$slots.default" class="empty-state__action">
      <slot />
    </view>
  </view>
</template>

<style scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 54px 24px;
  text-align: center;
  background: var(--surface);
  border: 1px dashed var(--line-strong);
  border-radius: 20px;
}

.empty-state__mark {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 58px;
  height: 58px;
  margin-bottom: 16px;
  color: var(--jade);
  font-family: var(--serif);
  font-size: 24px;
  background: var(--jade-light);
  border-radius: 50%;
}

.empty-state__title {
  color: var(--ink);
  font-family: var(--serif);
  font-size: 21px;
  font-weight: 600;
}

.empty-state__desc {
  margin-top: 8px;
  color: var(--muted);
  font-size: 13px;
  line-height: 1.7;
}

.empty-state__action {
  margin-top: 18px;
}
</style>
