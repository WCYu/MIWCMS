package com.rxjy.iwc2.api.bean;

import java.io.Serializable;

/**
 * Created by qindd on 2016/7/25.
 */
public class SendErrorMsgBean implements Serializable {
    private String apiKey;
    private String apiSign;
    private String timeStamp;
    private String IMEI;
    private String ExceptionContent;
    private String SubItemId;

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

    public String getExceptionContent() {
        return ExceptionContent;
    }

    public void setExceptionContent(String ExceptionContent)
    {
        this.ExceptionContent = ExceptionContent;
    }

    public String getSubItemId() {
        return SubItemId;
    }

    public void setSubItemId(String subItemId)
    {
        SubItemId = subItemId;
    }
}
