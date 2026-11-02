# Android APK（WebView 封装）

这里放的是把 `dist/build/h5` 封装成 Android APK 的最小工程。

> 这不是 DCloud 原生 App 运行时，而是一个 Android WebView 壳：
> APK 内离线加载 uni-app 的 H5 构建产物，页面和交互与 H5 一致。
> 真正的 DCloud 原生 APK 需要 HBuilderX 登录 DCloud 后云打包，配置见根目录 README。

## 构建产物

- APK：`../dist/hengyang-uniapp.apk`
- 包名：`com.asuka.hengyang`
- minSdk：24（Android 7.0）
- targetSdk：35
- 签名：debug keystore（仅用于安装测试，不能上架应用商店）

## 依赖

- JDK 17+（本机为 Java 21）
- Android SDK Platform 35
- Android Build-Tools 35.0.0

默认 SDK 路径为 `D:\AndroidSdk`，可通过环境变量覆盖：

```powershell
$env:ANDROID_SDK_ROOT = "D:\AndroidSdk"
$env:ANDROID_BUILD_TOOLS = "35.0.0"
$env:ANDROID_PLATFORM = "android-35"
```

## 构建

先构建 H5：

```bash
cd exp5
pnpm build:h5
```

再构建 APK：

```powershell
cd exp5\android
python build_apk.py
```

输出：

```text
exp5/dist/hengyang-uniapp.apk
```

> Windows 下 AAPT2 对中文路径可能报 “failed to open directory”。
> 如果项目路径包含中文，请把 `android` 目录和 `dist/build/h5` 复制到纯英文路径（例如 `D:\hengyang-apk`）再执行，或整体在英文路径下构建。

## 实现方式

- `MainActivity` 创建全屏 WebView；
- `shouldInterceptRequest` 把 `https://appassets.androidplatform.net/hengyang-uniapp/...` 映射到 `assets/hengyang-uniapp/...`；
- 页面路由使用 H5 的 hash 路由；
- 开启 DOM Storage，收藏和行前清单可持久化；
- 返回键优先交给 WebView 历史；
- 不申请 `INTERNET` 权限，离线运行；
- 状态栏/导航栏使用米色主题。

## 真机安装

```bash
adb install -r exp5/dist/hengyang-uniapp.apk
```

当前构建环境没有连接 Android 设备或模拟器，因此只完成了 APK 构建、签名和 `aapt2`/`apksigner` 静态校验，未做真机安装截图。
