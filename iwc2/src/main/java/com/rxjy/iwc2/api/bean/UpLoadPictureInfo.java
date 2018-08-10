package com.rxjy.iwc2.api.bean;

/**
 * Created by 解亚鑫 on 2018/5/8.
 */

public class UpLoadPictureInfo {

    /**
     * code : 10000
     * message : success
     * res : 25-25-68482-867282050070248-42c6a159-44d6-4515-913c-41c84f195f11.jpg
     * total : 0
     */

    private String code;
    private String message;
    private String res;
    private int total;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
