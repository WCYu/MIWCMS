package com.rxjy.iwc2.nohttp.bean;

import java.io.Serializable;

public class IsLowPowerBean implements Serializable{
    private String apiKey;
    private String apiSign;
    private String timeStamp;
    private String IMEI;
    private String SubItemId;
    private int ElectValue;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSign() {
        return apiSign;
    }

    public void setApiSign(String apiSign) {
        this.apiSign = apiSign;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String iMEI) {
        IMEI = iMEI;
    }

    public String getSubItemId() {
        return SubItemId;
    }

    public void setSubItemId(String subItemId) {
        SubItemId = subItemId;
    }

    public int getElectValue(){
        return ElectValue;
    }

    public void setElectValue(int electValue){
        ElectValue = electValue;
    }

}
