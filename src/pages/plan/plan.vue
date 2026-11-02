<script setup>
import { computed, onMounted, ref, watch } from 'vue';
import { onPageScroll } from '@dcloudio/uni-app';
import PageHero from '@/components/PageHero.vue';
import SectionHeading from '@/components/SectionHeading.vue';
import ItineraryTimeline from '@/components/ItineraryTimeline.vue';
import TravelTips from '@/components/TravelTips.vue';
import BackToTop from '@/components/BackToTop.vue';
import AppFooter from '@/components/AppFooter.vue';
import { itineraryDays, packingItems, travelTips } from '@/data/itinerary';
import { useTheme } from '@/composables/useTheme';
import { img } from '@/utils/assets';

const { isDark } = useTheme();
const STORAGE_KEY = 'hengyang_uni_packing';
const packingReady = ref(false);

const budgetStyle = ref('comfort');
const travelDays = ref(3);
const travelers = ref(2);
const activeDay = ref(1);
const showBackTop = ref(false);

const styleOptions = [
  { value: 'economy', label: '经济', perDay: 280, note: '青旅 / 民宿 + 本地小馆' },
  { value: 'comfort', label: '舒适', perDay: 520, note: '舒适酒店 + 正经餐馆' },
  { value: 'premium', label: '品质', perDay: 860, note: '山景酒店 + 精选餐厅' },
];

const ticketPerPerson = 160;
const styleIndex = computed(() =>
  Math.max(0, styleOptions.findIndex((item) => item.value === budgetStyle.value))
);
const styleInfo = computed(() => styleOptions[styleIndex.value] || styleOptions[1]);
const styleLabels = styleOptions.map((item) => `${item.label} · ${item.note}`);
const perPerson = computed(() => styleInfo.value.perDay * travelDays.value + ticketPerPerson);
const totalBudget = computed(() => perPerson.value * travelers.value);

const checklist = ref(packingItems.map((item) => ({ ...item, checked: false })));
const doneCount = computed(() => checklist.value.filter((item) => item.checked).length);
const packingProgress = computed(() =>
  checklist.value.length ? (doneCount.value / checklist.value.length) * 100 : 0
);

const transport = [
  {
    type: '高铁',
    title: '衡阳东站',
    text: '长沙南约 30–40 分钟，广州南约 2 小时。出站后可打车或坐旅游专线去南岳。',
  },
  {
    type: '飞机',
    title: '衡阳南岳机场',
    text: '机场到市区约 30 分钟，航班较少，适合远距离出发。',
  },
  {
    type: '自驾',
    title: '南岳景区',
    text: '节假日停车位紧张，建议早上 8 点前到。山路多弯，雨天慢行。',
  },
];

onMounted(() => {
  const saved = uni.getStorageSync(STORAGE_KEY);
  if (Array.isArray(saved)) {
    const ids = saved.map(String);
    checklist.value = checklist.value.map((item) => ({
      ...item,
      checked: ids.includes(item.id),
    }));
  }
  packingReady.value = true;
});

watch(
  checklist,
  (value) => {
    if (!packingReady.value) return;
    uni.setStorageSync(
      STORAGE_KEY,
      value.filter((item) => item.checked).map((item) => item.id)
    );
  },
  { deep: true }
);

onPageScroll((e) => {
  showBackTop.value = e.scrollTop > 560;
});

