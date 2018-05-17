package org.cocos2dx.lua.service;

import com.maisi.video.obj.video.BannerEntity;
import com.maisi.video.obj.video.ChargeInfoEntity;
import com.maisi.video.obj.video.MsbRateEntity;
import com.maisi.video.obj.video.NotifyEntity;
import com.maisi.video.obj.video.PlayLineEntity;
import com.maisi.video.obj.video.QuestionEntity;
import com.maisi.video.obj.video.RecommendEntity;
import com.maisi.video.obj.video.SystemMessageEntity;
import com.maisi.video.obj.video.TestEntity;
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

    //绑定支付宝接口/修改支付宝信息接口/App/business/addOrUpZfb/{uid}/{zfbno}/{zfbname}
    @GET("business/addOrUpZfb/{uid}/{zfbno}/{zfbname}")
    Observable<UserInfoEntity > bindZhiFu(@Path("uid") String no, @Path("zfbno") String pw,@Path("zfbname") String id);

    //--卡密激活VIP http://39.108.151.95:8000/App/web/useCard/{cardPw}/{uid}
    @GET("web/useCard/{cardNo}/{cardPw}/{uid}")
    Observable<String > activeVip(@Path("cardNo") String no, @Path("cardPw") String pw,@Path("uid") String id);

    //--百科 http://39.108.151.95:8000/App/user/getDicByGroup/114
    @GET("user/getDicByGroup/114")
    Observable<ArrayList<QuestionEntity>>getQuestion();

    //三级代理
    //19、收益明细 /App/business/queryIncomeDetail/{uid}
    @GET("business/queryIncomeDetail/{uid}")
    Observable<ArrayList<String>>queryIncomeDetail(@Path("uid") String no);

    //22、提现记录App/business/queryCommend2CashRecord/{uid}
    @GET("business/queryCommend2CashRecord/{uid}")
    Observable<ArrayList<String[]>>queryCommend2CashRecord(@Path("uid") String no);

    //21、查询我的团队App/business/queryTeamWithUid/{uid}
    @GET("business/queryTeamWithUid/{uid}")
    Observable<ArrayList<String[]>>queryTeamWithUid(@Path("uid") String no);

    //18、按月查询个人直推人数/App/business/queryCommenNumWithUid/{uid}
    @GET("business/queryCommenNumWithUid/{uid}")
    Observable<ArrayList<String>>queryCommenNumWithUid(@Path("uid") String no);

    //团队搜索接口/App/business/queryWithName/{zfbname}/{uid}
    @GET("business/queryWithName/{zfbname}/{uid}")
    Observable<ArrayList<String[]>>queryWithName(@Path("zfbname") String zfbname, @Path("uid") String uid);

    //17、团队排名/App/business/teamList/0,1,2
    @GET("business/teamList/0,1,2")
    Observable<ArrayList<String[]>>queryTeamList();

    //16、总收益排名App/business/incomeList
    @GET("business/incomeList")
    Observable<ArrayList<String[]>>queryIncomeList();

    //消息
    //系统消息接口 39.108.151.95:8000/App/user/getDicByGroup/136
    @GET("user/getDicByGroup/136")
    Observable<ArrayList<SystemMessageEntity>>querySystemMessage();

    //消息
    //微信生成订单 http://39.108.151.95:8000/App//wxpay/payOrder
    @POST("wxpay/payOrder")
    Observable<String>payOrder(@Body RequestBody body);

    //消息
    //微信生成订单 http://wxpay.wxutil.com/pub_v2/app/app_pay.php
    @GET("http://wxpay.wxutil.com/pub_v2/app/app_pay.php")
    Observable<TestEntity > wePayTest();


}
