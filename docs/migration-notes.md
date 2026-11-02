# 从 exp4 Vue 网站迁移到 uni-app

## 1. 迁移目标

`exp4` 是一个 Vue 3 + Vue Router + Vite 的纯 H5 单页应用。`exp5` 保留同一套视觉、数据和四个核心页面，但把运行载体改为 uni-app，使其可以被编译到：

- H5
- 微信小程序（mp-weixin）
- App（app-plus / Android 基座）

## 2. 结构对照

| exp4（Vue Web） | exp5（uni-app） |
| --- | --- |
| `src/views/HomeView.vue` | `src/pages/index/index.vue` |
| `src/views/SceneryView.vue` | `src/pages/scenery/scenery.vue` |
| `src/views/FoodView.vue` | `src/pages/food/food.vue` |
| `src/views/PlanView.vue` | `src/pages/plan/plan.vue` |
| `src/router/index.js` | `src/pages.json` + `uni.switchTab` |
| `src/App.vue` 全局样式 | `src/App.vue` + `src/styles/main.css` |
| `src/assets/images` | `src/static/images` |
| 15+ 个 Web 组件 | 14 个跨端组件 + 4 个页面 + App.vue |

## 3. 关键 API 替换

| Web / Vue Router | uni-app |
| --- | --- |
| `createWebHistory()` | `pages.json` 页面栈；H5 使用 hash 路由 |
| `<RouterLink to="/scenery">` | `<view @tap="uni.switchTab({ url: '/pages/scenery/scenery' })">` |
| `route.query` | `onLoad(options)` 或本地状态 |
| `window.localStorage` | `uni.getStorageSync` / `uni.setStorageSync` |
| `window.scrollTo()` | `uni.pageScrollTo()` |
| `document.title` | `pages.json` 的 `navigationBarTitleText` |
| `window.matchMedia()` | 移除；使用系统主题/本地设置 |
| `IntersectionObserver` + `v-reveal` | 移除自定义滚动入场指令 |
| `<Teleport to="body">` | 页面内 `position: fixed` 全屏弹层 |
| `<Transition>` / `<TransitionGroup>` | CSS 动画/过渡；列表不依赖过渡组件 |
| `div / section / header / footer / p / h1` | `view / text` |
| `img` | `image`（`mode="aspectFill"`） |
| `ul / li` | `view` 列表 |
| `select` | `picker` |
| `input[type=range]` | `slider` |
| `checkbox` | 自定义 `view` 勾选行 + `uni` 存储 |

## 4. 跨端样式处理

- 统一使用 `px` 编写移动端尺寸，在 H5 桌面端用 `@media (min-width: 900px)` 扩展为双列/三列。
- 颜色、圆角、阴影通过 `.page` 上的 CSS 变量集中管理；`.theme-dark` 覆盖变量实现日/夜主题。
- 图片使用 `<image mode="aspectFill">`，不再依赖 CSS `object-fit` 的 Web 实现。
- 横向筛选使用 `<scroll-view scroll-x>`，避免小程序中 `overflow-x` 的兼容问题。
- 打开详情弹层时隐藏原生 tabBar，关闭时恢复，避免弹层被 tabBar 遮挡。
- 移除 `backdrop-filter`、`color-mix()`、`clamp()` 等小程序兼容性不稳定的属性。

## 5. 数据与状态

- `src/data/attractions.js`、`foods.js`、`itinerary.js` 从 `exp4` 迁移，图片字段改为 `img('name')`。
- `src/utils/assets.js` 使用条件编译：
  - H5：`import.meta.env.BASE_URL + 'static/images/...'`，适配 GitHub Pages 子路径；
  - 小程序 / App：`/static/images/...`。
- `useFavorites` 保存收藏 id 数组；`useTheme` 保存 `light/dark`。
- 行程清单使用 `uni.getStorageSync('hengyang_uni_packing')` 持久化。

## 6. 构建与验证

```bash
pnpm build:h5
pnpm build:mp-weixin
pnpm build:app
```

产物：

- `dist/build/h5`：可部署的静态站点；
- `dist/build/mp-weixin`：微信开发者工具导入目录；
- `dist/build/app`：HBuilderX App 资源目录。

H5 使用 390×844 的设备模拟与 1440×1100 桌面窗口检查；分类筛选、景点弹窗、收藏、清单勾选均已验证，预算按公式实时计算。

## 7. 终端限制

- **微信小程序**：本机未安装微信开发者工具，HBuilderX CLI 编译成功后停止在“打开模拟器”阶段。导入 `dist/build/mp-weixin` 即可继续真机调试。
- **Android**：本机未连接 Android 设备，也未安装 Android SDK/模拟器；HBuilderX `launch app-android --compile true` 编译成功，但无法安装运行。APK 需要 HBuilderX 云打包，并登录 DCloud 账号、配置包名和证书。
- 微信小程序 AppID 当前为 `touristappid`，正式上传前需替换为自己的 AppID 和上传密钥。

## 8. 后续可改进

- 增加 404 / 网络错误页；
- 将静态数据迁移到 uniCloud 或后端 API；
- 增加地图、天气、路线规划；
- 使用 `uni.getLocation` 与地图组件增强行程页；
- 补充小程序真机截图与 Android APK。
