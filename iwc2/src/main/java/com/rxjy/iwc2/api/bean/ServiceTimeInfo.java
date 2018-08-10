package com.rxjy.iwc2.api.bean;

/**
 * Created by 解亚鑫 on 2018/5/14.
 */

public class ServiceTimeInfo {

    /**
     * code : 10000
     * message : success
     * res : {"date":1526358869000,"st":"20180515_123429","time":1526358869}
     * total : 0
     */

    private String code;
    private String message;
    private ResBean res;
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

    public ResBean getRes() {
        return res;
    }

    public void setRes(ResBean res) {
        this.res = res;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class ResBean {
        /**
         * date : 1526358869000
         * st : 20180515_123429
         * time : 1526358869
         */

        private long date;
        private String st;
        private int time;

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public String getSt() {
            return st;
        }

        public void setSt(String st) {
            this.st = st;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
