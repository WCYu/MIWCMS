package com.rxjy.iwc2.api.bean;

/**
 * Created by 解亚鑫 on 2018/5/26.
 */

public class VersionInfo {

    /**
     * code : 10000
     * message : success
     * res : {"versionNo":2,"versionName":"版本2","content":"更新抓拍功能","versionUrl":"192.168.1.106"}
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
         * versionNo : 2
         * versionName : 版本2
         * content : 更新抓拍功能
         * versionUrl : 192.168.1.106
         */

        private int versionNo;
        private String versionName;
        private String content;
        private String versionUrl;

        public int getVersionNo() {
            return versionNo;
        }

        public void setVersionNo(int versionNo) {
            this.versionNo = versionNo;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getVersionUrl() {
            return versionUrl;
        }

        public void setVersionUrl(String versionUrl) {
            this.versionUrl = versionUrl;
        }
    }
}
