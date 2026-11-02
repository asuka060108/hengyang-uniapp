<script setup>
defineProps({
  eyebrow: { type: String, default: '' },
  title: { type: String, required: true },
  description: { type: String, default: '' },
});
</script>

<template>
  <view class="section-heading">
    <view class="section-heading__main">
      <text v-if="eyebrow" class="eyebrow">{{ eyebrow }}</text>
      <text class="section-heading__title">{{ title }}</text>
      <text v-if="description" class="section-heading__desc">{{ description }}</text>
    </view>
    <view class="section-heading__actions">
      <slot name="actions" />
    </view>
  </view>
</template>

<style scoped>
.section-heading {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 20px;
}

.section-heading__main {
  flex: 1;
  min-width: 0;
}

.section-heading__title {
  display: block;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 26px;
  font-weight: 600;
  line-height: 1.3;
  letter-spacing: -0.5px;
}

.section-heading__desc {
  display: block;
  margin-top: 6px;
  color: var(--muted);
  font-size: 13px;
  line-height: 1.7;
}

.section-heading__actions {
  flex-shrink: 0;
  padding-bottom: 3px;
}
</style>
