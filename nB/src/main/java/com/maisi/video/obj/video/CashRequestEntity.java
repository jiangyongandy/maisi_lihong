package com.maisi.video.obj.video;

import java.io.Serializable;

/**
 * 功能
 * Created by Jiang on 2017/12/11.
 */

public class CashRequestEntity implements Serializable {

    private String uid;
    private double maisibi;
    private String payeeAccount;
    private double amount;
    private String pyeeRealName;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getMaisibi() {
        return maisibi;
    }

    public void setMaisibi(double maisibi) {
        this.maisibi = maisibi;
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPyeeRealName() {
        return pyeeRealName;
    }

    public void setPyeeRealName(String pyeeRealName) {
        this.pyeeRealName = pyeeRealName;
    }
}
