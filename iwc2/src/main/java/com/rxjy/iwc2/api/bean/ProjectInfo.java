package com.rxjy.iwc2.api.bean;

/**
 * Created by 解亚鑫 on 2018/4/17.
 */

public class ProjectInfo {

    /**
     * code : 10000
     * message : success
     * res : {"orderno":"1233","areaid":11,"proname":"1233"}
     * total : 0
     */

    private String code;
    private String message;
    private ResBean res;
    private String total;

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

    public ResBean getRes() {
        return res;
    }

    public void setRes(ResBean res) {
        this.res = res;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public static class ResBean {
        /**
         * orderno : 1233
         * areaid : 11
         * proname : 1233
         */

        private String orderno;
        private int areaid;
        private String proname;

        public String getOrderno() {
            return orderno;
        }

        public void setOrderno(String orderno) {
            this.orderno = orderno;
        }

        public int getAreaid() {
            return areaid;
        }

        public void setAreaid(int areaid) {
            this.areaid = areaid;
        }

        public String getProname() {
            return proname;
        }

        public void setProname(String proname) {
            this.proname = proname;
        }
    }
}
