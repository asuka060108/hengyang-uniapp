<script setup>
import { computed, ref } from 'vue';
import { onPageScroll } from '@dcloudio/uni-app';
import PageHero from '@/components/PageHero.vue';
import SectionHeading from '@/components/SectionHeading.vue';
import FilterTabs from '@/components/FilterTabs.vue';
import FoodCard from '@/components/FoodCard.vue';
import FavoritesBar from '@/components/FavoritesBar.vue';
import EmptyState from '@/components/EmptyState.vue';
import BackToTop from '@/components/BackToTop.vue';
import AppFooter from '@/components/AppFooter.vue';
import { foodCategories, foods } from '@/data/foods';
import { useFavorites } from '@/composables/useFavorites';
import { useTheme } from '@/composables/useTheme';
import { img, goTab } from '@/utils/assets';

const { isDark } = useTheme();
const { favorites, count, hasFavorite, toggleFavorite, clearFavorites } = useFavorites();

const activeCategory = ref('all');
const search = ref('');
const sort = ref('recommended');
const showFavorites = ref(false);
const showBackTop = ref(false);

const sortOptions = [
  { value: 'recommended', label: '推荐排序' },
  { value: 'spicy-asc', label: '辣度：从低到高' },
  { value: 'spicy-desc', label: '辣度：从高到低' },
];

const sortIndex = computed(() => Math.max(0, sortOptions.findIndex((item) => item.value === sort.value)));
const sortLabel = computed(() => sortOptions[sortIndex.value]?.label || '推荐排序');

const favoriteFoods = computed(() => foods.filter((food) => favorites.value.includes(food.id)));

const filteredFoods = computed(() => {
  const keyword = search.value.trim().toLowerCase();
  let list = foods.filter((food) => {
    const matchesCategory =
      activeCategory.value === 'all' || food.category === activeCategory.value;
    const matchesFavorite = !showFavorites.value || hasFavorite(food.id);
    const haystack = [food.name, food.en, food.category, food.summary, food.description, ...food.tags]
      .join(' ')
      .toLowerCase();
    return matchesCategory && matchesFavorite && (!keyword || haystack.includes(keyword));
  });

  if (sort.value === 'spicy-asc') {
    list = [...list].sort((a, b) => a.spice - b.spice);
  } else if (sort.value === 'spicy-desc') {
    list = [...list].sort((a, b) => b.spice - a.spice);
  }
  return list;
});

const resultText = computed(() => {
  if (showFavorites.value) {
    return count.value
      ? `我的清单里有 ${filteredFoods.value.length} 种衡阳味道`
      : '还没有收藏，先逛逛再决定';
  }
  if (search.value.trim() || activeCategory.value !== 'all') {
    return `找到 ${filteredFoods.value.length} 种味道`;
  }
  return '八种味道，从早餐一直到夜宵';
});

onPageScroll((e) => {
  showBackTop.value = e.scrollTop > 520;
});

function onSortChange(event) {
  sort.value = sortOptions[Number(event.detail.value)]?.value || 'recommended';
}

function resetFilters() {
  activeCategory.value = 'all';
  search.value = '';
  sort.value = 'recommended';
  showFavorites.value = false;
}

function removeFavorite(id) {
  if (hasFavorite(id)) toggleFavorite(id);
}
</script>

<template>
  <view class="page" :class="{ 'theme-dark': isDark }">
    <PageHero
      eyebrow="FOOD / 美食"
      title="一碗衡阳"
      accent="从早吃到夜"
      subtitle="衡阳菜不绕弯子：鲜、辣、香，直接落到胃里。早上鱼粉，中午土菜，晚上唆螺，半夜还能再打包一份卤粉。"
      :image="img('food-hero')"
    >
      <template #meta>
        <text>8 种本地味道</text>
        <text>粉面 · 土菜 · 小吃 · 物产</text>
        <text>建议空腹阅读</text>
      </template>
    </PageHero>

    <view class="taste-route">
      <view class="container taste-route__inner">
        <view class="taste-route__intro">
          <text class="eyebrow">A DAY OF EATING</text>
          <text class="taste-route__title">衡阳人的一天，
