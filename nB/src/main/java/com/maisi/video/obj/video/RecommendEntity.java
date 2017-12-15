package com.maisi.video.obj.video;

import java.io.Serializable;

/**
 * 功能
 * Created by Jiang on 2017/11/29.
 */

public class RecommendEntity implements Serializable {


    /**
     * uid : asdasd
     * vipLeft : 0
     * pointsLeft : 0
     * commendLeft : 0
     * createdTime : 1511938820542
     * lastupdatedTime : null
     * enable : 1
     * commendNo : EOUZ6UO4J3
     * ifFirst : 1
     */

    private String uid;
    private double vipLeft;
    private double pointsLeft;
    private double commendLeft;
    private long createdTime;
    private Object lastupdatedTime;
    private int enable;
    private String commendNo;
    private int ifFirst;

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
}
