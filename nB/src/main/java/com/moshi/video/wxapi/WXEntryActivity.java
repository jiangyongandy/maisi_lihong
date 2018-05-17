package com.moshi.video.wxapi;

//import org.cocos2dx.lua.AppActivity;

import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity {
    /*private String TAG = "WXEntryActivity";


    @Override
    public void onReq(BaseReq req) {
        Log.v("WeiChatLogin", "onReq++++++++++++");
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.v(TAG, "onResp-------------" + resp.getType());
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
                    String code = ((SendAuth.Resp) resp).code; // 这里的code就是接入指南里要拿到的code
                    Log.v("tiantianniu", "this is WXLogin callBack .... " + code);
//	                GetWeChatToken(code);
//	      	        AppActivity.CallFunction(code);
                    //这里写获取到code之后的事件
//                    Toast.makeText(WXEntryActivity.this, "微信登陆成功,可以愉快观看了", Toast.LENGTH_LONG).show();
                    GetWeChatToken(code);

//                    Intent intent = new Intent(this, HomeActivity.class);
//                    startActivity(intent);
                } else if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
                    finish();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
//	            AppActivity.CallFunction("-2");
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//	            AppActivity.CallFunction("-4");
                finish();
                break;
            default:
                Log.v(TAG, "login--unknown---");
                finish();
                break;
        }

    }

    private void GetWeChatToken(String code) {

        Service.getWeCHatService().getAccessToken(UrlConnect.wechatGetAccessToken, WXAPIConst.APP_ID, WXAPIConst.AppSecret, code, "authorization_code")
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<AccessTokenResult, Observable<WeiChatUserInfo>>() {
                    @Override
                    public Observable<WeiChatUserInfo> call(AccessTokenResult accessTokenResult) {
                        return Service.getWeCHatService().getUserInfo(UrlConnect.wechatGetUserInfo, accessTokenResult.getAccess_token(), accessTokenResult.getOpenid());
                    }
                })
                .flatMap(new Func1<WeiChatUserInfo, Observable<String>>() {
                    @Override
                    public Observable<String> call(WeiChatUserInfo userInfo) {
                        VipHelperUtils.getInstance().setUserInfo(userInfo);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", "xhxx_login");
                        map.put("c1", "10001");
                        map.put("c2", userInfo.getUnionid());
                        map.put("c3", "xhxxpwd");
                        map.put("c4", "1.0");
                        map.put("c5", userInfo.getOpenid());
                        map.put("c6", "1");
                        map.put("c7", "1");
                        return Service.getComnonService().requestLogin(UrlConnect.login, map);
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
                    public void onNext(String result) {
                        VipHelperUtils.getInstance().setLoginResult(result);
                        Toast.makeText(
                                APPAplication.instance,
                                "登陆成功~~",
                                Toast.LENGTH_SHORT).show();
                        VipHelperUtils.getInstance().setWechatLogin(true);
                        if(!result.equals("Error:用户已到期。")) {
                            //没有到期才能观看
                            Intent intent = new Intent(WXEntryActivity .this, BrowserActivity.class);
                            WXEntryActivity  .this.startActivity(intent);
                            VipHelperUtils.getInstance().setValidVip(true);
                        }else {
                            Toast.makeText(
                                    APPAplication.instance,
                                    result,
                                    Toast.LENGTH_SHORT).show();
                            VipHelperUtils.getInstance().setValidVip(false);
                        }

                        EventBus.getDefault().post(VipHelperUtils.getInstance().getUserInfo(), EventBusTag.TAG_LOGIN_SUCCESS);
                        finish();//必须要有，用于点击返回游戏的时候不会留在微信
                    }
                });

    }

    public static void WeChatReflushTokenLogin(String token) {
        String url_constant1 = HttpUtil.urlConnection("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" +
                WXAPIConst.APP_ID + "&grant_type=refresh_token&refresh_token=" + token, "", null, null);
        AccessTokenResult result = JSON.parseObject(url_constant1, AccessTokenResult.class);

        if (result.getErrcode() != 0) {
//	    	Toast.makeText(AppActivity.self, result.getErrcode()+"__"+result.getErrmsg(), Toast.LENGTH_LONG).show();
//	    	AppActivity.CallFunction("-2");
            return;
        }

//		AppActivity.self.SaveAccessToken(result.getRefresh_token());

        String url_constant2 = HttpUtil.urlConnection("https://api.weixin.qq.com/sns/userinfo?access_token="
                + result.getAccess_token() + "&openid=" + result.getOpenid(), "", null, null);
        Object ob = JSON.parse(url_constant2);

//        AppActivity.CallFunction(ob.toString());
    }*/

}
