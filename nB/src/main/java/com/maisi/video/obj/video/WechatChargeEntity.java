package com.maisi.video.obj.video;

import java.io.Serializable;

/**
 * 功能
 * Created by Jiang on 2017/12/11.
 */

public class WechatChargeEntity implements Serializable {

    /**
     * uid : 1111
     * points : 12
     * commendno : sdafgag
     * amount : 15
     */

    private String uid;
    private String commend;
    private double money;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCommend() {
        return commend;
    }

    public void setCommend(String commend) {
        this.commend = commend;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
