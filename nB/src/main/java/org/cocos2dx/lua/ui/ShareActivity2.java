package org.cocos2dx.lua.ui;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;
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
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zuiai.nn.R;

import org.cocos2dx.lua.CommonConstant;
import org.cocos2dx.lua.DataHelper;
import org.cocos2dx.lua.Logger;
import org.cocos2dx.lua.ToastUtil;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.model.LiveModel;

import java.net.MalformedURLException;
import java.net.URL;

public class ShareActivity2 extends BaseActivity  {
    private static final String IS_FIRST_PLAY = "IS_FIRST_PLAY";
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private X5WebView mWebView;
    private ViewGroup mViewParent;
    //    private Button mGo;
    private ImageView mPlayerBtn;
    private RelativeLayout mRlGuide;

    private TextView mTvKnow;
    private String mHomeUrl = "file:///android_asset/tui/tui.html";

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
    private boolean isLiveType;
    private TextView mPlay;
    private TextView mClose;
    private View mNavGation;

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

        setContentView(R.layout.activity_browse1);
        mViewParent = (ViewGroup) findViewById(R.id.webView1);

        initBtnListenser();

        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);

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
            }

            @Override
            public void onPageFinished(final WebView webView, String s) {
                Log.i("onPageFinished", s);
                super.onPageFinished(webView, s);
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
//                BarUtils.setStatusBarVis;
                mNavGation.setVisibility(View.GONE);
                BarUtils.setStatusBarVisibility(ShareActivity2.this, false);
                ScreenUtils.setLandscape(ShareActivity2.this);
                FrameLayout normalView = (FrameLayout) mViewParent;
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
                    BarUtils.setStatusBarVisibility(ShareActivity2.this, true);
                    mNavGation.setVisibility(View.VISIBLE);
                    ScreenUtils.setPortrait(ShareActivity2.this);
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
                new AlertDialog.Builder(ShareActivity2.this)
                        .setTitle("允许下载吗？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Toast.makeText(
                                                ShareActivity2.this,
                                                "准备下载...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("否",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                ShareActivity2.this,
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
                                                ShareActivity2.this,
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
        if(isLiveType) {
            if(LiveModel.getInstance().isPCUAbyCurPosition()) {
                webSetting.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            }else {
//                webSetting.setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.2; Nexus 4 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.114 Mobile Safari/537.36");
                webSetting.setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.4; OPPO R7 Build/KTU84P) AppleWebKit/537.36 (KHTML,like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile ");
            }
        }else {
            webSetting.setUserAgentString("User-Agent: MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 5.0; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
            //优酷，土豆，芒果PC_UA
/*            switch (VipHelperUtils.getInstance().getCurrentPosition()) {
                case 3:
                case 4:
                case 5:
                    Log.i("切换UA", "优酷，土豆，芒果PC版面--------------" );
                    webSetting.setUserAgentString("User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)");
                    break;
            }*/
        }
        enableX5FullscreenFunc();
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
        mPlayerBtn = (ImageView) findViewById(R.id.iv_vip_player);
        mRlGuide = (RelativeLayout) findViewById(R.id.rl_guide);
        mTvKnow = (TextView) findViewById(R.id.tv_know);
        mPlay = (TextView) findViewById(R.id.tv_play);
        mClose = (TextView) findViewById(R.id.tv_close);
        mNavGation = findViewById(R.id.navigation1);
/*        if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
            mBack.setAlpha(disable);
            mForward.setAlpha(disable);
            mHome.setAlpha(disable);
        }
        mHome.setEnabled(false);*/
        mClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(isLiveType) {
            mPlay.setVisibility(View.GONE);
            mPlayerBtn.setVisibility(View.GONE);
        }

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
/*                        //腾讯UA特殊处理（移动版面，PC接口）
                        if(VipHelperUtils.getInstance().getCurrentPosition() == 1) {

                            Log.i("切换UA", "腾讯UA--------------" + url);
                            WebSettings webSetting = mWebView.getSettings();
                            webSetting.setUserAgentString("User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)");
                            isPcUA = true;
                            mobileUrl = url;
                            mWebView.loadUrl(url);
                            return;
                        }*/

                        Intent intent = new Intent(ShareActivity2.this, PlayActivity.class);
                        intent.putExtra("URL", url);
                        VipHelperUtils.getInstance().setCurrentPlayUrl(url);
                        ShareActivity2.this.startActivity(intent);


                    }
                });
            }
        });

    }

    // /////////////////////////////////////////
    // 向webview发出信息
    private void enableX5FullscreenFunc() {

        if (mWebView.getX5WebViewExtension() != null) {
//            Toast.makeText(this, "开启X5全屏播放模式", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();

            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data);
        }
    }

    boolean[] m_selected = new boolean[]{true, true, true, true, false,
            false, true};

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
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

        private UMShareListener listener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {

                ToastUtil.show(ShareActivity2.this, "分享成功！", Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                ToastUtil.show(ShareActivity2.this, "分享失败:"+throwable.getMessage(), Toast.LENGTH_SHORT);

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                ToastUtil.show(ShareActivity2.this, "分享取消！", Toast.LENGTH_SHORT);

            }
        };

        @JavascriptInterface
        public void goToPlayPage(final String url) {
            mTestHandler.post(new Runnable() {
                @Override
                public void run() {

/*                    //腾讯UA特殊处理（移动版面，PC接口）
                    if(VipHelperUtils.getInstance().getCurrentPosition() == 1) {

                        Log.i("切换UA", "腾讯UA--------------" + url);
                        WebSettings webSetting = mWebView.getSettings();
                        webSetting.setUserAgentString("User-Agent, Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)");
                        isPcUA = true;
                        mobileUrl = url;
                        mWebView.loadUrl(url);
                        return;
                    }*/

                    Intent intent = new Intent(ShareActivity2.this, PlayActivity.class);
                    intent.putExtra("URL", url);
                    VipHelperUtils.getInstance().setCurrentPlayUrl(url);
                    ShareActivity2.this.startActivity(intent);


                }
            });

        }

        @JavascriptInterface
        public void go2Site(int position) {
            VipHelperUtils.getInstance().changeCurrentSite(position);
            Intent intent = new Intent(ShareActivity2.this, ShareActivity2.class);
            ShareActivity2.this.startActivity(intent);
        }

        @JavascriptInterface
        public void go2Share(int position) {
            SHARE_MEDIA platform  = SHARE_MEDIA.QQ;
            switch (position) {
                case 1:
                    platform  = SHARE_MEDIA.WEIXIN;
                    break;
                case 2:
                    platform  = SHARE_MEDIA.WEIXIN_CIRCLE;
                    break;
                case 3:
                    platform  = SHARE_MEDIA.QQ;
                    break;
                case 4:
                    platform  = SHARE_MEDIA.QZONE;
                    break;
            }
            UMWeb  web = new UMWeb("http://39.108.151.95:8000/App/web/share/"+VipHelperUtils.getInstance().getVipUserInfo().getCommendNo());
            web.setTitle("魔视VIP");//标题
            UMImage image = new UMImage(ShareActivity2.this, R.drawable.ic_logo);//资源文件
            web.setThumb(image);  //缩略图
            web.setDescription("免费VIP来啦");//描述
            new ShareAction(ShareActivity2.this)
                    .setPlatform(platform)//传入平台
                    .withMedia(web)
                    .setCallback(listener)//回调监听器
                    .share();
        }

        @JavascriptInterface
        public String getCommendNo() {
            return VipHelperUtils.getInstance().getVipUserInfo().getCommendNo();
        }

        @JavascriptInterface
        public void copyCommendNo() {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", VipHelperUtils.getInstance().getVipUserInfo().getCommendNo());
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            ToastUtil.show(ShareActivity2.this, "邀请码已复制到剪贴板！",Toast.LENGTH_SHORT);
        }

    }

}