function formatMoney(value) {
  return String(value).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

function onStyleChange(event) {
  budgetStyle.value = styleOptions[Number(event.detail.value)]?.value || 'comfort';
}

function onDaysChange(event) {
  travelDays.value = Number(event.detail.value);
}

function onTravelersChange(event) {
  travelers.value = Number(event.detail.value);
}

function toggleCheck(item) {
  item.checked = !item.checked;
}

function resetChecklist() {
  checklist.value = checklist.value.map((item) => ({ ...item, checked: false }));
}
</script>

<template>
  <view class="page" :class="{ 'theme-dark': isDark }">
    <PageHero
      eyebrow="PLAN / 行程"
      title="三日雁城"
      accent="把时间走慢"
      subtitle="三天两晚，一天给山，一天给城，一天给江畔和伴手礼。不是打卡清单，是一条可以随时停下来的慢路线。"
      :image="img('dongzhou')"
    >
      <template #meta>
        <text>3 天 2 晚</text>
        <text>南岳 + 市区 + 江畔</text>
        <text>适合第一次来衡阳</text>
      </template>
    </PageHero>

    <view class="plan-facts">
      <view class="container plan-facts__grid">
        <view class="plan-fact">
          <text class="plan-fact__label">最佳季节</text>
          <text class="plan-fact__value">春 / 秋</text>
          <text class="plan-fact__hint">4–5 月、9–11 月最舒服</text>
        </view>
        <view class="plan-fact">
          <text class="plan-fact__label">建议节奏</text>
          <text class="plan-fact__value">3 天 2 晚</text>
          <text class="plan-fact__hint">山上住一晚，市区住一晚</text>
        </view>
        <view class="plan-fact">
          <text class="plan-fact__label">人均预算</text>
          <text class="plan-fact__value">¥{{ formatMoney(perPerson) }}</text>
          <text class="plan-fact__hint">不含往返大交通</text>
        </view>
        <view class="plan-fact">
          <text class="plan-fact__label">关键词</text>
          <text class="plan-fact__value">山 · 江 · 粉</text>
          <text class="plan-fact__hint">早起看云，夜里嗦螺</text>
        </view>
      </view>
    </view>

    <view class="section container">
      <SectionHeading
        eyebrow="THE ROUTE"
        title="三天，刚好够认识衡阳"
        description="点击 DAY 01 / 02 / 03 切换行程。时间可以弹性调整，别把自己赶得太紧。"
      />
      <ItineraryTimeline v-model="activeDay" :days="itineraryDays" />
    </view>

    <view class="section container plan-tools">
      <view class="budget-card">
        <text class="eyebrow">BUDGET CALCULATOR</text>
        <text class="plan-card__title">先算一笔<text class="plan-card__em">旅行账</text></text>
        <text class="plan-card__intro">
          按 3 天 2 晚估算，包含住宿、吃饭、市内交通和南岳门票，不含往返大交通。
        </text>

        <view class="budget-form">
          <view class="field">
            <text class="field__label">旅行方式</text>
            <picker :range="styleLabels" :value="styleIndex" @change="onStyleChange">
              <view class="field__select">
                <text>{{ styleLabels[styleIndex] }}</text>
                <text class="field__arrow">▾</text>
              </view>
            </picker>
          </view>

          <view class="slider-field">
            <view class="slider-field__head">
              <text>旅行天数</text>
              <text class="slider-field__value">{{ travelDays }} 天</text>
            </view>
            <slider
              :value="travelDays"
              :min="1"
              :max="5"
              :step="1"
              active-color="#1f6b56"
              background-color="rgba(30,40,35,0.12)"
              block-size="18"
              @changing="onDaysChange"
              @change="onDaysChange"
            />
          </view>

          <view class="slider-field">
            <view class="slider-field__head">
              <text>出行人数</text>
              <text class="slider-field__value">{{ travelers }} 人</text>
            </view>
            <slider
              :value="travelers"
              :min="1"
              :max="8"
              :step="1"
              active-color="#1f6b56"
              background-color="rgba(30,40,35,0.12)"
              block-size="18"
              @changing="onTravelersChange"
              @change="onTravelersChange"
            />
          </view>
        </view>

        <view class="budget-result">
          <view class="budget-result__item">
            <text class="budget-result__label">人均预算</text>
            <text class="budget-result__value">¥{{ formatMoney(perPerson) }}</text>
          </view>
          <view class="budget-result__item">
            <text class="budget-result__label">总预算</text>
            <text class="budget-result__value">¥{{ formatMoney(totalBudget) }}</text>
          </view>
        </view>
        <text class="budget-card__note">
          估算口径：{{ styleInfo.note }}，含南岳门票 ¥{{ ticketPerPerson }}/人。
        </text>
      </view>

      <view class="checklist-card">
        <view class="checklist-card__head">
          <view>
            <text class="eyebrow">PACKING LIST</text>
            <text class="plan-card__title">出发前，<text class="plan-card__em">别忘带这些</text></text>
          </view>
          <text class="checklist-card__count">{{ doneCount }} / {{ checklist.length }}</text>
        </view>

        <view class="checklist-progress">
          <view class="checklist-progress__bar" :style="{ width: `${packingProgress}%` }" />
        </view>

        <view class="checklist">
          <view
            v-for="item in checklist"
            :key="item.id"
            class="checklist__item"
            :class="{ 'checklist__item--checked': item.checked }"
            @tap="toggleCheck(item)"
          >
            <view class="checklist__box">{{ item.checked ? '✓' : '' }}</view>
            <view class="checklist__text">
              <text class="checklist__label">{{ item.label }}</text>
              <text class="checklist__hint">{{ item.hint }}</text>
            </view>
          </view>
        </view>

        <view class="checklist-card__foot">
          <text>勾选状态会保存在本机。</text>
          <text class="checklist-card__reset" @tap="resetChecklist">清空清单</text>
        </view>
      </view>
    </view>

    <view class="section container">
      <SectionHeading
        eyebrow="GETTING THERE"
        title="怎么到衡阳，怎么去南岳"
        description="衡阳的交通不复杂，关键是给南岳留出完整的半天。"
      />
      <view class="transport-grid">
        <view v-for="item in transport" :key="item.type" class="transport-card">
          <text class="transport-card__type">{{ item.type }}</text>
          <text class="transport-card__title">{{ item.title }}</text>
          <text class="transport-card__text">{{ item.text }}</text>
        </view>
      </view>
    </view>

    <view class="section container">
      <SectionHeading
        eyebrow="TRAVEL NOTES"
        title="最后，再提醒你四件事"
        description="这些小事，能让衡阳之行舒服很多。"
      />
      <TravelTips :tips="travelTips" />
    </view>

    <view class="container">
      <AppFooter />
    </view>

    <BackToTop :visible="showBackTop" />
  </view>
</template>

<style scoped>
.plan-facts {
  padding: 18px 0;
  background: var(--surface);
  border-top: 1px solid var(--line);
  border-bottom: 1px solid var(--line);
}

.plan-facts__grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px 12px;
}

