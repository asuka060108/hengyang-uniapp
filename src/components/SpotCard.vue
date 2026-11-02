<script setup>
defineProps({
  spot: { type: Object, required: true },
});

defineEmits(['open']);
</script>

<template>
  <view class="spot-card" hover-class="spot-card--hover" @tap="$emit('open', spot)">
    <view class="spot-card__media">
      <image class="spot-card__image" :src="spot.image" mode="aspectFill" />
      <view class="spot-card__badge">
        <text>{{ spot.category }}</text>
      </view>
      <view class="spot-card__rating">
        <text class="spot-card__star">★</text>
        <text>{{ spot.rating }}</text>
      </view>
    </view>
    <view class="spot-card__body">
      <view class="spot-card__heading">
        <view class="spot-card__name-wrap">
          <text class="spot-card__name">{{ spot.name }}</text>
          <text class="spot-card__en">{{ spot.en }}</text>
        </view>
        <text class="spot-card__arrow">↗</text>
      </view>
      <text class="spot-card__summary">{{ spot.summary }}</text>
      <view class="spot-card__meta">
        <text>{{ spot.location }}</text>
        <text class="spot-card__dot">·</text>
        <text>{{ spot.duration }}</text>
      </view>
      <view class="spot-card__tags">
        <text v-for="tag in spot.highlights.slice(0, 3)" :key="tag" class="spot-card__tag">{{ tag }}</text>
      </view>
    </view>
  </view>
</template>

<style scoped>
.spot-card {
  overflow: hidden;
  background: var(--surface);
  border: 1px solid var(--line);
  border-radius: 20px;
}

.spot-card--hover {
  opacity: 0.88;
}

.spot-card__media {
  position: relative;
  height: 196px;
  overflow: hidden;
  background: var(--surface-strong);
}

.spot-card__image {
  width: 100%;
  height: 100%;
}

.spot-card__badge,
.spot-card__rating {
  position: absolute;
  top: 12px;
  display: flex;
  align-items: center;
  height: 24px;
  padding: 0 9px;
  color: #f8f2e5;
  font-size: 10px;
  background: rgba(16, 24, 20, 0.62);
  border-radius: 999px;
}

.spot-card__badge {
  left: 12px;
}

.spot-card__rating {
  right: 12px;
  gap: 4px;
}

.spot-card__star {
  color: #e4bc77;
  font-size: 11px;
}

.spot-card__body {
  padding: 15px 16px 17px;
}

.spot-card__heading {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 10px;
}

.spot-card__name-wrap {
  flex: 1;
  min-width: 0;
}

.spot-card__name {
  display: block;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 23px;
  font-weight: 600;
  line-height: 1.25;
}

.spot-card__en {
  display: block;
  margin-top: 3px;
  color: var(--faint);
  font-size: 9px;
  letter-spacing: 1.2px;
}

.spot-card__arrow {
  color: var(--jade);
  font-size: 20px;
  line-height: 1;
}

.spot-card__summary {
  display: -webkit-box;
  overflow: hidden;
  margin-top: 9px;
  color: var(--muted);
  font-size: 12px;
  line-height: 1.65;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

.spot-card__meta {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 10px;
  color: var(--faint);
  font-size: 10px;
}

.spot-card__dot {
  color: var(--gold);
}

.spot-card__tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
  margin-top: 11px;
}

.spot-card__tag {
  padding: 3px 8px;
  color: var(--jade);
  font-size: 9px;
  background: var(--jade-light);
  border-radius: 999px;
}

@media (min-width: 900px) {
  .spot-card__media {
    height: 230px;
  }
}
</style>
