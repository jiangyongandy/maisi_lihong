package com.maisi.video.obj.video;

import com.tencent.mm.opensdk.modelpay.PayReq;

/**
 * Created by jiangyong on 2018/4/22.
 */

public class TestEntity extends BaseEntity{

    /*
    * {
appid: "wxb4ba3c02aa476ea1",
partnerid: "1900006771",
package: "Sign=WXPay",
noncestr: "70c5b36bc892ad60ff9a36c1bf026341",
timestamp: 1526492413,
prepayid: "wx17014013786983694f183f560376712157",
sign: "15354C78FE6B4E5EE69B10C658798AC1"
}
    * */

    public String appid;
    public String partnerid;
    public String prepayid;
    public String noncestr;
    public String timestamp;
    public String packageValue;
    public String sign;
    public String extData;
    public PayReq.Options options;
    public String signType;

}
