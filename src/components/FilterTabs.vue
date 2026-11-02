<script setup>
defineProps({
  items: { type: Array, default: () => [] },
  modelValue: { type: String, default: 'all' },
});

const emit = defineEmits(['update:modelValue']);

function select(value) {
  if (value !== undefined) emit('update:modelValue', value);
}
</script>

<template>
  <scroll-view class="filter-scroll" scroll-x :show-scrollbar="false" :enable-flex="true">
    <view class="filter-tabs">
      <view
        v-for="item in items"
        :key="item.value"
        class="filter-tab"
        :class="{ 'filter-tab--active': modelValue === item.value }"
        hover-class="filter-tab--hover"
        @tap="select(item.value)"
      >
        <text>{{ item.label }}</text>
      </view>
    </view>
  </scroll-view>
</template>

<style scoped>
.filter-scroll {
  width: 100%;
  white-space: nowrap;
}

.filter-tabs {
  display: inline-flex;
  gap: 7px;
  padding: 2px 0;
}

.filter-tab {
  display: inline-flex;
  flex: 0 0 68px;
  align-items: center;
  justify-content: center;
  width: 68px;
  min-width: 68px;
  height: 34px;
  padding: 0;
  color: var(--muted);
  font-size: 12px;
  line-height: 34px;
  white-space: nowrap;
  background: var(--surface);
  border: 1px solid var(--line);
  border-radius: 999px;
}

.filter-tab text {
  display: inline-block;
  white-space: nowrap;
}

.filter-tab--active {
  color: #fbf8f1;
  background: var(--jade);
  border-color: var(--jade);
}

.filter-tab--hover {
  opacity: 0.78;
}
</style>
