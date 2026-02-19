package me.sinz.sinz;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GFriendActivity extends AppCompatActivity {

    private final String GFRIEND_URL = "htt"+"ps://sin"+"z.me/"+"gfriend/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);
        Toolbar title = SinZ.createTitle(this, "여자친구 노래방 수록곡");
        title.setBackgroundColor(Color.parseColor("#5F4B8B"));
        layout.addView(title);

        WebView web = new WebView(this);
        web.clearCache(true);
        web.addJavascriptInterface(new WebLinker(), "SinZ");
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient());
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUserAgentString(settings.getUserAgentString() + " SinZ/" + SinZ.VERSION);
        web.loadUrl(GFRIEND_URL);
        web.setBackgroundColor(Color.WHITE);
        web.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        layout.addView(web);

        setContentView(layout);
        SinZ.preventEdgeToEdge(this);
    }

    private class WebLinker {

        @JavascriptInterface
        public void toast(final String msg) {
            new Handler().post(() -> runOnUiThread(()->SinZ.toast(GFriendActivity.this, msg).show()));
        }

    }
}
