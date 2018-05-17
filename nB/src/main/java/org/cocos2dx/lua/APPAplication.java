package org.cocos2dx.lua;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;
import com.maisi.video.obj.WXAPIConst;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class APPAplication extends Application {

    public static APPAplication instance;
    public boolean qbsdkIsInit = false;
    public QbInitListener qbInitListener;
    public ArrayList<QbInitListener>  qbInitListenerArrayList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        instance = this;
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        final QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean isSuccess) {
                qbsdkIsInit = true;
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + isSuccess);
/*                if(isSuccess) {
                    ToastUtil.show(instance,"VIP内核加载成功", Toast.LENGTH_SHORT);
                }else {
                    ToastUtil.show(instance,"VIP内核加载失败", Toast.LENGTH_SHORT);
                }*/
/*                for(QbInitListener qbInitListener: qbInitListenerArrayList) {
                    if(qbInitListener != null) {
                        qbInitListener.onQbInit();
                    }
                }*/
            }

            @Override
            public void onCoreInitFinished() {
                Log.d("app", " onCoreInitFinished ");
            }
        };
        //x5内核初始化接口
        BoyiRxUtils.makeObservable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                QbSdk.initX5Environment(getApplicationContext(), cb);
                boolean isInit = true;
                return isInit;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1< Boolean>() {
                    @Override
                    public void call( Boolean aBoolean) {
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(
                                APPAplication.instance,
                                throwable.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        //友盟
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(true);
        //上报内核错误信息
//		MobclickAgent.reportError(this, WebView.getCrashExtraMessage(this));
        Log.e("app", "x5内核错误信息------------------" + WebView.getCrashExtraMessage(this));

        Utils.init(this);
        //友盟注册微信等平台
        if(CommonConstant.isDebug) {
            Config.DEBUG = true;
//            WebView.setWebContentsDebuggingEnabled(true);
        }
        PlatformConfig.setWeixin(WXAPIConst.APP_ID, WXAPIConst.AppSecret);
        PlatformConfig.setQQZone("1106902646", "z124XcEUWkWRJid1");
        PlatformConfig.setSinaWeibo("1643151326", "b8bc9ed471127abab76e55548ae2a276", "http://39.108.151.95:8000/");

        if (CommonConstant.isRelease) {

            VipHelperUtils.getInstance().updatePlayLineByServer();
        }
    }

    public interface QbInitListener {
        void onQbInit();
    }

}
