import { computed, ref } from 'vue';

const THEME_KEY = 'hengyang_uni_theme';
const initial = uni.getStorageSync(THEME_KEY);
const theme = ref(initial === 'dark' ? 'dark' : 'light');

export function useTheme() {
  const isDark = computed(() => theme.value === 'dark');

  function toggleTheme() {
    theme.value = isDark.value ? 'light' : 'dark';
    uni.setStorageSync(THEME_KEY, theme.value);
  }

  return { theme, isDark, toggleTheme };
}
