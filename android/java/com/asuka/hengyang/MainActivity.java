package com.asuka.hengyang;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 将 uni-app 的 H5 构建产物封装为可安装的 Android WebView APK。
 *
 * 注意：这是 H5 WebView 壳，不是 DCloud 原生 App 运行时。
 * 页面的筛选、收藏、清单、tabBar 等逻辑全部运行打包进 assets 的 H5 构建产物。
 */
public class MainActivity extends Activity {
    private static final String HOST = "https://appassets.androidplatform.net";
    private static final String BASE_PATH = "/hengyang-uniapp/";
    private static final String START_URL = HOST + BASE_PATH + "index.html";

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureWindow();

        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(244, 239, 228));
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setSupportMultipleWindows(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setTextZoom(100);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new LocalAssetClient());

        setContentView(webView);
        webView.loadUrl(START_URL);
    }

    private void configureWindow() {
        Window window = getWindow();
        window.setStatusBarColor(Color.rgb(244, 239, 228));
        window.setNavigationBarColor(Color.rgb(251, 248, 241));

        int flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }
        window.getDecorView().setSystemUiVisibility(flags);
    }

    private class LocalAssetClient extends WebViewClient {
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            Uri uri = request.getUrl();
            if (uri == null || !"https".equals(uri.getScheme()) || !HOST.equals(uri.getScheme() + "://" + uri.getHost())) {
                return null;
            }
            return serveAsset(uri.getPath());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri uri = request.getUrl();
            if (uri == null) {
                return true;
            }
            String origin = uri.getScheme() + "://" + uri.getHost();
            return !HOST.equals(origin);
        }
    }

    private WebResourceResponse serveAsset(String path) {
        if (path == null || BASE_PATH.equals(path)) {
            path = BASE_PATH + "index.html";
        }
        if (!path.startsWith(BASE_PATH)) {
            return notFound();
        }

        String assetPath = "hengyang-uniapp/" + path.substring(BASE_PATH.length());
        try {
            InputStream stream = getAssets().open(assetPath);
            String mime = mimeType(assetPath);
            String encoding = isText(mime) ? "UTF-8" : null;
            Map<String, String> headers = new HashMap<>();
            headers.put("Cache-Control", "no-cache");
            headers.put("Access-Control-Allow-Origin", "*");
            return new WebResourceResponse(mime, encoding, 200, "OK", headers, stream);
        } catch (IOException error) {
            return notFound();
        }
    }

    private WebResourceResponse notFound() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain; charset=utf-8");
        return new WebResourceResponse(
                "text/plain",
                "UTF-8",
                404,
                "Not Found",
                headers,
                new ByteArrayInputStream("Not Found".getBytes())
        );
    }

    private boolean isText(String mime) {
        return mime.startsWith("text/")
                || mime.contains("javascript")
                || mime.contains("json")
                || mime.contains("xml")
                || mime.contains("svg");
    }

    private String mimeType(String path) {
        String lower = path.toLowerCase();
        if (lower.endsWith(".html") || lower.endsWith(".htm")) return "text/html";
        if (lower.endsWith(".css")) return "text/css";
        if (lower.endsWith(".js") || lower.endsWith(".mjs")) return "application/javascript";
        if (lower.endsWith(".json")) return "application/json";
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".svg")) return "image/svg+xml";
        if (lower.endsWith(".ico")) return "image/x-icon";
        if (lower.endsWith(".woff2")) return "font/woff2";
        if (lower.endsWith(".woff")) return "font/woff";
        if (lower.endsWith(".ttf")) return "font/ttf";
        return "application/octet-stream";
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.loadUrl("about:blank");
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
