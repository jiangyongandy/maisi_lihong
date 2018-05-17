package org.cocos2dx.lua.service;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zuiai.nn.BuildConfig;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.CommonConstant;
import org.cocos2dx.lua.Logger;
import org.cocos2dx.lua.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * 功能
 * Created by Jiang on 2017/10/27.
 */

public class Service {
    private static OkHttpClient okHttpClient = null;
    private static WechatApi api;
    private static CommonApi commonApi;
    private static CommonApi commonApi2;
    private static String host = "http://39.108.151.95:8000/App/";

    //todo 对rx引起的内存泄漏进行处理
    public static WechatApi getWeCHatService() {
        if (api == null) {
            initOkHttp();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.baidu.com/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            api = retrofit.create(WechatApi.class);
        }
        return api;
    }

    public static CommonApi getComnonService() {
        if (commonApi == null) {
            initOkHttp();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Service.host)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(new StringConverterFactory())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            commonApi = retrofit.create(CommonApi.class);
        }
        return commonApi;
    }

    public static CommonApi getComnonServiceForString() {
        if (commonApi2 == null) {
            initOkHttp();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Service.host)
                    .client(okHttpClient)
                    .addConverterFactory(new StringConverterFactory())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            commonApi2 = retrofit.create(CommonApi.class);
        }
        return commonApi2;
    }

    public static WechatApi getComnonServiceForXml() {
        if (api == null) {
            initOkHttp();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Service.host)
                    .client(okHttpClient)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            api = retrofit.create(WechatApi.class);
        }
        return api;
    }

    private static void initOkHttp() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(loggingInterceptor);
            }
            File cacheFile = new File(CommonConstant.PATH_CACHE);
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
            Interceptor cacheInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    if (!NetworkUtils.isConnected()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.show(APPAplication.instance,"网络似乎有问题,请检查网络稍候再试~~", Toast.LENGTH_SHORT);
                            }
                        });
                    }
                    int tryCount = 0;
                    Response response = null;
                    try {
                        response = chain.proceed(request); //there are socket connect timeout exception
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //重试三次
                    while ((response == null || !response.isSuccessful()) && tryCount < 3) {

//                        KL.d(RetrofitHelper.class, "interceptRequest is not successful - :{}", tryCount);
                        Logger.showLog("interceptRequest is not successful","response");
                        tryCount++;

                        // retry the request
                        response = chain.proceed(request);
                    }


                    /*if (NetworkUtils.isConnected()) {
                        int maxAge = 0;
                        // 有网络时, 不缓存, 最大保存时长为0
                        response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .removeHeader("Pragma")
                                .build();
                    } else {
                        // 无网络时，设置超时为4周
                        int maxStale = 60 * 60 * 24 * 28;
                        response.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .removeHeader("Pragma")
                                .build();
                    }*/
                    return response;
                }
            };
            //设置缓存
//            builder.addNetworkInterceptor(cacheInterceptor);
            builder.addInterceptor(cacheInterceptor);
            builder.cache(cache);
            //设置超时
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);
            //错误重连
            builder.retryOnConnectionFailure(true);
            okHttpClient = builder.build();
        }
    }
}