从一碗粉开始。</text>
        </view>
        <view class="taste-route__steps">
          <view class="taste-step">
            <text class="taste-step__time">07:30</text>
            <text class="taste-step__title">鱼粉醒来</text>
            <text class="taste-step__text">鲜鱼汤、剁椒、酸豆角，先喝汤再吃粉。</text>
          </view>
          <view class="taste-step">
            <text class="taste-step__time">12:30</text>
            <text class="taste-step__title">土菜下饭</text>
            <text class="taste-step__text">衡东黄贡椒、脆肚、麸子肉，米饭要续。</text>
          </view>
          <view class="taste-step">
            <text class="taste-step__time">21:30</text>
            <text class="taste-step__title">夜宵收尾</text>
            <text class="taste-step__text">唆螺、烧烤、冰饮，坐在路边慢慢嗦。</text>
          </view>
        </view>
      </view>
    </view>

    <view class="section container">
      <SectionHeading
        eyebrow="TASTE MAP"
        title="把衡阳的味道，装进一张清单"
        description="按类型筛选，搜索你想吃的，或者把感兴趣的菜先收藏起来。"
      />

      <view class="toolbar">
        <FilterTabs v-model="activeCategory" :items="foodCategories" />
        <view class="toolbar__right">
          <view class="search-box">
            <text class="search-box__icon">⌕</text>
            <input
              v-model="search"
              class="search-box__input"
              type="text"
              confirm-type="search"
              placeholder="搜索菜名、口味或食材"
              placeholder-class="search-box__placeholder"
            />
            <text v-if="search" class="search-box__clear" @tap="search = ''">×</text>
          </view>

          <picker
            :range="sortOptions"
            range-key="label"
            :value="sortIndex"
            @change="onSortChange"
          >
            <view class="sort-box">
              <text>{{ sortLabel }}</text>
              <text class="sort-box__arrow">▾</text>
            </view>
          </picker>

          <view
            class="favorite-filter"
            :class="{ 'favorite-filter--active': showFavorites }"
            @tap="showFavorites = !showFavorites"
          >
            <text class="favorite-filter__heart">{{ showFavorites ? '♥' : '♡' }}</text>
            <text>我的清单</text>
            <text v-if="count" class="favorite-filter__count">{{ count }}</text>
          </view>
        </view>
      </view>

      <FavoritesBar
        :foods="favoriteFoods"
        @remove="removeFavorite"
        @clear="clearFavorites"
      />

      <view class="results-line">
        <text>{{ resultText }}</text>
        <text class="results-line__hint">图片为菜品氛围示意</text>
      </view>

      <view v-if="filteredFoods.length" class="food-grid">
        <FoodCard
          v-for="food in filteredFoods"
          :key="food.id"
          :food="food"
          :favorite="hasFavorite(food.id)"
          @toggle="toggleFavorite"
        />
      </view>

      <EmptyState
        v-else
        title="这张桌子上暂时没有菜"
        description="换个关键词，或者取消“我的清单”筛选，看看别的衡阳味道。"
        mark="味"
      >
        <view class="btn" @tap="resetFilters">重置筛选</view>
      </EmptyState>
    </view>

    <view class="food-note">
      <view class="container food-note__inner">
        <view class="food-note__seal">食</view>
        <view class="food-note__copy">
          <text class="food-note__eyebrow">A SMALL WARNING</text>
          <text class="food-note__title">衡阳的“微辣”，可能和你想的不太一样。</text>
          <text class="food-note__text">
            点菜时直接说“不要辣”或“少放辣椒”，比说“微辣”更有效。
            鱼粉和唆螺都可以调整辣度，别硬撑。
          </text>
        </view>
        <view class="btn btn--light" @tap="goTab('/pages/plan/plan')">
          安排吃的行程 <text class="btn__arrow">→</text>
        </view>
      </view>
    </view>

    <view class="container">
      <AppFooter />
    </view>

    <BackToTop :visible="showBackTop" />
  </view>
</template>

<style scoped>
.taste-route {
  padding: 30px 0;
  background: var(--surface-strong);
  border-bottom: 1px solid var(--line);
}

