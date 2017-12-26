package com.maisi.video.obj.video;

import com.maisi.video.obj.WeiChatUserInfo;

import java.io.Serializable;

/**
 * 功能
 * Created by Jiang on 2017/11/29.
 */

public class UserInfoEntity implements Serializable {
    /**
     * uid : 11  微信UID
     * vipLeft : 0 VIP剩余天数
     * pointsLeft : 0   积分剩余
     * commendLeft : 0  迈思币剩余
     * createdTime : 1511934467493 后台注册时间
     * lastupdatedTime : null
     * enable : 1
     * commendNo : null 推荐码（个人信息页）
     * ifFirst : 1  （是否为首充用户）
     * giveAmount: 50.0 （获得的系统赠送迈思币数量）
     * private double commend2cash; 当前迈思币兑换的RMB数量
     */


    private String uid;
    private double vipLeft;
    private double pointsLeft;
    private double commendLeft;
    private double giveAmount;
    private double commend2cash;
    private long createdTime;
    private Object lastupdatedTime;
    private int enable;
    private Object commendNo;
    private int ifFirst;


    private WeiChatUserInfo weiChatUserInfo;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getVipLeft() {
        return vipLeft;
    }

    public void setVipLeft(double vipLeft) {
        this.vipLeft = vipLeft;
    }

    public double getPointsLeft() {
        return pointsLeft;
    }

    public void setPointsLeft(double pointsLeft) {
        this.pointsLeft = pointsLeft;
    }

    public double getCommendLeft() {
        return commendLeft;
    }

    public void setCommendLeft(double commendLeft) {
        this.commendLeft = commendLeft;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public Object getLastupdatedTime() {
        return lastupdatedTime;
    }

    public void setLastupdatedTime(Object lastupdatedTime) {
        this.lastupdatedTime = lastupdatedTime;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public Object getCommendNo() {
        return commendNo;
    }

    public void setCommendNo(Object commendNo) {
        this.commendNo = commendNo;
    }

    public int getIfFirst() {
        return ifFirst;
    }

    public void setIfFirst(int ifFirst) {
        this.ifFirst = ifFirst;
    }

    public WeiChatUserInfo getWeiChatUserInfo() {
        return weiChatUserInfo;
    }

    public void setWeiChatUserInfo(WeiChatUserInfo weiChatUserInfo) {
        this.weiChatUserInfo = weiChatUserInfo;
    }

    public double getGiveAmount() {
        return giveAmount;
    }

    public void setGiveAmount(double giveAmount) {
        this.giveAmount = giveAmount;
    }

    public double getCommend2cash() {
        return commend2cash;
    }

    public void setCommend2cash(double commend2cash) {
        this.commend2cash = commend2cash;
    }
}
