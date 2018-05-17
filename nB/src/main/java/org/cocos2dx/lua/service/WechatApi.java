package org.cocos2dx.lua.service;

import com.maisi.video.obj.AccessTokenResult;
import com.maisi.video.obj.WeiChatUserInfo;
import com.maisi.video.obj.video.WechatOrderResultEntity;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 功能
 * Created by Jiang on 2017/10/27.
 */

public interface WechatApi {

    //wechat通过code获取access_token的接口。
    @GET
    Observable<AccessTokenResult> getAccessToken(@Url String url, @Query("appid") String appid,  @Query("secret") String secret, @Query("code") String code, @Query("grant_type") String grantType);

    //wechat获取用户个人信息（UnionID机制）
    @GET
    Observable<WeiChatUserInfo> getUserInfo(@Url String url, @Query("access_token") String token,  @Query("openid") String openid);

    //--微信生成订单接口 https://api.mch.weixin.qq.com/pay/unifiedorder
    @POST("https://api.mch.weixin.qq.com/pay/unifiedorder")
    Observable<WechatOrderResultEntity> createOrder(@Body RequestBody body);


}
