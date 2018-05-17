package org.cocos2dx.lua.model;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.maisi.video.obj.WeiChatUserInfo;
import com.maisi.video.obj.video.ChargeInfoEntity;
import com.maisi.video.obj.video.ChargeRequestEntity;
import com.maisi.video.obj.video.PayResult;
import com.maisi.video.obj.video.UserInfoEntity;
import com.maisi.video.obj.video.WechatChargeEntity;
import com.maisi.video.obj.video.WechatOrderResultEntity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.BoyiRxUtils;
import org.cocos2dx.lua.CommonConstant;
import org.cocos2dx.lua.EventBusTag;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.service.Service;
import org.simple.eventbus.EventBus;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 功能
 * Created by Jiang on 2017/11/30.
 */

public class UserModel {

    private static UserModel instance = null;
    //def -1 charge 0
    private int action = -1;

    private Class intent;
    private boolean needVip;

    public static UserModel getInstance() {
        if(instance == null)
            instance = new UserModel();
        return  instance;
    }

    public void login(final Activity activity) {

        if (VipHelperUtils.getInstance().isWechatLogin())
            return;

        final UMAuthListener umUserInfoListener = new UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Toast.makeText(activity, "成功返回用户信息", Toast.LENGTH_LONG).show();
                final WeiChatUserInfo userInfo = new WeiChatUserInfo();
                userInfo.setHeadimgurl(map.get("iconurl"));
                userInfo.setNickname(map.get("name"));
                userInfo.setUnionid(map.get("uid"));
                VipHelperUtils.getInstance().setUserInfo(userInfo);
                //需要登录自己的后台
                Service.getComnonService().requestLogin(userInfo.getUnionid())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(
                                        APPAplication.instance,
                                        "错误:" + throwable.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .subscribe(new Subscriber<UserInfoEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(
                                        APPAplication.instance,
                                        e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(UserInfoEntity result) {
                                Toast.makeText(
                                        APPAplication.instance,
                                        "登陆成功~~",
                                        Toast.LENGTH_SHORT).show();
                                VipHelperUtils.getInstance().setWechatLogin(true);
                                result.setWeiChatUserInfo(userInfo);
                                VipHelperUtils.getInstance().setVipUserInfo(result);
                                EventBus.getDefault().post(result, EventBusTag.TAG_LOGIN_SUCCESS);
                                if (result.getVipLeft() > 0) {
                                    VipHelperUtils.getInstance().setValidVip(true);
                                } else {
                                    String temp ="";
                                    if (!CommonConstant.isRelease) {
                                        temp = "需要重新激活VIP才能正常使用哦~.~";
                                    }else {
                                        temp = "需要重新激活VIP才能正常观看哦~.~";
                                    }
                                    Toast.makeText(
                                            APPAplication.instance,
                                            temp,
                                            Toast.LENGTH_SHORT).show();
                                    VipHelperUtils.getInstance().setValidVip(false);
                                }
                                if(intent != null) {
                                    if(needVip) {

                                        needVip = false;
                                        if(!VipHelperUtils.getInstance().isValidVip()) {
                                            MaterialDialog dialog = new MaterialDialog.Builder(activity)
                                                    .title("VIP已过期")
                                                    .content("VIP已过期不能愉快的观看啦，需要重新激活...")
                                                    .positiveText("")
                                                    .negativeText("稍等")
                                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(MaterialDialog dialog, DialogAction which) {
//                                                            UserModel.getInstance().launchActivity(activity, ChargeActivity.class);
                                                        }
                                                    })
                                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(MaterialDialog dialog, DialogAction which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                            return;
                                        }

                                    }
                                    Intent intent1 = new Intent(activity, intent);
                                    activity.startActivity(intent1);
                                    intent = null;
                                }
                            }
                        });

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Toast.makeText(
                        APPAplication.instance,
                        "错误:" + throwable.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        };

        UMAuthListener umAuthListener = new UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                UMShareAPI.get(APPAplication.instance).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, umUserInfoListener);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Toast.makeText(
                        APPAplication.instance,
                        "错误:" + throwable.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        };

