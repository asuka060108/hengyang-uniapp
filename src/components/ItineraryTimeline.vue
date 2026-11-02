<script setup>
import { computed } from 'vue';

const props = defineProps({
  days: { type: Array, default: () => [] },
  modelValue: { type: Number, default: 1 },
});

const emit = defineEmits(['update:modelValue']);

const activeDay = computed(() => props.days.find((day) => day.id === props.modelValue) || props.days[0]);

function select(id) {
  emit('update:modelValue', id);
}
</script>

<template>
  <view class="itinerary">
    <scroll-view class="itinerary__tabs-scroll" scroll-x :show-scrollbar="false">
      <view class="itinerary__tabs">
        <view
          v-for="day in days"
          :key="day.id"
          class="itinerary__tab"
          :class="{ 'itinerary__tab--active': modelValue === day.id }"
          @tap="select(day.id)"
        >
          <text class="itinerary__tab-label">{{ day.label }}</text>
          <text class="itinerary__tab-title">{{ day.title }}</text>
        </view>
      </view>
    </scroll-view>

    <view v-if="activeDay" class="itinerary__panel">
      <view class="itinerary__intro">
        <text class="itinerary__day-title">{{ activeDay.title }}</text>
        <text class="itinerary__day-subtitle">{{ activeDay.subtitle }}</text>
      </view>

      <view class="itinerary__list">
        <view v-for="(stop, index) in activeDay.stops" :key="`${activeDay.id}-${stop.time}`" class="stop">
          <view class="stop__rail">
            <view class="stop__dot" :style="{ background: activeDay.color }" />
            <view v-if="index < activeDay.stops.length - 1" class="stop__line" />
          </view>
          <view class="stop__body">
            <view class="stop__head">
              <text class="stop__time">{{ stop.time }}</text>
              <text class="stop__tag">{{ stop.tag }}</text>
            </view>
            <text class="stop__title">{{ stop.title }}</text>
            <text class="stop__desc">{{ stop.description }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<style scoped>
.itinerary {
  overflow: hidden;
  background: var(--surface);
  border: 1px solid var(--line);
  border-radius: 20px;
}

.itinerary__tabs-scroll {
  width: 100%;
  background: var(--surface-strong);
  border-bottom: 1px solid var(--line);
  white-space: nowrap;
}

.itinerary__tabs {
  display: inline-flex;
}

.itinerary__tab {
  display: inline-flex;
  flex-direction: column;
  min-width: 108px;
  padding: 13px 14px 12px;
  border-right: 1px solid var(--line);
}

.itinerary__tab--active {
  background: var(--surface);
  box-shadow: inset 0 -2px 0 var(--jade);
}

.itinerary__tab-label {
  color: var(--cinnabar);
  font-size: 9px;
  font-weight: 700;
  letter-spacing: 1px;
}

.itinerary__tab-title {
  margin-top: 2px;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 14px;
  font-weight: 600;
}

.itinerary__panel {
  padding: 18px 16px 8px;
}

.itinerary__intro {
  padding-bottom: 14px;
  border-bottom: 1px solid var(--line);
}

.itinerary__day-title {
  display: block;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 24px;
  font-weight: 600;
  line-height: 1.3;
}

.itinerary__day-subtitle {
  display: block;
  margin-top: 4px;
  color: var(--muted);
  font-size: 11px;
  letter-spacing: 0.5px;
}

.itinerary__list {
  padding-top: 2px;
}

.stop {
  display: flex;
  gap: 12px;
}

.stop__rail {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 12px;
  padding-top: 20px;
}

.stop__dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
}

.stop__line {
  flex: 1;
  width: 1px;
  margin-top: 4px;
  background: var(--line-strong);
}

.stop__body {
  flex: 1;
  min-width: 0;
  padding: 16px 0 18px;
  border-bottom: 1px solid var(--line);
}

.stop__head {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stop__time {
  color: var(--cinnabar);
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.stop__tag {
  padding: 2px 7px;
  color: var(--muted);
  font-size: 9px;
  background: var(--surface-strong);
  border-radius: 999px;
}

.stop__title {
  display: block;
  margin-top: 5px;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 18px;
  font-weight: 600;
  line-height: 1.4;
}

.stop__desc {
  display: block;
  margin-top: 5px;
  color: var(--muted);
  font-size: 11px;
  line-height: 1.75;
}

@media (min-width: 900px) {
  .itinerary__panel {
    padding: 22px 24px 8px;
  }

  .itinerary__tab {
    min-width: 150px;
    padding: 15px 20px;
  }

  .stop__desc {
    font-size: 12px;
  }
}
</style>
