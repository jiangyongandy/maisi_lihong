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
    private String commendNo;
    private int ifFirst;


    private WeiChatUserInfo weiChatUserInfo;
    /**
     * vipLeft : 0
     * pointsLeft : 0
     * commendLeft : 0
     * lastupdatedTime : null
     * commendNo : 151189
     * give_status : 1
     * zfbname : null
     * zfbno : null
     * commend_level : 0
     * incomeAll : null
     * zhituiIncome : null
     * fenxiaoIncome : null
     * monthIncome : null
     * giveAmount : 0
     * commend2cash : 0
     */

    private int give_status;
    private String zfbname;
    private String zfbno;
    private int commend_level;
    private double incomeAll;
    private double zhituiIncome;
    private double fenxiaoIncome;
    private double monthIncome;

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

    public String getCommendNo() {
        return commendNo;
    }

    public void setCommendNo(String commendNo) {
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

    public int getGive_status() {
        return give_status;
    }

    public void setGive_status(int give_status) {
        this.give_status = give_status;
    }

    public String getZfbname() {
        return zfbname;
    }

    public void setZfbname(String zfbname) {
        this.zfbname = zfbname;
    }

    public String getZfbno() {
        return zfbno;
    }

    public void setZfbno(String zfbno) {
        this.zfbno = zfbno;
    }

    public int getCommend_level() {
        return commend_level;
    }

    public void setCommend_level(int commend_level) {
        this.commend_level = commend_level;
    }

    public Object getIncomeAll() {
        return incomeAll;
    }

    public void setIncomeAll(double incomeAll) {
        this.incomeAll = incomeAll;
    }

    public Object getZhituiIncome() {
        return zhituiIncome;
    }

    public void setZhituiIncome(double zhituiIncome) {
        this.zhituiIncome = zhituiIncome;
    }

    public Object getFenxiaoIncome() {
        return fenxiaoIncome;
    }

    public void setFenxiaoIncome(double fenxiaoIncome) {
        this.fenxiaoIncome = fenxiaoIncome;
    }

    public Object getMonthIncome() {
        return monthIncome;
    }

    public void setMonthIncome(double monthIncome) {
        this.monthIncome = monthIncome;
    }
}
