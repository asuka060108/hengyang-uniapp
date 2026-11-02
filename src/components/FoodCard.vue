<script setup>
defineProps({
  food: { type: Object, required: true },
  favorite: { type: Boolean, default: false },
});

defineEmits(['toggle']);
</script>

<template>
  <view class="food-card">
    <view class="food-card__media">
      <image class="food-card__image" :src="food.image" mode="aspectFill" />
      <view class="food-card__category">{{ food.category }}</view>
      <view
        class="food-card__fav"
        :class="{ 'food-card__fav--active': favorite }"
        hover-class="food-card__fav--hover"
        @tap.stop="$emit('toggle', food.id)"
      >
        <text>{{ favorite ? '♥' : '♡' }}</text>
      </view>
    </view>
    <view class="food-card__body">
      <view class="food-card__heading">
        <text class="food-card__name">{{ food.name }}</text>
        <text class="food-card__price">{{ food.price }}</text>
      </view>
      <text class="food-card__summary">{{ food.summary }}</text>
      <view class="food-card__meta">
        <view class="food-card__spice">
          <text
            v-for="n in 3"
            :key="n"
            class="food-card__dot"
            :class="{ 'food-card__dot--on': n <= food.spice }"
          />
        </view>
        <text class="food-card__meta-text">{{ food.taste }}</text>
        <text class="food-card__meta-text">{{ food.area }}</text>
      </view>
      <view class="food-card__tags">
        <text v-for="tag in food.tags.slice(0, 3)" :key="tag" class="food-card__tag">{{ tag }}</text>
      </view>
    </view>
  </view>
</template>

<style scoped>
.food-card {
  overflow: hidden;
  background: var(--surface);
  border: 1px solid var(--line);
  border-radius: 20px;
}

.food-card__media {
  position: relative;
  height: 190px;
  overflow: hidden;
  background: var(--surface-strong);
}

.food-card__image {
  width: 100%;
  height: 100%;
}

.food-card__category {
  position: absolute;
  top: 12px;
  left: 12px;
  padding: 4px 9px;
  color: #f8f2e5;
  font-size: 10px;
  background: rgba(16, 24, 20, 0.62);
  border-radius: 999px;
}

.food-card__fav {
  position: absolute;
  top: 10px;
  right: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  color: #f8f2e5;
  font-size: 19px;
  background: rgba(16, 24, 20, 0.48);
  border: 1px solid rgba(248, 242, 229, 0.34);
  border-radius: 50%;
}

.food-card__fav--active {
  color: #f6dfb6;
  background: rgba(184, 73, 50, 0.76);
}

.food-card__fav--hover {
  opacity: 0.75;
}

.food-card__body {
  padding: 14px 16px 16px;
}

.food-card__heading {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  gap: 12px;
}

.food-card__name {
  color: var(--ink);
  font-family: var(--serif);
  font-size: 21px;
  font-weight: 600;
  line-height: 1.3;
}

.food-card__price {
  flex-shrink: 0;
  color: var(--gold);
  font-size: 12px;
  font-weight: 600;
}

.food-card__summary {
  display: -webkit-box;
  overflow: hidden;
  margin-top: 8px;
  color: var(--muted);
  font-size: 12px;
  line-height: 1.65;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

.food-card__meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 10px;
}

.food-card__spice {
  display: flex;
  gap: 2px;
}

.food-card__dot {
  width: 5px;
  height: 5px;
  background: var(--line-strong);
  border-radius: 50%;
}

.food-card__dot--on {
  background: var(--cinnabar);
}

.food-card__meta-text {
  color: var(--faint);
  font-size: 10px;
}

.food-card__tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
  margin-top: 10px;
}

.food-card__tag {
  padding: 3px 8px;
  color: var(--jade);
  font-size: 9px;
  background: var(--jade-light);
  border-radius: 999px;
}

@media (min-width: 900px) {
  .food-card__media {
    height: 220px;
  }
}
</style>
