<script setup>
import { onMounted, onUnmounted, ref } from 'vue';

const props = defineProps({
  items: {
    type: Array,
    default: () => [],
  },
});

const values = ref(props.items.map(() => 0));
let timer = null;

onMounted(() => {
  const duration = 760;
  const start = Date.now();
  timer = setInterval(() => {
    const progress = Math.min((Date.now() - start) / duration, 1);
    const eased = 1 - Math.pow(1 - progress, 3);
    values.value = props.items.map((item) => Math.round(Number(item.value) * eased));
    if (progress >= 1) {
      clearInterval(timer);
      timer = null;
    }
  }, 36);
});

onUnmounted(() => {
  if (timer) clearInterval(timer);
});
</script>

<template>
  <view class="stat-strip">
    <view v-for="(item, index) in items" :key="item.label" class="stat-strip__item">
      <view class="stat-strip__value-row">
        <text class="stat-strip__value">{{ values[index] }}</text>
        <text v-if="item.unit" class="stat-strip__unit">{{ item.unit }}</text>
      </view>
      <text class="stat-strip__label">{{ item.label }}</text>
    </view>
  </view>
</template>

<style scoped>
.stat-strip {
  display: flex;
  padding: 18px 0;
  background: var(--surface);
  border-top: 1px solid var(--line);
  border-bottom: 1px solid var(--line);
}

.stat-strip__item {
  flex: 1;
  min-width: 0;
  padding: 0 10px;
  text-align: center;
  border-right: 1px solid var(--line);
}

.stat-strip__item:last-child {
  border-right: 0;
}

.stat-strip__value-row {
  display: flex;
  align-items: baseline;
  justify-content: center;
  gap: 2px;
}

.stat-strip__value {
  color: var(--ink);
  font-family: var(--serif);
  font-size: 25px;
  font-weight: 600;
  line-height: 1.2;
}

.stat-strip__unit {
  color: var(--gold);
  font-size: 10px;
  font-weight: 700;
}

.stat-strip__label {
  display: block;
  margin-top: 4px;
  color: var(--muted);
  font-size: 10px;
  letter-spacing: 0.5px;
}
</style>
