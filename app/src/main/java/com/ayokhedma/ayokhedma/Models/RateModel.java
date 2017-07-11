package com.ayokhedma.ayokhedma.Models;

/**
 * Created by MK on 12/07/2017.
 */

public class RateModel {
    String objid,userid;
    float rate;

    public RateModel(String objid, String userid, float rate) {
        this.objid = objid;
        this.userid = userid;
        this.rate = rate;
    }

    public String getObjid() {
        return objid;
    }

    public String getUserid() {
        return userid;
    }

    public float getRate() {
        return rate;
    }
}
