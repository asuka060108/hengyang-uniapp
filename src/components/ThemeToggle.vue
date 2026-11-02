<script setup>
import { useTheme } from '@/composables/useTheme';

const { isDark, toggleTheme } = useTheme();
</script>

<template>
  <view
    class="theme-toggle"
    :class="{ 'theme-toggle--dark': isDark }"
    hover-class="theme-toggle--hover"
    @tap="toggleTheme"
  >
    <text class="theme-toggle__icon">{{ isDark ? '☾' : '☀' }}</text>
    <text class="theme-toggle__label">{{ isDark ? '夜' : '日' }}</text>
  </view>
</template>

<style scoped>
.theme-toggle {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  width: 62px;
  height: 34px;
  color: var(--ink);
  background: rgba(251, 248, 241, 0.86);
  border: 1px solid var(--line);
  border-radius: 999px;
}

.theme-toggle--dark {
  color: var(--ink);
  background: rgba(26, 34, 30, 0.86);
}

.theme-toggle--hover {
  opacity: 0.78;
}

.theme-toggle__icon {
  font-size: 15px;
  line-height: 1;
}

.theme-toggle__label {
  font-size: 10px;
  letter-spacing: 1px;
}
</style>
