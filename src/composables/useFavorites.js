import { computed, ref } from 'vue';

const FAVORITES_KEY = 'hengyang_uni_favorites';
const stored = uni.getStorageSync(FAVORITES_KEY);
const favorites = ref(Array.isArray(stored) ? stored : []);

function persist() {
  uni.setStorageSync(FAVORITES_KEY, favorites.value);
}

export function useFavorites() {
  const count = computed(() => favorites.value.length);

  function hasFavorite(id) {
    return favorites.value.includes(id);
  }

  function toggleFavorite(id) {
    if (hasFavorite(id)) {
      favorites.value = favorites.value.filter((item) => item !== id);
    } else {
      favorites.value = [...favorites.value, id];
    }
    persist();
  }

  function clearFavorites() {
    favorites.value = [];
    persist();
  }

  return { favorites, count, hasFavorite, toggleFavorite, clearFavorites };
}
