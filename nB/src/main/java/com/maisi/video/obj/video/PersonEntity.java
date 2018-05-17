package com.maisi.video.obj.video;

import java.io.Serializable;

/**
 * Created by jiangyong on 2018/4/22.
 */

public class PersonEntity extends BaseEntity implements Serializable {
    private String uid;
    private String name;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
