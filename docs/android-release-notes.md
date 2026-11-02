# 雁城衡阳 Android APK v1.0.1

这是 uni-app 项目的 H5 构建产物的 Android WebView 封装版，离线加载页面资源。

- 包名：com.asuka.hengyang
- 版本：1.0.1 (versionCode 2)
- minSdk：24 / targetSdk：35
- 签名：debug keystore（仅安装测试）
- SHA-256：7975136fb2cd3f1e74c161bd7012f025cf9fa3bf09830239bc03b558e0c7f876

## 本次修复

v1.0.0 首次启动闪退的原因是 D8 只打包了 `MainActivity.class`，没有把内部类 `MainActivity$LocalAssetClient` 写入 `classes.dex`。v1.0.1 已把 `javac` 生成的全部 `.class` 文件交给 D8，并通过 `apkanalyzer` 确认内部类已定义。

## 安装注意

v1.0.0 与 v1.0.1 使用不同的 debug 签名证书，不能直接覆盖安装。请先卸载旧版 `com.asuka.hengyang`，再安装 v1.0.1。

注意：这不是 DCloud 原生 App 运行时；原生 APK 需 HBuilderX 登录 DCloud 后云打包。
