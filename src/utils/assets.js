// #ifdef H5
const STATIC_BASE = import.meta.env.BASE_URL;
// #endif
// #ifndef H5
const STATIC_BASE = '/';
// #endif

/**
 * 取得 static/images 下的图片路径。
 * H5 使用 Vite base，微信小程序 / App 使用 /static。
 */
export function img(name) {
  return `${STATIC_BASE}static/images/${name}.webp`;
}

export function goTab(path) {
  uni.switchTab({ url: path });
}

export function goPage(path) {
  uni.navigateTo({ url: path });
}
