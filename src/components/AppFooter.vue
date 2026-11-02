<script setup>
import ThemeToggle from '@/components/ThemeToggle.vue';

const links = [
  { label: '首页', path: '/pages/index/index' },
  { label: '风景', path: '/pages/scenery/scenery' },
  { label: '美食', path: '/pages/food/food' },
  { label: '行程', path: '/pages/plan/plan' },
];

function go(path) {
  uni.switchTab({ url: path });
}
</script>

<template>
  <view class="footer">
    <view class="footer__top">
      <view class="footer__brand">
        <view class="footer__seal">衡</view>
        <view>
          <text class="footer__name">雁城衡阳</text>
          <text class="footer__tagline">山有南岳，城有湘江</text>
        </view>
      </view>
      <ThemeToggle />
    </view>

    <view class="footer__links">
      <text v-for="link in links" :key="link.path" class="footer__link" @tap="go(link.path)">
        {{ link.label }}
      </text>
    </view>

    <view class="footer__bottom">
      <text>uni-app 跨端版 · 衡阳旅行指南</text>
      <text>© 2026 · 南华大学</text>
    </view>
  </view>
</template>

<style scoped>
.footer {
  margin-top: 30px;
  padding: 22px 0 18px;
  border-top: 1px solid var(--line);
}

.footer__top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.footer__brand {
  display: flex;
  align-items: center;
  gap: 10px;
}

.footer__seal {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  color: #f8f2e5;
  font-family: var(--serif);
  font-size: 16px;
  background: var(--cinnabar);
  border-radius: 7px;
}

.footer__name {
  display: block;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 16px;
  font-weight: 600;
}

.footer__tagline {
  display: block;
  margin-top: 1px;
  color: var(--faint);
  font-size: 9px;
  letter-spacing: 1px;
}

.footer__links {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
  margin-top: 18px;
}

.footer__link {
  color: var(--muted);
  font-size: 11px;
}

.footer__bottom {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  gap: 6px;
  margin-top: 18px;
  padding-top: 12px;
  color: var(--faint);
  font-size: 9px;
  line-height: 1.6;
  border-top: 1px solid var(--line);
}
</style>
