package org.cocos2dx.lua.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.zuiai.nn.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 功能
 * Created by Jiang on 2017/10/16.
 */

public class ShareActivity extends BaseActivity {


    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview.destroy();
    }

    private void initData() {
        webview.addJavascriptInterface(new PictureJavaScriptInterface(), "picturejs");
        webview.loadUrl("file:///android_asset/tui/tui.html");
        webview.postDelayed(new Runnable() {

            @Override
            public void run() {
                webview.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    final class PictureJavaScriptInterface {

        public PictureJavaScriptInterface() {

        }

        @JavascriptInterface
        public void onClick() {
            finish();
        }

    }
}
