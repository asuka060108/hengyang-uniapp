<script setup>
import { computed, ref } from 'vue';
import { onPageScroll, onUnload } from '@dcloudio/uni-app';
import SectionHeading from '@/components/SectionHeading.vue';
import StatStrip from '@/components/StatStrip.vue';
import SpotCard from '@/components/SpotCard.vue';
import FoodCard from '@/components/FoodCard.vue';
import SpotModal from '@/components/SpotModal.vue';
import TravelTips from '@/components/TravelTips.vue';
import BackToTop from '@/components/BackToTop.vue';
import AppFooter from '@/components/AppFooter.vue';
import { attractions } from '@/data/attractions';
import { foods } from '@/data/foods';
import { itineraryDays, travelTips } from '@/data/itinerary';
import { useFavorites } from '@/composables/useFavorites';
import { useTheme } from '@/composables/useTheme';
import { img, goTab } from '@/utils/assets';

const { isDark } = useTheme();
const { hasFavorite, toggleFavorite } = useFavorites();
const featuredSpots = computed(() => attractions.slice(0, 3));
const featuredFoods = computed(() => foods.slice(0, 3));
const activeSpot = ref(null);
const showBackTop = ref(false);

const stats = [
  { value: 1300, unit: 'm', label: '祝融峰海拔' },
  { value: 2200, unit: '+', label: '年建城史' },
  { value: 6, unit: '处', label: '精选目的地' },
  { value: 8, unit: '味', label: '从早吃到夜' },
];

function openSpot(spot) {
  activeSpot.value = spot;
  uni.hideTabBar({ animation: false });
}

function closeSpot() {
  activeSpot.value = null;
  uni.showTabBar({ animation: false });
}

onPageScroll((e) => {
  showBackTop.value = e.scrollTop > 620;
});

onUnload(() => {
  uni.showTabBar({ animation: false });
});
</script>

<template>
  <view class="page home" :class="{ 'theme-dark': isDark }">
    <view class="home-hero">
      <image class="home-hero__image" :src="img('nanyue')" mode="aspectFill" />
      <view class="home-hero__shade" />
      <view class="home-hero__copy">
        <text class="home-hero__eyebrow">HUNAN · CHINA · 26.89°N</text>
        <text class="home-hero__title">山有南岳</text>
        <text class="home-hero__title home-hero__title--gold">城有湘江</text>
        <text class="home-hero__desc">
          衡阳，古称雁城。北雁南飞至此回，湘江与蒸水在此相拥。
          这里有五岳独秀的云海，也有一碗鱼粉唤醒的清晨。
        </text>
        <view class="home-hero__actions">
          <view class="btn btn--light" @tap="goTab('/pages/plan/plan')">
            安排三日行程
            <text class="btn__arrow">→</text>
          </view>
          <view class="btn btn--outline-light" @tap="goTab('/pages/scenery/scenery')">
            先看风景
            <text class="btn__arrow">↗</text>
          </view>
        </view>
      </view>
      <view class="home-hero__stamp">
        <text class="home-hero__stamp-main">雁</text>
        <text class="home-hero__stamp-sub">SOUTH RETURN</text>
      </view>
    </view>

    <view class="container stats-wrap">
      <StatStrip :items="stats" />
    </view>

    <view class="section container intro">
      <view class="intro__copy">
        <text class="eyebrow">WHY HENGYANG</text>
        <text class="intro__title">不只是<text class="intro__em">中转站</text></text>
        <text class="intro__p">
          很多人把衡阳当作去南岳的中转站，匆匆路过。但这座湘江边的城市，
          有书院的旧时光、江心岛的晚风，也有凌晨还冒着热气的鱼粉摊。
        </text>
        <text class="intro__p">
          它不急着讨好谁。你可以在山上看完云海，再回城里慢慢吃一顿饭。
          山与城之间，不过一小时车程。
        </text>
        <view class="text-link" @tap="goTab('/pages/scenery/scenery')">
          <text>从风景开始认识它</text>
          <text>→</text>
        </view>
      </view>

      <view class="intro__collage">
        <view class="intro__figure intro__figure--tall">
          <image class="intro__img" :src="img('dongzhou')" mode="aspectFill" />
          <text class="intro__caption">东洲岛 · 湘江把城市轻轻推开</text>
        </view>
        <view class="intro__figure">
          <image class="intro__img" :src="img('shigu')" mode="aspectFill" />
          <text class="intro__caption">石鼓书院 · 蒸湘汇流处</text>
        </view>
        <text class="intro__note">NORTH GOOSE / SOUTH RETURN</text>
      </view>
    </view>

    <view class="section container">
      <SectionHeading
        eyebrow="SCENERY / 风景"
        title="山有南岳，水有湘江"
        description="从五岳独秀的祝融峰，到江心岛上的书院。衡阳的风景不只在景区里，也在城市转身的每一个瞬间。"
      >
        <template #actions>
          <view class="btn btn--ghost btn--small" @tap="goTab('/pages/scenery/scenery')">
            全部风景 <text class="btn__arrow">→</text>
          </view>
        </template>
      </SectionHeading>
      <view class="spot-grid">
        <SpotCard v-for="spot in featuredSpots" :key="spot.id" :spot="spot" @open="openSpot" />
      </view>
    </view>

    <view class="quote-band">
      <view class="container quote-band__inner">
        <text class="quote-band__mark">“</text>
        <text class="quote-band__text">雁到这里回头，