.plan-fact__label {
  display: block;
  color: var(--faint);
  font-size: 9px;
  letter-spacing: 0.5px;
}

.plan-fact__value {
  display: block;
  margin-top: 3px;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 19px;
  font-weight: 600;
}

.plan-fact__hint {
  display: block;
  margin-top: 2px;
  color: var(--muted);
  font-size: 8px;
  line-height: 1.5;
}

.plan-tools {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.budget-card,
.checklist-card {
  padding: 18px;
  background: var(--surface);
  border: 1px solid var(--line);
  border-radius: 20px;
}

.plan-card__title {
  display: block;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 28px;
  font-weight: 600;
  line-height: 1.3;
}

.plan-card__em {
  color: var(--jade);
  font-style: italic;
}

.plan-card__intro {
  display: block;
  margin-top: 8px;
  color: var(--muted);
  font-size: 10px;
  line-height: 1.7;
}

.budget-form {
  margin-top: 16px;
}

.field__label {
  display: block;
  margin-bottom: 5px;
  color: var(--faint);
  font-size: 9px;
}

.field__select {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  min-height: 38px;
  padding: 0 11px;
  color: var(--ink);
  font-size: 11px;
  background: var(--bg);
  border: 1px solid var(--line);
  border-radius: 9px;
}

.field__arrow {
  color: var(--faint);
  font-size: 9px;
}

.slider-field {
  margin-top: 13px;
}

.slider-field__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: var(--muted);
  font-size: 10px;
}

.slider-field__value {
  color: var(--ink);
  font-size: 11px;
  font-weight: 600;
}

.slider-field slider {
  margin: 5px 0 0;
}

.budget-result {
  display: flex;
  gap: 10px;
  margin-top: 4px;
  padding-top: 13px;
  border-top: 1px solid var(--line);
}

.budget-result__item {
  flex: 1;
  padding: 9px 10px;
  background: var(--jade-light);
  border-radius: 10px;
}

.budget-result__label {
  display: block;
  color: var(--jade);
  font-size: 8px;
}

.budget-result__value {
  display: block;
  margin-top: 2px;
  color: var(--jade);
  font-family: var(--serif);
  font-size: 21px;
  font-weight: 600;
}

.budget-card__note {
  display: block;
  margin-top: 8px;
  color: var(--faint);
  font-size: 8px;
  line-height: 1.5;
}

.checklist-card__head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
}

.checklist-card__count {
  flex-shrink: 0;
  padding: 3px 8px;
  color: var(--cinnabar);
  font-size: 10px;
  background: var(--cinnabar-light);
  border-radius: 999px;
}

.checklist-progress {
  height: 4px;
  margin: 14px 0 6px;
  overflow: hidden;
  background: var(--bg);
  border-radius: 999px;
}

.checklist-progress__bar {
  height: 100%;
  background: var(--jade);
  border-radius: 999px;
  transition: width 0.25s ease;
}

.checklist {
  display: flex;
  flex-direction: column;
}

.checklist__item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 0;
  border-bottom: 1px solid var(--line);
}

.checklist__box {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 18px;
  height: 18px;
  color: #f8f2e5;
  font-size: 12px;
  border: 1px solid var(--line-strong);
  border-radius: 5px;
}

.checklist__item--checked .checklist__box {
  background: var(--jade);
  border-color: var(--jade);
}

.checklist__text {
  flex: 1;
  min-width: 0;
}

.checklist__label {
  display: block;
  color: var(--ink);
  font-size: 11px;
  font-weight: 600;
}

.checklist__item--checked .checklist__label {
  color: var(--faint);
  text-decoration: line-through;
}

.checklist__hint {
  display: block;
  margin-top: 1px;
  color: var(--faint);
  font-size: 8px;
}

.checklist-card__foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  margin-top: 12px;
  color: var(--faint);
  font-size: 8px;
}

.checklist-card__reset {
  color: var(--cinnabar);
}

.transport-grid {
  display: flex;
  flex-direction: column;
  gap: 9px;
}

.transport-card {
  padding: 14px;
  background: var(--surface);
  border: 1px solid var(--line);
  border-radius: 14px;
}

.transport-card__type {
  display: inline-block;
  padding: 3px 8px;
  color: var(--jade);
  font-size: 9px;
  background: var(--jade-light);
  border-radius: 999px;
}

.transport-card__title {
  display: block;
  margin-top: 7px;
  color: var(--ink);
  font-family: var(--serif);
  font-size: 17px;
  font-weight: 600;
}

.transport-card__text {
  display: block;
  margin-top: 4px;
  color: var(--muted);
  font-size: 10px;
  line-height: 1.7;
}

@media (min-width: 900px) {
  .plan-facts__grid {
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
  }

  .plan-tools {
    flex-direction: row;
    align-items: flex-start;
  }

  .budget-card,
  .checklist-card {
    flex: 1;
    padding: 26px;
  }

  .transport-grid {
    flex-direction: row;
  }

  .transport-card {
    flex: 1;
  }
}
</style>
