package com.moshi.video.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.maisi.video.obj.HttpUtil;
import com.maisi.video.obj.MD5;
import com.maisi.video.obj.WXAPIConst;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


//import org.cocos2dx.lua.AppActivity;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "WXPayEntryActivity";
    public static String lastRechargeNoncestr = "";
    public static String lastRechargeOrderID = "";
    public static String lastRechargeSign = "";
	private IWXAPI api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		IWXAPI api = WXAPIFactory.createWXAPI(this, WXAPIConst.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp)
	{
		Log.e(TAG, "onResp: "+resp.errCode+"__"+resp.errStr );
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
		{
			if (resp.errCode == 0)
			{
				//展示成功页面
                Log.e(TAG, "onResp: 充值返回" );
				Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
//				AppActivity.CallFunction("0");
            }
			else if(resp.errCode == -1)
			{
				//可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
                Log.e(TAG, "onResp: 充值错误" );
				Toast.makeText(this, "充值错误", Toast.LENGTH_SHORT).show();
			}
			else if (resp.errCode == -2)
			{
				//无需处理。发生场景：用户不支付了，点击取消，返回APP。
                Log.e(TAG, "onResp: 充值取消" );
				Toast.makeText(this, "充值取消", Toast.LENGTH_SHORT).show();
			}
//			WXPayEntryActivity.CheckWXOrder();
			finish();
		}
	}

	public static void CheckWXOrder()
    {
		String md5 = MD5.getPwd("appid="+WXAPIConst.APP_ID+
				"&mch_id="+WXAPIConst.AppPay_ID+
				"&nonce_str="+lastRechargeNoncestr+
				"&out_trade_no="+lastRechargeOrderID+
				"&key="+lastRechargeSign).toUpperCase();
		String xml = "<xml>" +
				"<appid><![CDATA["+WXAPIConst.APP_ID+"]]></appid>" +
				"<mch_id><![CDATA["+WXAPIConst.AppPay_ID+"]]></mch_id>"+
				"<nonce_str><![CDATA["+lastRechargeNoncestr+"]]></nonce_str>"+
				"<out_trade_no><![CDATA["+lastRechargeOrderID+"]]></out_trade_no>"+
//                    "<transaction_id><![CDATA["+lastRechargeOrderID+"]]></transaction_id>"+
				"<sign><![CDATA["+md5+"]]></sign>"+
				"</xml>";
		Log.e(TAG, "CheckWXOrder xmlString: "+xml );
		String urlConnection = HttpUtil.urlConnection("https://api.mch.weixin.qq.com/pay/orderquery", xml, null, null);
		Log.e(TAG, "CheckWXOrder: "+urlConnection );
		int result_code = urlConnection.indexOf("result_code");
		if (result_code != -1) {
			int success = urlConnection.indexOf("SUCCESS", result_code);
			if (success!=-1)
			{
				Log.e(TAG, "CheckWXOrder: 支付成功 " );
//				AppActivity.CallFunction("0");
			}
			else
			{
//				AppActivity.CallFunctionByString("platformMethodJavaCall","支付失败");
			}
		}
		else
		{
//			AppActivity.CallFunctionByString("platformMethodJavaCall","支付失败");
		}
	}
}