人到这里慢下来。</text>
        <text class="quote-band__source">回雁峰 · 雁城之名的由来</text>
        <text class="quote-band__seal">雁</text>
      </view>
    </view>

    <view class="section container">
      <SectionHeading
        eyebrow="FOOD / 美食"
        title="一碗衡阳，从早吃到夜"
        description="清晨的鱼粉、午间的土菜、夜里的唆螺。衡阳的味道不精致，但很直接，像这里的人。"
      >
        <template #actions>
          <view class="btn btn--ghost btn--small" @tap="goTab('/pages/food/food')">
            味觉地图 <text class="btn__arrow">→</text>
          </view>
        </template>
      </SectionHeading>
      <view class="food-grid">
        <FoodCard
          v-for="food in featuredFoods"
          :key="food.id"
          :food="food"
          :favorite="hasFavorite(food.id)"
          @toggle="toggleFavorite"
        />
      </view>
    </view>

    <view class="section container plan-preview">
      <view class="plan-preview__copy">
        <text class="eyebrow">3 DAYS IN HENGYANG</text>
        <text class="intro__title">把三天，<text class="intro__em">走成一条慢线</text></text>
        <text class="intro__p">
          第一天给南岳，第二天给古城和江岛，第三天留给江畔、老街和伴手礼。
          不赶路，也不把行程塞满。
        </text>
        <view class="btn" @tap="goTab('/pages/plan/plan')">
          查看完整行程 <text class="btn__arrow">→</text>
        </view>
      </view>
      <view class="plan-preview__days">
        <view
          v-for="day in itineraryDays"
          :key="day.id"
          class="day-card"
          hover-class="day-card--hover"
          @tap="goTab('/pages/plan/plan')"
        >
          <text class="day-card__label">{{ day.label }}</text>
          <text class="day-card__title">{{ day.title }}</text>
          <text class="day-card__sub">{{ day.subtitle }}</text>
        </view>
      </view>
    </view>

    <view class="section container">
      <SectionHeading
        eyebrow="BEFORE YOU GO"
        title="出发前，记住这几件小事"
        description="衡阳不难玩，但山和辣都有自己的脾气。"
      />
      <TravelTips :tips="travelTips" />
    </view>

    <view class="closing">
      <image class="closing__image" :src="img('nanyue')" mode="aspectFill" />
      <view class="closing__shade" />
      <view class="closing__inner">
        <text class="closing__eyebrow">THE LAST PAGE</text>
        <text class="closing__title">来衡阳，
把脚步放慢一点。</text>
        <text class="closing__desc">山会一直在，粉也会一直热。</text>
        <view class="closing__actions">
          <view class="btn btn--light" @tap="goTab('/pages/plan/plan')">安排三日行程</view>
          <view class="closing__link" @tap="goTab('/pages/food/food')">先看看吃什么 ↗</view>
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
.home-hero {
  position: relative;
  height: 520px;
  overflow: hidden;
  background: #101713;
}

.home-hero__image,
.home-hero__shade {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.home-hero__shade {
  background: linear-gradient(180deg, rgba(7, 12, 9, 0.16) 0%, rgba(7, 12, 9, 0.86) 100%);
}

.home-hero__copy {
  position: absolute;
  right: 24px;
  bottom: 38px;
  left: 24px;
}

.home-hero__eyebrow {
  display: block;
  margin-bottom: 12px;
  color: #d6b67c;
  font-size: 9px;
  font-weight: 700;
  letter-spacing: 2px;
}

.home-hero__title {
  display: block;
  color: #f7f1e3;
  font-family: var(--serif);
  font-size: 58px;
  font-weight: 600;
  line-height: 1.06;
  letter-spacing: -2px;
}

.home-hero__title--gold {
  color: #d9b26d;
}

.home-hero__desc {
  display: block;
  max-width: 520px;
  margin-top: 14px;
  color: rgba(247, 241, 227, 0.74);
  font-size: 12px;
  line-height: 1.8;
}

.home-hero__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 9px;
  margin-top: 18px;
}

.home-hero__stamp {
  position: absolute;
  top: 24px;
  right: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 58px;
  height: 58px;
  color: #e1bd7f;
  border: 1px solid rgba(225, 189, 127, 0.55);
  border-radius: 50%;
}

.home-hero__stamp-main {
  font-family: var(--serif);
  font-size: 22px;
  line-height: 1;
}

.home-hero__stamp-sub {
  margin-top: 3px;
  font-size: 5px;
  letter-spacing: 1px;
}

.stats-wrap {
  margin-top: -1px;
}

