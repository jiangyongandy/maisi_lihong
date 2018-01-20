package org.cocos2dx.lua.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test_webview_demo.utils.X5WebView;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;
import com.zuiai.nn.R;

import org.cocos2dx.lua.CommonConstant;
import org.cocos2dx.lua.DataHelper;
import org.cocos2dx.lua.Logger;
import org.cocos2dx.lua.VipHelperUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserActivity extends BaseActivity  {
    private static final String IS_FIRST_PLAY = "IS_FIRST_PLAY";
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private X5WebView mWebView;
    private ViewGroup mViewParent;
    private ImageButton mBack;
    private ImageButton mForward;
    private ImageButton mExit;
    private ImageButton mHome;
    //    private Button mGo;
    private ImageView mPlayerBtn;
    private RelativeLayout mRlGuide;

    private ImageButton mMore;
    private TextView mTvKnow;
    //    private EditText mUrl;
    //	private static final String mHomeUrl = "http://www.youku.com/v_feelist/pt_1.html";
    private final String mHomeUrl = VipHelperUtils.getInstance().getURLbyPositon();

    private static final String TAG = "SdkDemo";
    private static final int MAX_LENGTH = 14;

    private boolean mNeedTestPage = false;

    private final int disable = 120;
    private final int enable = 255;
    private ValueCallback<Uri> uploadFile;
    private URL mIntentUrl;
    private TextView mChangeLine;
    private boolean isPcUA;
    private String mobileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        Intent intent = getIntent();
        if (intent != null) {
            try {
                mIntentUrl = new URL(intent.getData().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
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
        setContentView(R.layout.activity_browse1);
        mViewParent = (ViewGroup) findViewById(R.id.webView1);

        initBtnListenser();

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

    private void init() {

        mWebView = new X5WebView(this, null);

        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));

        mWebView.addJavascriptInterface(new JSHook(), "jsHook");

        mWebView.setWebViewClient(new WebViewClient() {

            /**
             * 防止加载网页时调起系统浏览器
             */
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("should", url);
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
                Log.i("onPageStarted", s);
                super.onPageStarted(webView, s, bitmap);
                if(isPcUA && !s.equals(mobileUrl)) {
                    Log.i("PC页面加载开始","--------------"+ s);
                    isPcUA = false;
                    webView.stopLoading();
                    Log.i("恢复移动UA","--------------"+ s);
                    WebSettings webSetting = mWebView.getSettings();
                    webSetting.setUserAgentString("User-Agent: MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
                    Intent intent = new Intent(BrowserActivity.this, PlayActivity.class);
                    intent.putExtra("URL", s);
                    VipHelperUtils.getInstance().setCurrentPlayUrl(s);
                    BrowserActivity.this.startActivity(intent);
                    return;
                }
                if(VipHelperUtils.getInstance().getCurrentPosition() == 5) {
                }else {
                    webView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            webView.loadUrl(VipHelperUtils.getInstance().changePlayURLbyPositon(BrowserActivity.this));
                        }
                    }, 3000L);
                }
            }

            @Override
            public void onPageFinished(final WebView webView, String s) {
                Log.i("onPageFinished", s);
                super.onPageFinished(webView, s);
//				Toast.makeText(getApplicationContext(), "页面加载完成", Toast.LENGTH_SHORT).show();
                if(VipHelperUtils.getInstance().getCurrentPosition() == 4) {
                    webView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            webView.loadUrl(VipHelperUtils.getInstance().changePlayURLbyPositon(BrowserActivity.this));
                        }
                    }, 5000L);
                    return;
                }

                if(VipHelperUtils.getInstance().getCurrentPosition() == 5 ) {
                    webView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            webView.loadUrl(VipHelperUtils.getInstance().changePlayURLbyPositon(BrowserActivity.this));
                        }
                    }, 3000L);
                }else {
//                    webView.loadUrl(VipHelperUtils.getInstance().changePlayURLbyPositon(BrowserActivity.this));
                }