.taste-route__inner {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.taste-route__title {
  display: block;
  margin-top: 6px;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 27px;
  font-weight: 600;
  line-height: 1.4;
  white-space: pre-line;
}

.taste-route__steps {
  display: flex;
  flex-direction: column;
  gap: 7px;
}

.taste-step {
  padding: 10px 12px;
  background: var(--surface);
  border-left: 2px solid var(--gold);
  border-radius: 0 10px 10px 0;
}

.taste-step__time {
  color: var(--cinnabar);
  font-size: 9px;
  font-weight: 700;
}

.taste-step__title {
  display: block;
  margin-top: 1px;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 15px;
  font-weight: 600;
}

.taste-step__text {
  display: block;
  margin-top: 2px;
  color: var(--muted);
  font-size: 10px;
  line-height: 1.6;
}

.toolbar {
  padding: 12px;
  background: var(--surface);
  border: 1px solid var(--line);
  border-radius: 16px;
}

.toolbar__right {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 10px;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 7px;
  height: 40px;
  padding: 0 12px;
  background: var(--bg);
  border: 1px solid var(--line);
  border-radius: 10px;
}

.search-box__icon {
  color: var(--faint);
  font-size: 18px;
}

.search-box__input {
  flex: 1;
  min-width: 0;
  height: 38px;
  color: var(--ink);
  font-size: 12px;
}

.search-box__placeholder {
  color: var(--faint);
}

.search-box__clear {
  color: var(--faint);
  font-size: 18px;
}

.sort-box {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 7px;
  height: 38px;
  color: var(--ink-soft);
  font-size: 11px;
  background: var(--bg);
  border: 1px solid var(--line);
  border-radius: 10px;
}

.sort-box__arrow {
  color: var(--faint);
  font-size: 9px;
}

.favorite-filter {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  height: 38px;
  color: var(--muted);
  font-size: 11px;
  background: var(--bg);
  border: 1px solid var(--line);
  border-radius: 10px;
}

.favorite-filter--active {
  color: var(--cinnabar);
  background: var(--cinnabar-light);
  border-color: var(--cinnabar);
}

.favorite-filter__heart {
  font-size: 14px;
}

.favorite-filter__count {
  padding: 0 5px;
  color: #f8f2e5;
  font-size: 9px;
  background: var(--cinnabar);
  border-radius: 999px;
}

.results-line {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  margin: 16px 0 12px;
  color: var(--muted);
  font-size: 11px;
}

.results-line__hint {
  color: var(--faint);
  font-size: 9px;
}

.food-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 14px;
}

.food-note {
  padding: 34px 0;
  background: var(--jade-deep);
}

.food-note__inner {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 13px;
}

.food-note__seal {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 42px;
  height: 42px;
  color: #e0bb7c;
  font-family: var(--serif);
  font-size: 20px;
  border: 1px solid rgba(224, 187, 124, 0.55);
  border-radius: 50%;
}

.food-note__eyebrow {
  display: block;
  color: #d9b26d;
  font-size: 8px;
  font-weight: 700;
  letter-spacing: 1.8px;
}

.food-note__title {
  display: block;
  margin-top: 6px;
  color: #f5eddd;
  font-family: var(--serif);
  font-size: 23px;
  font-weight: 600;
  line-height: 1.4;
}

.food-note__text {
  display: block;
  margin-top: 8px;
  color: rgba(245, 237, 221, 0.64);
  font-size: 11px;
  line-height: 1.8;
}

.favorite-filter:active,
.sort-box:active {
  opacity: 0.75;
}

@media (min-width: 900px) {
  .taste-route__inner {
    flex-direction: row;
    align-items: center;
    gap: 44px;
  }

  .taste-route__intro,
  .taste-route__steps {
    flex: 1;
  }

  .taste-route__steps {
    flex-direction: row;
  }

  .taste-step {
    flex: 1;
  }

  .toolbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 22px;
  }

  .toolbar__right {
    flex-direction: row;
    flex-shrink: 0;
    width: 520px;
    margin-top: 0;
  }

  .search-box {
    flex: 1;
  }

  .sort-box {
    width: 145px;
  }

  .favorite-filter {
    width: 125px;
  }

  .food-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 18px;
  }

  .food-note__inner {
    flex-direction: row;
    align-items: center;
    gap: 20px;
  }
}
</style>