.intro {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.intro__copy {
  flex: 1;
}

.intro__title {
  display: block;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 34px;
  font-weight: 600;
  line-height: 1.25;
  letter-spacing: -1px;
}

.intro__em {
  color: var(--jade);
  font-style: italic;
}

.intro__p {
  display: block;
  margin-top: 12px;
  color: var(--muted);
  font-size: 13px;
  line-height: 1.85;
}

.text-link {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  margin-top: 16px;
  color: var(--jade);
  font-size: 12px;
  font-weight: 600;
}

.intro__collage {
  position: relative;
  display: flex;
  gap: 10px;
  padding-bottom: 24px;
}

.intro__figure {
  flex: 1;
  overflow: hidden;
  background: var(--surface);
  border: 1px solid var(--line);
  border-radius: 14px;
}

.intro__figure--tall {
  margin-top: 22px;
}

.intro__img {
  width: 100%;
  height: 145px;
}

.intro__caption {
  display: block;
  padding: 7px 8px 8px;
  color: var(--faint);
  font-size: 8px;
  line-height: 1.5;
}

.intro__note {
  position: absolute;
  right: 0;
  bottom: 0;
  color: var(--gold);
  font-size: 7px;
  font-weight: 700;
  letter-spacing: 1.5px;
}

.spot-grid,
.food-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 14px;
}

.quote-band {
  padding: 38px 0;
  background: var(--jade-deep);
}

.quote-band__inner {
  position: relative;
  text-align: center;
}

.quote-band__mark {
  display: block;
  height: 28px;
  color: var(--gold);
  font-family: var(--serif);
  font-size: 56px;
  line-height: 1;
}

.quote-band__text {
  display: block;
  color: #f4eddd;
  font-family: var(--serif);
  font-size: 25px;
  line-height: 1.6;
  white-space: pre-line;
}

.quote-band__source {
  display: block;
  margin-top: 9px;
  color: rgba(244, 237, 221, 0.55);
  font-size: 9px;
  letter-spacing: 1.5px;
}

.quote-band__seal {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  margin-top: 14px;
  color: #e0bb7c;
  font-family: var(--serif);
  font-size: 18px;
  border: 1px solid rgba(224, 187, 124, 0.55);
  border-radius: 50%;
}

.plan-preview {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 22px;
  background: var(--surface);
  border: 1px solid var(--line);
  border-radius: 20px;
}

.plan-preview__days {
  display: flex;
  flex-direction: column;
  gap: 7px;
}

.day-card {
  display: flex;
  align-items: baseline;
  gap: 8px;
  padding: 10px 12px;
  background: var(--surface-strong);
  border-left: 2px solid var(--jade);
  border-radius: 0 10px 10px 0;
}

.day-card--hover {
  opacity: 0.75;
}

.day-card__label {
  color: var(--cinnabar);
  font-size: 8px;
  font-weight: 700;
  letter-spacing: 0.8px;
}

.day-card__title {
  color: var(--ink);
  font-family: var(--serif);
  font-size: 15px;
  font-weight: 600;
}

.day-card__sub {
  color: var(--faint);
  font-size: 9px;
}

.closing {
  position: relative;
  height: 340px;
  overflow: hidden;
  background: #101713;
}

.closing__image,
.closing__shade {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.closing__shade {
  background: rgba(7, 12, 9, 0.68);
}

.closing__inner {
  position: absolute;
  right: 24px;
  bottom: 30px;
  left: 24px;
}

.closing__eyebrow {
  display: block;
  color: #d8b577;
  font-size: 8px;
  font-weight: 700;
  letter-spacing: 2px;
}

.closing__title {
  display: block;
  margin-top: 7px;
  color: #f7f1e3;
  font-family: var(--serif);
  font-size: 34px;
  font-weight: 600;
  line-height: 1.25;
  white-space: pre-line;
}

.closing__desc {
  display: block;
  margin-top: 7px;
  color: rgba(247, 241, 227, 0.65);
  font-size: 11px;
}

.closing__actions {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-top: 15px;
}

.closing__link {
  color: rgba(247, 241, 227, 0.8);
  font-size: 11px;
}

@media (min-width: 900px) {
  .home-hero {
    height: 620px;
  }

  .home-hero__copy {
    right: 48px;
    bottom: 60px;
    left: 48px;
  }

  .home-hero__title {
    font-size: 88px;
  }

  .home-hero__desc {
    font-size: 14px;
  }

  .intro {
    flex-direction: row;
    align-items: center;
    gap: 48px;
  }

  .intro__title {
    font-size: 44px;
  }

  .intro__img {
    height: 230px;
  }

  .spot-grid {
    grid-template-columns: repeat(3, 1fr);
  }

  .food-grid {
    grid-template-columns: repeat(3, 1fr);
  }

  .plan-preview {
    flex-direction: row;
    align-items: center;
    padding: 40px;
    gap: 40px;
  }

  .plan-preview__copy,
  .plan-preview__days {
    flex: 1;
  }

  .quote-band__text {
    font-size: 34px;
  }

  .closing {
    height: 420px;
  }

  .closing__title {
    font-size: 48px;
  }
}
</style>
