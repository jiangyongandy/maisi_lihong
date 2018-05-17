package org.cocos2dx.lua.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.test_webview_demo.utils.X5WebView;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import org.cocos2dx.lua.VipHelperUtils;

public class PlayActivity extends BaseActivity  {
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private X5WebView mWebView;
    private ViewGroup mViewParent;
    private ImageButton mBack;
    private ImageButton mForward;
    private ImageButton mExit;
    private ImageButton mHome;
    private ImageButton mMore;
    private Button mGo;
    private EditText mUrl;

    //	private static final String mHomeUrl = "http://www.youku.com/v_feelist/pt_1.html";
    private static final String mHomeUrl = "https://v.qq.com/";
    private static final String TAG = "SdkDemo";
    private static final int MAX_LENGTH = 14;
    private boolean mNeedTestPage = false;

    private final int disable = 120;
    private final int enable = 255;

    private ProgressBar mPageLoadingProgressBar = null;

    private ValueCallback<Uri> uploadFile;

    private String mIntentUrl;
    private TextView mChangeLine;
    private TextView mShare;
    private ViewGroup mViewMask;
    private View mAdView;
//    private int resumeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Intent intent = getIntent();
        if (intent != null) {
            try {
                mIntentUrl = intent.getStringExtra("URL");
            } catch (NullPointerException e) {

            } catch (Exception e) {
            }
        }
        //
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }

		/*
         * getWindow().addFlags(
		 * android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
        setContentView(com.example.test_webview_demo.R.layout.activity_play);
        mViewParent = (ViewGroup) findViewById(com.example.test_webview_demo.R.id.webView1);
        mViewMask = (ViewGroup) findViewById(com.example.test_webview_demo.R.id.fl_mask);
        mAdView =  findViewById(com.example.test_webview_demo.R.id.iv_ad);
        mAdView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);

    }

    private void changGoForwardButton(WebView view) {
        if (view.canGoBack())
            mBack.setAlpha(enable);
        else
            mBack.setAlpha(disable);
        if (view.canGoForward())
            mForward.setAlpha(enable);
        else
            mForward.setAlpha(disable);
        if (view.getUrl() != null && view.getUrl().equalsIgnoreCase(mHomeUrl)) {
            mHome.setAlpha(disable);
            mHome.setEnabled(false);
        } else {
            mHome.setAlpha(enable);
            mHome.setEnabled(true);
        }
    }

    private void initProgressBar() {
        mPageLoadingProgressBar = (ProgressBar) findViewById(com.example.test_webview_demo.R.id.progressBar1);// new
        mChangeLine = (TextView) findViewById(com.example.test_webview_demo.R.id.tv_change_line);// new
        mShare = (TextView) findViewById(com.example.test_webview_demo.R.id.tv_share);
        // ProgressBar(getApplicationContext(),
        // null,
        // android.R.attr.progressBarStyleHorizontal);
        mPageLoadingProgressBar.setMax(100);
        mPageLoadingProgressBar.setProgressDrawable(this.getResources()
                .getDrawable(com.example.test_webview_demo.R.drawable.color_progressbar));

        mChangeLine.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                VipHelperUtils.getInstance().changePlayLine(mWebView);
                mViewMask.setVisibility(View.VISIBLE);
            }
        });
        mShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("" + VipHelperUtils.getInstance().getCurrentApi() + mIntentUrl);
                intent.setData(content_url);
                startActivity(Intent.createChooser(intent, "请选择浏览器"));
            }
        });
    }

    private void init() {

        mWebView = new X5WebView(this, null);

        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));

        initProgressBar();

    /*    mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                *//*if(event.getAction() == MotionEvent.ACTION_DOWN) {

                    Toast.makeText(PlayActivity.this, "setOnTouchdianji", Toast.LENGTH_LONG).show();
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            View childAt = mWebView.getChildAt(0);
                            if(childAt != null) {
                                if(childAt instanceof  ViewGroup) {
                                    ViewGroup.LayoutParams layoutParams3 = childAt.getLayoutParams();
                                    layoutParams3.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                    layoutParams3.height = ViewGroup.LayoutParams.MATCH_PARENT;
                                    childAt.setLayoutParams(layoutParams3);
                                    View childAt1 = ((ViewGroup) childAt).getChildAt(0);
                                    if(childAt1 != null) {

                                        ViewGroup.LayoutParams layoutParams = childAt1.getLayoutParams();
                                        layoutParams.width = 500;
                                        layoutParams.height = 500;
                                        childAt1.setLayoutParams(layoutParams);
                                        if(childAt1 instanceof ViewGroup) {
                                            View childAt2 = ((ViewGroup) childAt).getChildAt(0);
                                            if(childAt2 != null) {

                                                ViewGroup.LayoutParams layoutParams2 = childAt2.getLayoutParams();
                                                layoutParams2.width = 400;
                                                layoutParams2.height = 400;
                                                childAt2.setLayoutParams(layoutParams);
                                                mViewMask.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }, 200);
                }
                return false;
            }
        });*/

        mWebView.addJavascriptInterface(new JSHook(), "jsHook");

        mWebView.setWebViewClient(new WebViewClient() {

            /**
             * 防止加载网页时调起系统浏览器
             */
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("onPageFinished", url);
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return false;
                } else {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent);
                    return true;
                }
            }

            @Override
            public void onPageStarted(final WebView webView, String s, Bitmap bitmap) {
                Log.i("onPageFinished", s);
                super.onPageStarted(webView, s, bitmap);
                mViewMask.setVisibility(View.VISIBLE);
                VipHelperUtils.getInstance().changeTitle(webView);
//                mWebView.loadUrl(VipHelperUtils.getInstance().pingbiAD());
                webView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        VipHelperUtils.getInstance().changeTitle(webView);
//                        mWebView.loadUrl(VipHelperUtils.getInstance().pingbiAD());
//                        webView.loadUrl(VipHelperUtils.getInstance().changePlayURLbyPositon());
                    }
                }, 1000);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                Log.i("onPageFinished", s);
                super.onPageFinished(webView, s);
                VipHelperUtils.getInstance().changeTitle(webView);
