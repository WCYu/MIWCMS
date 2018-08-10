package com.rxjy.iwc2.api.bean;

/**
 * Created by 解亚鑫 on 2018/5/9.
 */

public class IsCaptureInfo {


    /**
     * code : 10000
     * message : success
     * res : {"capture":0,"deviceId":"10001","did":137,"id":137,"imei":"867282050070466"}
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
         * capture : 0
         * deviceId : 10001
         * did : 137
         * id : 137
         * imei : 867282050070466
         */

        private int capture;
        private String deviceId;
        private int did;
        private int id;
        private String imei;

        public int getCapture() {
            return capture;
        }

        public void setCapture(int capture) {
            this.capture = capture;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public int getDid() {
            return did;
        }

        public void setDid(int did) {
            this.did = did;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }
    }
}
