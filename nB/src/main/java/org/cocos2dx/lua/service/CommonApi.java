package org.cocos2dx.lua.service;

import com.maisi.video.obj.video.BannerEntity;
import com.maisi.video.obj.video.ChargeInfoEntity;
import com.maisi.video.obj.video.MsbRateEntity;
import com.maisi.video.obj.video.NotifyEntity;
import com.maisi.video.obj.video.PlayLineEntity;
import com.maisi.video.obj.video.RecommendEntity;
import com.maisi.video.obj.video.UpdateEntity;
import com.maisi.video.obj.video.UserInfoEntity;
import com.maisi.video.obj.video.WechatTipsEntity;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * 功能
 * Created by Jiang on 2017/10/27.
 */

public interface CommonApi {

    //登录会员后台 http://39.108.151.95:8000/MyApp/user/getUser/{id}
    @GET("user/getUser/{id}")
    Observable<UserInfoEntity> requestLogin(@Path("id") String id);

    //获取充值金额 http://39.108.151.95:8000/MyApp/user/getDicByGroup/1
    @GET("user/getDicByGroup/1")
    Observable<ArrayList<ChargeInfoEntity > > getChargeCatalog();

    //分享成功后根据uid刷新积分 http://39.108.151.95:8000/MyApp/user/updatePoints/{uid}
    @GET("user/updatePoints/{uid}")
    Observable<String> updatePoints(@Path("uid") String id);

    //视频线路接口 39.108.151.95:8000/MyApp/user/getDicByGroup/15
    @GET("user/getDicByGroup/15")
    Observable<UserInfoEntity> getVipPlayLine();

    //轮播图接口 39.108.151.95:8000/MyApp/user/getDicByGroup/8
    @GET("user/getDicByGroup/8")
    Observable<ArrayList<BannerEntity>> getBanner();

    //获取推荐码 39.108.151.95:8000/MyApp/user/getCommendNo/{uid}
    @GET("user/getCommendNo/{uid}")
    Observable<RecommendEntity> getRecommendNo(@Path("uid") String id);

    //返回公告 http://39.108.151.95:8000/MyApp/user/getDicByGroup/38
    @GET("user/getDicByGroup/38")
    Observable<ArrayList<NotifyEntity>> getNotify();

    //修改第一次充值引导状态 39.108.151.95:8000/MyApp/user/getCommendNo/{uid}
    @GET("user/updateUser/{uid}")
    Observable<RecommendEntity> updateNewUserState(@Path("uid") String id);

    //查询解析接口 按顺序返回
    /*
    优酷：http://39.108.151.95:8000/MyApp/user/getDicByGroup/15
    爱奇艺：http://39.108.151.95:8000/MyApp/user/getDicByGroup/16
    腾讯：http://39.108.151.95:8000/MyApp/user/getDicByGroup/17
    芒果：http://39.108.151.95:8000/MyApp/user/getDicByGroup/18
    乐视：http://39.108.151.95:8000/MyApp/user/getDicByGroup/19
    其他：http://39.108.151.95:8000/MyApp/user/getDicByGroup/22
    * */
    @GET("user/getDicByGroup/{uid}")
    Observable<ArrayList<PlayLineEntity >> getPlayLine(@Path("uid") int id);

    //--支付宝生成订单接口 39.108.151.95:8000/MyApp/user/createOrder
    @POST("user/createOrder/")
    Observable<String> createOrder(@Body RequestBody body);

    //--同步通知接口 返回“true”表示支付完成 39.108.151.95:8000/MyApp/user/payStatusMsg
    @POST("user/payStatusMsg/")
    Observable<String> payStatusMsg();

    //--APP版本更新接口 http://39.108.151.95:8000/MyApp/user/getValueWithKey/version_update
    @GET("user/getValueWithKey/version_update")
    Observable<UpdateEntity > versionUpdate();

    //--积分抵现实付金额查询接口 http://39.108.151.95:8000/MyApp/user/calcuPay
    @POST("user/calcuPay")
    Observable<String > calcuPay(@Body RequestBody body);

    //--获取微信提现提示 http://39.108.151.95:8000/MyApp/user/getRemarkWithKey/cash
    @GET("user/getRemarkWithKey/cash")
    Observable<WechatTipsEntity > getWechatTips();

    //--获取迈思币规则 http://39.108.151.95:8000/MyApp/user/getRemarkWithKey/maisibi
    @GET("user/getRemarkWithKey/maisibi")
    Observable<WechatTipsEntity > getMaiSiBiTips();

    //--获取迈思币比例 http://39.108.151.95:8000/MyApp/user/getValueWithKey/maisibiRate
    @GET("user/getValueWithKey/maisibiRate")
    Observable<MsbRateEntity > getMaisibiRate();

    //--系统奖励迈思币 http://39.108.151.95:8000/MyApp/user/getMaisibi/lihong_01/400
    @GET("user/getMaisibi/{uid}/{giveAmount}")
    Observable<String > getMaisibi(@Path("uid") String id, @Path("giveAmount") double giveAmount);

    //--转账迈思币 http://39.108.151.95:8000/MyApp/user/giveMSB2Other/{uid}/{giveAmount}/{commendno}
    @GET("user/giveMSB2Other/{uid}/{giveAmount}/{commendno}")
    Observable<String > giveMSB2Other(@Path("uid") String id, @Path("giveAmount") double giveAmount, @Path("commendno") String commendNo);

    //--支付宝提现 http://39.108.151.95:8000/MyApp/alipay/alitransfer
    @POST("alipay/alitransfer")
    Observable<String > alitransfer(@Body RequestBody body);

    //--卡密激活VIP http://39.108.151.95:8000/App/web/useCard/{cardPw}/{uid}
    @GET("web/useCard/{cardPw}/{uid}")
    Observable<String > activeVip(@Path("cardPw") String pw, @Path("uid") String id);

}