//                webView.loadUrl(VipHelperUtils.getInstance().changePlayURLbyPositon());
                // mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
                mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    changGoForwardButton(webView);
                /* mWebView.showLog("test Log"); */
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
                FrameLayout normalView = (FrameLayout) findViewById(R.id.web_filechooser);
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
//                if (mPageLoadingProgressBar == null)
//                    return;
//                if (i < 100) {
//                    mPageLoadingProgressBar.setVisibility(View.VISIBLE);
//                } else if (i >= 100) {
//                    mPageLoadingProgressBar.setVisibility(View.GONE);
////                    invalidateOptionsMenu();
//                }
//                mPageLoadingProgressBar.setProgress(i);
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {
                TbsLog.d(TAG, "url: " + arg0);
                new AlertDialog.Builder(BrowserActivity.this)
                        .setTitle("允许下载吗？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Toast.makeText(
                                                BrowserActivity.this,
                                                "准备下载...",
                                                1000).show();
                                    }
                                })
                        .setNegativeButton("否",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                BrowserActivity.this,
                                                "拒绝下载...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                BrowserActivity.this,
                                                "取消下载...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
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
        if (mIntentUrl == null) {
            //默认移动UA
            webSetting.setUserAgentString("User-Agent: MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
        }
        //优酷，土豆，芒果PC_UA
        switch (VipHelperUtils.getInstance().getCurrentPosition()) {
            case 3:
            case 4:
            case 5:
                Log.i("切换UA", "优酷，土豆，芒果PC版面--------------" );
                webSetting.setUserAgentString("User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)");
                break;
        }
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();
        if (mIntentUrl == null) {
            mWebView.loadUrl(mHomeUrl);
        } else {
            mWebView.loadUrl(mIntentUrl.toString());
            Logger.showLog("-----------" + mIntentUrl.toString(),"webView load url ---");
        }
        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

    private void initBtnListenser() {
        mBack = (ImageButton) findViewById(R.id.btnBack1);
        mForward = (ImageButton) findViewById(R.id.btnForward1);
        mExit = (ImageButton) findViewById(R.id.btnExit1);
        mHome = (ImageButton) findViewById(R.id.btnHome1);
//        mGo = (Button) findViewById(R.id.btnGo1);
//        mUrl = (EditText) findViewById(R.id.editUrl1);
        mMore = (ImageButton) findViewById(R.id.btnMore);
        mPlayerBtn = (ImageView) findViewById(R.id.iv_vip_player);
        mRlGuide = (RelativeLayout) findViewById(R.id.rl_guide);
        mTvKnow = (TextView) findViewById(R.id.tv_know);
        if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
            mBack.setAlpha(disable);
            mForward.setAlpha(disable);
            mHome.setAlpha(disable);
        }
        mHome.setEnabled(false);


        if (mIntentUrl != null) {
            mPlayerBtn.setVisibility(View.GONE);
        }
        if (!CommonConstant.isRelease) {
            mPlayerBtn.setVisibility(View.GONE);
        }

        if(CommonConstant.isRelease) {

            boolean isFirstRun = DataHelper.getBoolSp(this, IS_FIRST_PLAY, true);
            if(isFirstRun) {
                mRlGuide.setVisibility(View.VISIBLE);
                mTvKnow.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRlGuide.setVisibility(View.GONE);
                    }
                });
            }
            DataHelper.setBoolSF(this, IS_FIRST_PLAY, false);
        }

        mPlayerBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTestHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        String url = mWebView.getUrl();
                        String url1 = mWebView.getOriginalUrl();
                        Log.i("获取Url", "获取Url--------------" + url + "原始url--------" + url1);
                        //腾讯UA特殊处理（移动版面，PC接口）
                        if(VipHelperUtils.getInstance().getCurrentPosition() == 1) {

                            Log.i("切换UA", "腾讯UA--------------" + url);
                            WebSettings webSetting = mWebView.getSettings();
                            webSetting.setUserAgentString("User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)");
                            isPcUA = true;
                            mobileUrl = url;
                            mWebView.loadUrl(url);
                            return;
                        }

                        Intent intent = new Intent(BrowserActivity.this, PlayActivity.class);
                        intent.putExtra("URL", url);
                        VipHelperUtils.getInstance().setCurrentPlayUrl(url);
                        BrowserActivity.this.startActivity(intent);


                    }
                });
            }
        });
        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebView != null && mWebView.canGoBack())
                    mWebView.goBack();
            }
        });

        mForward.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebView != null && mWebView.canGoForward())
                    mWebView.goForward();
            }
        });

      /*  mGo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = mUrl.getText().toString();
                mWebView.loadUrl(url);
                mWebView.requestFocus();
            }
        });*/

        mMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(BrowserActivity.this, "not completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        /*mUrl.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mGo.setVisibility(View.VISIBLE);
                    if (null == mWebView.getUrl())
                        return;
                    if (mWebView.getUrl().equalsIgnoreCase(mHomeUrl)) {
                        mUrl.setText("");
                        mGo.setText("首页");
                        mGo.setTextColor(0X6F0F0F0F);
                    } else {
                        mUrl.setText(mWebView.getUrl());
                        mGo.setText("进入");
                        mGo.setTextColor(0X6F0000CD);
                    }
                } else {
                    mGo.setVisibility(View.GONE);
                    String title = mWebView.getTitle();
                    if (title != null && title.length() > MAX_LENGTH)
                        mUrl.setText(title.subSequence(0, MAX_LENGTH) + "...");
                    else
                        mUrl.setText(title);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }

        });*/

       /* mUrl.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                String url = null;
                if (mUrl.getText() != null) {
                    url = mUrl.getText().toString();
                }

                if (url == null
                        || mUrl.getText().toString().equalsIgnoreCase("")) {
                    mGo.setText("请输入网址");
                    mGo.setTextColor(0X6F0F0F0F);
                } else {
                    mGo.setText("进入");
                    mGo.setTextColor(0X6F0000CD);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }
        });*/

        mHome.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebView != null)
                    mWebView.loadUrl(mHomeUrl);
            }
        });

        mExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Process.killProcess(Process.myPid());
            }
        });
    }

    boolean[] m_selected = new boolean[]{true, true, true, true, false,
            false, true};

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    changGoForwardButton(mWebView);
                return true;
            } else
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode
                + ",resultCode:" + resultCode);

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
        public void goToPlayPage(final String url) {
            mTestHandler.post(new Runnable() {
                @Override
                public void run() {

                    //腾讯UA特殊处理（移动版面，PC接口）
                    if(VipHelperUtils.getInstance().getCurrentPosition() == 1) {

                        Log.i("切换UA", "腾讯UA--------------" + url);
                        WebSettings webSetting = mWebView.getSettings();
                        webSetting.setUserAgentString("User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)");
                        isPcUA = true;
                        mobileUrl = url;
                        mWebView.loadUrl(url);
                        return;
                    }

                    Intent intent = new Intent(BrowserActivity.this, PlayActivity.class);
                    intent.putExtra("URL", url);
                    VipHelperUtils.getInstance().setCurrentPlayUrl(url);
                    BrowserActivity.this.startActivity(intent);


                }
            });

        }

        @JavascriptInterface
        public void go2Site(int position) {
            VipHelperUtils.getInstance().changeCurrentSite(position);
            Intent intent = new Intent(BrowserActivity.this, BrowserActivity.class);
            BrowserActivity.this.startActivity(intent);
        }

    }

}
