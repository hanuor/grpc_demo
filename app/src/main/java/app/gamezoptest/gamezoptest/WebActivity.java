package app.gamezoptest.gamezoptest;
/*
 * Created by Han
 *Vamos!
 *
 */

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;

public class WebActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        WebView webView = (WebView) findViewById(R.id.webView);
        String getPath = getIntent().getStringExtra("path");
        WebChromeClient webClient = new WebChromeClient() {
            // Override page so it's load on my view only

        };
        webView.setWebChromeClient(webClient);
        webView.getSettings().setAppCacheMaxSize(10 * 1024 * 1024);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowContentAccess(true);

        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.loadUrl("file:///" + Environment.getExternalStorageDirectory().toString() + File.separator + getPath + "index.html");
        GameActivity.progress.dismiss();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
