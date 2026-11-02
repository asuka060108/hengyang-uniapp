<script setup>
import { computed } from 'vue';

const props = defineProps({
  spot: { type: Object, default: null },
});

const emit = defineEmits(['close']);

const meta = computed(() => {
  if (!props.spot) return [];
  return [
    { label: '位置', value: props.spot.location },
    { label: '海拔', value: props.spot.altitude },
    { label: '建议', value: props.spot.duration },
    { label: '最佳', value: props.spot.bestTime },
  ].filter((item) => item.value);
});

function close() {
  emit('close');
}
</script>

<template>
  <view v-if="spot" class="modal" @tap="close">
    <view class="modal__panel" @tap.stop>
      <scroll-view class="modal__scroll" scroll-y :show-scrollbar="false">
        <view class="modal__media">
          <image class="modal__image" :src="spot.image" mode="aspectFill" />
          <view class="modal__close" hover-class="modal__close--hover" @tap="close">×</view>
          <view class="modal__badge">{{ spot.category }}</view>
        </view>
        <view class="modal__body">
          <text class="modal__eyebrow">{{ spot.eyebrow }}</text>
          <text class="modal__title">{{ spot.name }}</text>
          <text class="modal__en">{{ spot.en }}</text>
          <text class="modal__desc">{{ spot.description }}</text>

          <view class="modal__meta">
            <view v-for="item in meta" :key="item.label" class="modal__meta-item">
              <text class="modal__meta-label">{{ item.label }}</text>
              <text class="modal__meta-value">{{ item.value }}</text>
            </view>
          </view>

          <view class="modal__block">
            <text class="modal__block-title">不可错过</text>
            <view class="modal__tags">
              <text v-for="tag in spot.highlights" :key="tag" class="modal__tag">{{ tag }}</text>
            </view>
          </view>

          <view class="modal__tip">
            <text class="modal__tip-label">旅行提示</text>
            <text class="modal__tip-text">{{ spot.tip }}</text>
          </view>
          <text class="modal__quote">“{{ spot.quote }}”</text>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: 999;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  padding-top: 40px;
  background: rgba(8, 13, 10, 0.62);
}

.modal__panel {
  width: 100%;
  max-width: 760px;
  overflow: hidden;
  background: var(--surface);
  border-radius: 22px 22px 0 0;
}

.modal__scroll {
  height: 78vh;
}

.modal__media {
  position: relative;
  height: 230px;
  overflow: hidden;
  background: var(--surface-strong);
}

.modal__image {
  width: 100%;
  height: 100%;
}

.modal__close {
  position: absolute;
  top: 12px;
  right: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  color: #f8f2e5;
  font-size: 23px;
  line-height: 1;
  background: rgba(12, 18, 15, 0.48);
  border: 1px solid rgba(248, 242, 229, 0.28);
  border-radius: 50%;
}

.modal__close--hover {
  opacity: 0.72;
}

.modal__badge {
  position: absolute;
  bottom: 12px;
  left: 14px;
  padding: 4px 10px;
  color: #f8f2e5;
  font-size: 10px;
  background: rgba(12, 18, 15, 0.62);
  border-radius: 999px;
}

.modal__body {
  padding: 20px 20px calc(28px + env(safe-area-inset-bottom));
}

.modal__eyebrow {
  display: block;
  color: var(--gold);
  font-size: 10px;
  font-weight: 700;
  letter-spacing: 1.4px;
}

.modal__title {
  display: block;
  margin-top: 4px;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 30px;
  font-weight: 600;
  line-height: 1.25;
}

.modal__en {
  display: block;
  margin-top: 3px;
  color: var(--faint);
  font-size: 9px;
  letter-spacing: 1.4px;
}

.modal__desc {
  display: block;
  margin-top: 14px;
  color: var(--ink-soft);
  font-size: 13px;
  line-height: 1.85;
}

.modal__meta {
  display: flex;
  flex-wrap: wrap;
  margin-top: 18px;
  border-top: 1px solid var(--line);
  border-bottom: 1px solid var(--line);
}

.modal__meta-item {
  width: 50%;
  padding: 12px 0;
}

.modal__meta-label {
  display: block;
  color: var(--faint);
  font-size: 9px;
}

.modal__meta-value {
  display: block;
  margin-top: 2px;
  color: var(--ink);
  font-size: 12px;
  font-weight: 600;
}

.modal__block {
  margin-top: 18px;
}

.modal__block-title {
  display: block;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 16px;
  font-weight: 600;
}

.modal__tags {
  display: flex;
  flex-wrap: wrap;
  gap: 7px;
  margin-top: 8px;
}

.modal__tag {
  padding: 4px 10px;
  color: var(--jade);
  font-size: 10px;
  background: var(--jade-light);
  border-radius: 999px;
}

.modal__tip {
  margin-top: 18px;
  padding: 14px;
  background: var(--gold-light);
  border-radius: 14px;
}

.modal__tip-label {
  display: block;
  color: var(--gold);
  font-size: 10px;
  font-weight: 700;
  letter-spacing: 1px;
}

.modal__tip-text {
  display: block;
  margin-top: 5px;
  color: var(--ink-soft);
  font-size: 12px;
  line-height: 1.75;
}

.modal__quote {
  display: block;
  margin-top: 20px;
  color: var(--muted);
  font-family: var(--serif);
  font-size: 15px;
  line-height: 1.8;
  text-align: center;
}

@media (min-width: 900px) {
  .modal {
    align-items: center;
    padding: 40px;
  }

  .modal__panel {
    border-radius: 22px;
  }

  .modal__scroll {
    height: 80vh;
  }
}
</style>
