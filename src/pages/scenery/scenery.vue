<script setup>
import { computed, ref } from 'vue';
import { onLoad, onPageScroll, onUnload } from '@dcloudio/uni-app';
import PageHero from '@/components/PageHero.vue';
import SectionHeading from '@/components/SectionHeading.vue';
import FilterTabs from '@/components/FilterTabs.vue';
import SpotCard from '@/components/SpotCard.vue';
import SpotModal from '@/components/SpotModal.vue';
import EmptyState from '@/components/EmptyState.vue';
import BackToTop from '@/components/BackToTop.vue';
import AppFooter from '@/components/AppFooter.vue';
import { attractionCategories, attractions } from '@/data/attractions';
import { useTheme } from '@/composables/useTheme';
import { img, goTab } from '@/utils/assets';

const { isDark } = useTheme();
const activeCategory = ref('all');
const search = ref('');
const sort = ref('recommended');
const activeSpot = ref(null);
const showBackTop = ref(false);

const sortOptions = [
  { value: 'recommended', label: '推荐排序' },
  { value: 'rating', label: '评分优先' },
  { value: 'name', label: '名称排序' },
];

const sortIndex = computed(() => Math.max(0, sortOptions.findIndex((item) => item.value === sort.value)));
const sortLabel = computed(() => sortOptions[sortIndex.value]?.label || '推荐排序');

const filteredSpots = computed(() => {
  const keyword = search.value.trim().toLowerCase();
  let list = attractions.filter((spot) => {
    const matchesCategory =
      activeCategory.value === 'all' || spot.category === activeCategory.value;
    const haystack = [spot.name, spot.en, spot.category, spot.summary, spot.location, ...spot.highlights]
      .join(' ')
      .toLowerCase();
    return matchesCategory && (!keyword || haystack.includes(keyword));
  });

  if (sort.value === 'rating') {
    list = [...list].sort((a, b) => Number(b.rating) - Number(a.rating));
  } else if (sort.value === 'name') {
    list = [...list].sort((a, b) => a.name.localeCompare(b.name, 'zh-CN'));
  }
  return list;
});

const resultText = computed(() =>
  search.value.trim() || activeCategory.value !== 'all'
    ? `找到 ${filteredSpots.value.length} 处目的地`
    : '六处目的地，山、江、书院与古塔'
);

onLoad((options) => {
  if (options?.category && attractionCategories.some((item) => item.value === options.category)) {
    activeCategory.value = options.category;
  }
  if (options?.q) search.value = options.q;
  if (options?.sort && sortOptions.some((item) => item.value === options.sort)) {
    sort.value = options.sort;
  }
});

onPageScroll((e) => {
  showBackTop.value = e.scrollTop > 520;
});

onUnload(() => {
  uni.showTabBar({ animation: false });
});

function onSortChange(event) {
  sort.value = sortOptions[Number(event.detail.value)]?.value || 'recommended';
}

function resetFilters() {
  activeCategory.value = 'all';
  search.value = '';
  sort.value = 'recommended';
}

function openSpot(spot) {
  activeSpot.value = spot;
  uni.hideTabBar({ animation: false });
}

function closeSpot() {
  activeSpot.value = null;
  uni.showTabBar({ animation: false });
}
</script>

<template>
  <view class="page" :class="{ 'theme-dark': isDark }">
    <PageHero
      eyebrow="SCENERY / 风景"
      title="山有南岳"
      accent="水有湘江"
      subtitle="从 1300 米的云海，到江心岛上的一阵晚风。衡阳的风景不急着被看完，它更适合被慢慢经过。"
      :image="img('scenery-hero')"
    >
      <template #meta>
        <text>6 处精选目的地</text>
        <text>山岳 · 古建 · 书院 · 江岛</text>
        <text>建议 2–3 天</text>
      </template>
    </PageHero>

    <view class="section container">
      <SectionHeading
        eyebrow="PLACES TO GO"
        title="把衡阳走成一张地图"
        description="按类型筛选，或者直接搜索你想去的地方。每一处都可以点开看详情。"
      />

      <view class="toolbar">
        <FilterTabs v-model="activeCategory" :items="attractionCategories" />

        <view class="toolbar__right">
          <view class="search-box">
            <text class="search-box__icon">⌕</text>
            <input
              v-model="search"
              class="search-box__input"
              type="text"
              confirm-type="search"
              placeholder="搜索景点、地点或关键词"
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
        </view>
      </view>

      <view class="results-line">
        <text>{{ resultText }}</text>
        <text v-if="search || activeCategory !== 'all'" class="results-line__hint">正在筛选</text>
      </view>

      <view v-if="filteredSpots.length" class="spot-grid">
        <SpotCard v-for="spot in filteredSpots" :key="spot.id" :spot="spot" @open="openSpot" />
      </view>

      <EmptyState
        v-else
        title="没有找到这处风景"
        description="换一个关键词，或者把筛选条件放宽一点。衡阳还有很多角落没写完。"
        mark="寻"
      >
        <view class="btn" @tap="resetFilters">重置筛选</view>
      </EmptyState>
    </view>

    <view class="scenery-tip">
      <view class="container scenery-tip__inner">
        <view class="scenery-tip__seal">游</view>
        <view class="scenery-tip__copy">
          <text class="scenery-tip__eyebrow">A SMALL REMINDER</text>
          <text class="scenery-tip__title">山上的天气，比手机里的预报快一步。</text>
          <text class="scenery-tip__text">
            去南岳记得带一件外套和轻便雨衣。山顶风大，夏天也会凉。
            如果看日出，提前确认景区车和索道的首班时间。
          </text>
        </view>
        <view class="btn btn--light" @tap="goTab('/pages/plan/plan')">
          行程建议 <text class="btn__arrow">→</text>
        </view>
      </view>
    </view>

    <view class="container">
      <AppFooter />
    </view>

    <BackToTop :visible="showBackTop" />
    <SpotModal :spot="activeSpot" @close="closeSpot" />
  </view>
</template>

<style scoped>
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
  line-height: 1;
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
  line-height: 1;
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
  color: var(--cinnabar);
  font-size: 9px;
  letter-spacing: 0.5px;
}

.spot-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 14px;
}

.scenery-tip {
  padding: 34px 0;
  background: var(--jade-deep);
}

.scenery-tip__inner {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 13px;
}

.scenery-tip__seal {
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

.scenery-tip__copy {
  flex: 1;
}

.scenery-tip__eyebrow {
  display: block;
  color: #d9b26d;
  font-size: 8px;
  font-weight: 700;
  letter-spacing: 1.8px;
}

.scenery-tip__title {
  display: block;
  margin-top: 6px;
  color: #f5eddd;
  font-family: var(--serif);
  font-size: 23px;
  font-weight: 600;
  line-height: 1.4;
}

.scenery-tip__text {
  display: block;
  margin-top: 8px;
  color: rgba(245, 237, 221, 0.64);
  font-size: 11px;
  line-height: 1.8;
}

@media (min-width: 900px) {
  .toolbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 22px;
  }

  .toolbar__right {
    flex-direction: row;
    flex-shrink: 0;
    width: 430px;
    margin-top: 0;
  }

  .search-box {
    flex: 1;
  }

  .sort-box {
    width: 120px;
  }

  .spot-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 18px;
  }

  .scenery-tip__inner {
    flex-direction: row;
    align-items: center;
    gap: 20px;
  }
}
</style>
