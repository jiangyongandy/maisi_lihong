package com.maisi.video.obj.video;

import java.io.Serializable;

/**
 * 功能
 * Created by Jiang on 2017/11/29.
 */

public class ChargeInfoEntity extends BaseEntity implements Serializable {

    /**
     * id : 2
     * groupId : 1
     * key : amount15
     * value1 : 15
     * value2 : 18
     * description : 充值金额
     * enable : 1
     * created_time : null
     */

    private int id;
    private int groupId;
    private String key;
    private String value1;
    private String value2;
    private String description;
    private int enable;
    private Object created_time;

    private boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public Object getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Object created_time) {
        this.created_time = created_time;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
