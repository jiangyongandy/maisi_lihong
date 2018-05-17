package org.cocos2dx.lua.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
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

import org.cocos2dx.lua.ui.BrowserActivity;


public class FindWebFragment extends BaseFragment {
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private X5WebView mWebView;
    private ViewGroup mViewParent;
    private EditText mUrl;

    private final String mAboutUrl = "https://3g.163.com/touch/video/subchannel/all?ver=c&clickfrom=index2018_header&dataversion=A&uversion=A&version=v_standard";
    //    private final String mHomeUrl = "http://dwjzywapp.com/appindex.php";
    private static final String TAG = "SdkDemo";
    private static final int MAX_LENGTH = 14;
    private boolean mNeedTestPage = false;

    private final int disable = 120;
    private final int enable = 255;

    private ValueCallback<Uri> uploadFile;
    private boolean isFirst = false;

    private void init() {

        mWebView = new X5WebView(mActivity, null);

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
                    if(isFirst) {
                        LogUtils.i("第一次加载------------");
                        view.loadUrl(url);
                        isFirst = false;
                        return true;
                    }
                    Intent intent = new Intent(getActivity(), BrowserActivity.class);
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    getActivity().startActivity(intent);
                    return true;
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
                handleAd(webView);
            }

            @Override
            public void onPageFinished(final WebView webView, String s) {
                Log.i("onPageFinished", s);
                super.onPageFinished(webView, s);
                handleAd(webView);
//				Toast.makeText(getApplicationContext(), "页面加载完成", Toast.LENGTH_SHORT).show();
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
                FrameLayout normalView = (FrameLayout) mActivity.findViewById(com.example.test_webview_demo.R.id.web_filechooser);
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
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {
                TbsLog.d(TAG, "url: " + arg0);
                new AlertDialog.Builder(mActivity)
                        .setTitle("允许下载吗？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Toast.makeText(
                                                mActivity,
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
                                                mActivity,
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
                                                mActivity,
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
        webSetting.setAppCachePath(mActivity.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(mActivity.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(mActivity.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();
        mWebView.loadUrl(mAboutUrl);
        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(mActivity);
        CookieSyncManager.getInstance().sync();

    }

    public void refreshPage() {
        mWebView.loadUrl(mAboutUrl);
    }


    boolean[] m_selected = new boolean[]{true, true, true, true, false,
            false, true};

    @Override
    protected void initData() {
        mViewParent = (ViewGroup) mRootView.findViewById(com.example.test_webview_demo.R.id.webView1);

        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_person;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden()) {
            isFirst = false;
        }
    }

    @Override
    public void onDestroyView() {
        if (mTestHandler != null)
            mTestHandler.removeCallbacksAndMessages(null);
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroyView();
    }

    private void handleAd(WebView webView) {
        String temp = "javascript:" +
                "var elementName = \"=====\";\n" +
                "function countTotalElement(node) {\n" +
                "    var total = 0;\n" +
                "    if (node.nodeType == 1) {\n" +
                "        total++;\n" +
                "        elementName = elementName + node.tagName + \"\";\n" +
                "        if (node.className == 'channel-nav') {\n" +
                "            console.log(\"找到对应的node------------------\");\n" +
                "            node.style.display=\"none\";"+
                "            return;\n" +
                "        }\n" +
                "    }\n" +
                "    var childrens = node.childNodes;\n" +
                "    for (var i = 0; i < childrens.length; i++) {\n" +
                "        total += countTotalElement(childrens[i]);\n" +
                "    }\n" +
                "    return total;\n" +
                "}\n" +
                "countTotalElement(document);";;
        webView.loadUrl(temp);
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
        public void goToPlayPage(String url) {
        }

        @JavascriptInterface
        public void go2Site(int position, String apiString) {
        }

        @JavascriptInterface
        public void paySuccess(String result) {
        }

    }

}