        UMShareAPI.get(APPAplication.instance).doOauthVerify(activity, SHARE_MEDIA.WEIXIN, umAuthListener);

    }

    public void exitLogin() {
        VipHelperUtils.getInstance().setUserInfo(null);
        VipHelperUtils.getInstance().setWechatLogin(false);
        VipHelperUtils.getInstance().setVipUserInfo(null);
        VipHelperUtils.getInstance().setValidVip(false);
    }

    public void refreshUserInfo() {

        //需要登录自己的后台
        Service.getComnonService().requestLogin(VipHelperUtils.getInstance().getVipUserInfo().getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(
                                APPAplication.instance,
                                "错误:" + throwable.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(new Subscriber<UserInfoEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                APPAplication.instance,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(UserInfoEntity result) {
                        Toast.makeText(
                                APPAplication.instance,
                                "刷新信息~~",
                                Toast.LENGTH_SHORT).show();
                        result.setWeiChatUserInfo(VipHelperUtils.getInstance().getVipUserInfo().getWeiChatUserInfo());
                        VipHelperUtils.getInstance().setVipUserInfo(result);
                        EventBus.getDefault().post(result, EventBusTag.TAG_LOGIN_SUCCESS);
                        if (result.getVipLeft() > 0) {
                            VipHelperUtils.getInstance().setValidVip(true);
                        } else {
                            VipHelperUtils.getInstance().setValidVip(false);
                        }
                    }
                });
    }

    public void refreshUserInfo(UserInfoEntity result) {
        result.setWeiChatUserInfo(VipHelperUtils.getInstance().getVipUserInfo().getWeiChatUserInfo());
        VipHelperUtils.getInstance().setVipUserInfo(result);
        EventBus.getDefault().post(result, EventBusTag.TAG_LOGIN_SUCCESS);
        if (result.getVipLeft() > 0) {
            VipHelperUtils.getInstance().setValidVip(true);
        } else {
            VipHelperUtils.getInstance().setValidVip(false);
        }
    }

    public void getChargeMenu() {
        //获取充值金额
        Service.getComnonService().getChargeCatalog()
                .compose(BoyiRxUtils.<ArrayList<ChargeInfoEntity>>applySchedulers())
                .subscribe(new BoyiRxUtils.MySubscriber<ArrayList<ChargeInfoEntity>>() {

                    @Override
                    public void onNext(ArrayList< ChargeInfoEntity > result) {
/*                        list.clear();
                        list.addAll(result);
                        list.get(0).setSelected(true);
                        listAdapter.notifyDataSetChanged();

                        CalcuPayEntity entity = new CalcuPayEntity();
                        entity.setAmount(Double.parseDouble(list.get(0).getValue1()));
                        entity.setPoints(VipHelperUtils.getInstance().getVipUserInfo().getPointsLeft());
                        Gson gson = new Gson();
                        String toJson = gson.toJson(entity);
                        RequestBody body = RequestBody.create(MediaType.parse("application/json"), toJson);
                        Service.getComnonService().calcuPay(body)
                                .compose(BoyiRxUtils.<String>applySchedulers())
                                .subscribe(new BoyiRxUtils.MySubscriber<String>() {

                                    @Override
                                    public void onNext(String result) {
                                        mIvRealCharge.setText("积分抵现后仅需支付：" + result + "元");
                                        currentPay = Double.parseDouble(result);
                                    }
                                });*/
                    }
                });

    }

    public void requestCharge(final Activity activity, double amount, double points, String commendNo) {

        if(!VipHelperUtils.getInstance().isWechatLogin()) {
            login(activity);
            return;
        }
        ChargeRequestEntity chargeRequestEntity = new ChargeRequestEntity();
        chargeRequestEntity.setAmount(amount);
        chargeRequestEntity.setPoints(points);
        chargeRequestEntity.setCommendno(commendNo);
        chargeRequestEntity.setUid(VipHelperUtils.getInstance().getVipUserInfo().getUid());

        if(VipHelperUtils.getInstance().isWechatLogin()) {
            //直接充值
            Gson gson = new Gson();
            String toJson = gson.toJson(chargeRequestEntity);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), toJson);
            Service.getComnonServiceForString().createOrder(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Toast.makeText(
                                    APPAplication.instance,
                                    "错误:" + throwable.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(
                                    APPAplication.instance,
                                    e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(String  result) {
                            payV2(result, activity);

                        }
                    });
        }
    }

    public void requestWeixinCharge(final Activity activity, double amount, double points, String commendNo) {

        WechatChargeEntity chargeRequestEntity = new WechatChargeEntity();
        chargeRequestEntity.setCommend(commendNo);
        chargeRequestEntity.setMoney(amount);
//        chargeRequestEntity.setUid(VipHelperUtils.getInstance().getVipUserInfo().getUid());
        chargeRequestEntity.setUid(VipHelperUtils.getInstance().getVipUserInfo().getUid());
        //生成订单
        Gson gson = new Gson();
        String toJson = gson.toJson(chargeRequestEntity);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), toJson);
        Service.getComnonServiceForString().payOrder(body)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<WechatOrderResultEntity >>() {
                    @Override
                    public Observable<WechatOrderResultEntity > call(String xml) {
                        RequestBody body = RequestBody.create(MediaType.parse("text/xml"), xml);
                        return Service.getComnonServiceForXml().createOrder(body);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(
                                APPAplication.instance,
                                "错误:" + throwable.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(new Subscriber<WechatOrderResultEntity >() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                APPAplication.instance,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(WechatOrderResultEntity   result) {
                        LogUtils.i(">>>>>>>>>>>"+ result);
                        if (result.getResult_code().equals("SUCCESS")) {
                            payWeixin(result,activity);
                        }else {
                            Toast.makeText(activity, "请求微信支付失败："+result.getReturn_msg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

/*        Service.getComnonService().wePayTest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(
                                APPAplication.instance,
                                "错误:" + throwable.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(new Subscriber<TestEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                APPAplication.instance,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(TestEntity   result) {
                        LogUtils.i(">>>>>>>>>>>"+ result);
                        WechatOrderResultEntity entity = new WechatOrderResultEntity();
                        entity.setAppid(result.appid);
                        entity.setMch_id(result.partnerid);
                        entity.setPrepay_id(result.prepayid);
                        entity.setNonce_str(result.noncestr);
                        entity.setTimeStamp(result.timestamp);
                        entity.setSign(result.sign);
                        payWeixin(entity,activity);
                    }
                });*/
    }

    /**
     * 支付宝支付业务
     *
     */
    public void payV2(final String orderInfo, final Activity activity) {

        BoyiRxUtils.makeObservable(new Callable<Map<String, String>>() {
            @Override
            public Map<String, String> call() throws Exception {

                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                return result;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1< Map<String, String>>() {
                    @Override
                    public void call( Map<String, String> aBoolean) {
                        PayResult payResult = new PayResult((Map<String, String>) aBoolean);
                        /**
                         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show();
                        }
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
    }

    /*
    * 微信支付业务
    * */
    public void payWeixin(WechatOrderResultEntity entity, Activity activity) {
        IWXAPI api = WXAPIFactory.createWXAPI(activity, entity.getAppid());
        PayReq request = new PayReq();

        request.appId = entity.getAppid();

        request.partnerId = entity.getMch_id();

        request.prepayId= entity.getPrepay_id();

        request.packageValue = "Sign=WXPay";

        request.nonceStr= entity.getNonce_str();

        //获取当前时间戳
        long timeStamp = System.currentTimeMillis()/1000;
        Log.d("xxxxx", String.valueOf(timeStamp));

        request.timeStamp= String.valueOf(timeStamp);
//        request.timeStamp= entity.getTimeStamp();


        String key = "46db7877b95c1f98f0bc1ce1b108bcdb";
        String paySign = "";
        Map<String ,String > map=new HashMap<String,String>();
        map.put("appid", request.appId );
        map.put("partnerid",request.partnerId);
        map.put("prepayid", request.prepayId);
        map.put("package","Sign=WXPay");
        map.put("noncestr", request.nonceStr);
        map.put("timestamp", request.timeStamp);
        try {
            paySign=getPayCustomSign(map,key);
            map.put("sign",paySign);
            request.sign= paySign;
            api.registerApp(entity.getAppid());

            api.sendReq(request);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String getPayCustomSign(Map<String, String> bizObj,String key) throws Exception {
        String bizString = FormatBizQueryParaMap(bizObj, false);
        return sign(bizString, key);
    }

    public static String FormatBizQueryParaMap(Map<String, String> paraMap,
                                               boolean urlencode) throws Exception {
        String buff = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(paraMap.entrySet());
            Collections.sort(infoIds,
                    new Comparator<Map.Entry<String, String>>() {
                        public int compare(Map.Entry<String, String> o1,
                                           Map.Entry<String, String> o2) {
                            return (o1.getKey()).toString().compareTo(
                                    o2.getKey());
                        }
                    });
            for (int i = 0; i < infoIds.size(); i++) {
                Map.Entry<String, String> item = infoIds.get(i);
                //System.out.println(item.getKey());
                if (item.getKey() != "") {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlencode) {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    buff += key + "=" + val + "&";
                }
            }
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return buff;
    }

    public static String sign(String content, String key)
            throws Exception{
        String signStr = "";
        signStr = content + "&key=" + key;
        return MD5(signStr).toUpperCase();
    }
    public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public void launchActivity(Activity context, Class intent) {
        if (VipHelperUtils.getInstance().isWechatLogin()) {
            Intent intent1 = new Intent(context, intent);
            context.startActivity(intent1);
        }else {
            this.intent = intent;
            login(context);
        }
    }

    public void launchActivity(final Activity context, Class intent, boolean needVip) {
        this.needVip = needVip;
        if (VipHelperUtils.getInstance().isWechatLogin() ) {
            if(this.needVip) {

                this.needVip = false;
                if(!VipHelperUtils.getInstance().isValidVip()) {

                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                            .title("VIP已过期")
                            .content("VIP已过期不能愉快的观看啦，请重新激活...")
                            .positiveText("")
                            .negativeText("稍等")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
//                                    UserModel.getInstance().launchActivity(context, ChargeActivity.class);
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return;
                }

            }
            Intent intent1 = new Intent(context, intent);
            context.startActivity(intent1);
        }else {
            this.intent = intent;
            login(context);
        }
    }

}