//                mWebView.loadUrl(VipHelperUtils.getInstance().pingbiAD());
                mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
/*                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    changGoForwardButton(webView);*/
				/* mWebView.showLog("test Log"); */
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                Log.i("onReceivedError", "code--"+i+"  "+ s + "  "+s1);
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                Log.i("WebResourceError", "code--"+webResourceError.getErrorCode()+ "  "+ webResourceError.getDescription());
            }

            @Override
            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                Log.i("onReceivedHttpError", "code--"+webResourceResponse.getStatusCode()+"  "+ webResourceResponse.getReasonPhrase());
                VipHelperUtils.getInstance().changePlayLine(mWebView);
                mViewMask.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                Log.i("onReceivedSslError", "code--onReceivedSslErroronReceivedSslError");
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
                                       JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            View myVideoView;
            View myNormalView;
            CustomViewCallback callback;

            // /////////////////////////////////////////////////////////
            //

            /**
             * 全屏播放配置
             */
            @Override
            public void onShowCustomView(View view,
                                         CustomViewCallback customViewCallback) {
                FrameLayout normalView = mWebView;
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                myVideoView = view;
                myNormalView = normalView;
                callback = customViewCallback;
            }

            @Override
            public void onHideCustomView() {
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }
                if (myVideoView != null) {
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
            }

            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2,
                                     JsResult arg3) {
                /**
                 * 这里写入你自定义的window alert
                 */
                return super.onJsAlert(null, arg1, arg2, arg3);
            }

            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (mPageLoadingProgressBar == null)
                    return;
                if (i < 100) {
                    mPageLoadingProgressBar.setVisibility(View.VISIBLE);
                } else if (i >= 100) {
                    mPageLoadingProgressBar.setVisibility(View.GONE);
//                    invalidateOptionsMenu();
                }
                mPageLoadingProgressBar.setProgress(i);
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {
                TbsLog.d(TAG, "url: " + arg0);
/*                new AlertDialog.Builder(PlayActivity.this)
                        .setTitle("allow to download？")
                        .setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Toast.makeText(
                                                PlayActivity.this,
                                                "fake message: i'll download...",
                                                1000).show();
                                    }
                                })
                        .setNegativeButton("no",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                PlayActivity.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                PlayActivity.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).show();*/
            }
        });

        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setAllowFileAccessFromFileURLs(true);
        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setUserAgentString("User-Agent: MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 5.0; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        enableX5FullscreenFunc();
//        enableLiteWndFunc();
        long time = System.currentTimeMillis();
        if (mIntentUrl == null) {
            mWebView.loadUrl(mHomeUrl);
        } else {
//            Toast.makeText(PlayActivity.this, "" + mIntentUrl, Toast.LENGTH_SHORT).show();
//            mWebView.loadUrl(VipHelperUtils.getInstance().getCurrentApi() + mIntentUrl);
            mWebView.loadUrl("file:///android_asset/play3.html");
//            mWebView.loadUrl(VipHelperUtils.getInstance().getDefaultApi() + mIntentUrl);
//            mWebView.loadUrl("http://hoooz.cn/v1/#");
            Log.i("vipvideo_url_pa", VipHelperUtils.getInstance().getDefaultApi() + mIntentUrl);
        }
        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("温馨提示")
                .content(VipHelperUtils.getInstance().getTips())
                .positiveText("知道了")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();

        /*new AlertDialog.Builder(this)
                .setTitle(VipHelperUtils.getInstance().getTips())
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        })
                .setNegativeButton("否",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub

                            }
                        })
                .setOnCancelListener(
                        new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                // TODO Auto-generated method stub
                                Toast.makeText(
                                        PlayActivity.this,
                                        "取消...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).show();*/
    }

    boolean[] m_selected = new boolean[]{true, true, true, true, false,
            false, true};

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
/*        resumeCount++;
//        Toast.makeText(PlayActivity.this, "onActivityResult, requestCode:    " + resumeCount, Toast.LENGTH_SHORT).show();
        if(resumeCount > 1)
            finish();*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

/*        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }*/
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode
                + ",resultCode:" + resultCode);
        Toast.makeText(PlayActivity.this, "onActivityResult, requestCode:" + requestCode + ",resultCode:" + resultCode, Toast.LENGTH_SHORT).show();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (null != uploadFile) {
                        Uri result = data == null || resultCode != RESULT_OK ? null
                                : data.getData();
                        uploadFile.onReceiveValue(result);
                        uploadFile = null;
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (null != uploadFile) {
                uploadFile.onReceiveValue(null);
                uploadFile = null;
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || mWebView == null || intent.getData() == null)
            return;
        mWebView.loadUrl(intent.getData().toString());
    }

    @Override
    protected void onDestroy() {
        if (mTestHandler != null)
            mTestHandler.removeCallbacksAndMessages(null);
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }

    // /////////////////////////////////////////
    // 向webview发出信息
    private void enableX5FullscreenFunc() {

        if (mWebView.getX5WebViewExtension() != null) {
//            Toast.makeText(this, "开启X5全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void disableX5FullscreenFunc() {
        if (mWebView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "恢复webkit初始状态", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", true);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enableLiteWndFunc() {
        if (mWebView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启小窗模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    private void enablePageVideoFunc() {
        if (mWebView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "页面内全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    public static final int MSG_OPEN_TEST_URL = 0;
    public static final int MSG_INIT_UI = 1;
    private final int mUrlStartNum = 0;
    private int mCurrentUrl = mUrlStartNum;
    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN_TEST_URL:
                    if (!mNeedTestPage) {
                        return;
                    }

                    String testUrl = "file:///sdcard/outputHtml/html/"
                            + Integer.toString(mCurrentUrl) + ".html";
                    if (mWebView != null) {
                        mWebView.loadUrl(testUrl);
                    }

                    mCurrentUrl++;
                    break;
                case MSG_INIT_UI:
                    init();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public class JSHook {

        @JavascriptInterface
        public void hideMask() {
            mViewMask.setVisibility(View.GONE);
        }

        @JavascriptInterface
        public void loadPlayPage() {
/*            mWebView.loadUrl("javascript:" +
                    "$(\"#iframe\").attr(\"src\",\""+VipHelperUtils.getInstance().getDefaultApi() + mIntentUrl +"\");\t");*/
            mWebView.loadUrl("javascript:" +
                    "$(\"#proj_Iframe\").attr(\"src\",\""+VipHelperUtils.getInstance().getDefaultApi() + mIntentUrl +"\");\t");
        }

    }

}
