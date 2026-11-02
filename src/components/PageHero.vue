<script setup>
defineProps({
  image: { type: String, required: true },
  eyebrow: { type: String, default: '' },
  title: { type: String, required: true },
  accent: { type: String, default: '' },
  subtitle: { type: String, default: '' },
  compact: { type: Boolean, default: false },
});
</script>

<template>
  <view class="page-hero" :class="{ 'page-hero--compact': compact }">
    <image class="page-hero__image" :src="image" mode="aspectFill" />
    <view class="page-hero__shade" />
    <view class="page-hero__copy">
      <text v-if="eyebrow" class="page-hero__eyebrow">{{ eyebrow }}</text>
      <text class="page-hero__title">{{ title }}</text>
      <text v-if="accent" class="page-hero__accent">{{ accent }}</text>
      <text v-if="subtitle" class="page-hero__subtitle">{{ subtitle }}</text>
      <view v-if="$slots.meta" class="page-hero__meta">
        <slot name="meta" />
      </view>
    </view>
  </view>
</template>

<style scoped>
.page-hero {
  position: relative;
  height: 350px;
  overflow: hidden;
  background: #1b211d;
}

.page-hero--compact {
  height: 270px;
}

.page-hero__image,
.page-hero__shade {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.page-hero__shade {
  background: linear-gradient(180deg, rgba(9, 15, 12, 0.18) 0%, rgba(9, 15, 12, 0.82) 100%);
}

.page-hero__copy {
  position: absolute;
  right: 24px;
  bottom: 28px;
  left: 24px;
  color: #f8f2e5;
}

.page-hero__eyebrow {
  display: block;
  margin-bottom: 8px;
  color: #e5c28a;
  font-size: 10px;
  font-weight: 700;
  letter-spacing: 2px;
}

.page-hero__title {
  display: block;
  font-family: var(--serif);
  font-size: 43px;
  font-weight: 600;
  line-height: 1.12;
  letter-spacing: -1px;
}

.page-hero__accent {
  display: block;
  margin-top: 2px;
  color: #e3bd7e;
  font-family: var(--serif);
  font-size: 43px;
  font-weight: 600;
  line-height: 1.12;
  letter-spacing: -1px;
}

.page-hero__subtitle {
  display: block;
  max-width: 520px;
  margin-top: 12px;
  color: rgba(248, 242, 229, 0.78);
  font-size: 13px;
  line-height: 1.7;
}

.page-hero__meta {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
  margin-top: 14px;
  color: rgba(248, 242, 229, 0.68);
  font-size: 10px;
  letter-spacing: 0.5px;
}

@media (min-width: 900px) {
  .page-hero {
    height: 450px;
  }

  .page-hero--compact {
    height: 340px;
  }

  .page-hero__copy {
    right: 48px;
    bottom: 44px;
    left: 48px;
  }

  .page-hero__title,
  .page-hero__accent {
    font-size: 64px;
  }

  .page-hero__subtitle {
    font-size: 15px;
  }
}
</style>
