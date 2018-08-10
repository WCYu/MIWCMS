package com.rxjy.iwc2.nohttp.bean;

/**
 * Created by 解亚鑫 on 2018/4/18.
 */

public class DeviceStateInfo {


    /**
     * code : 10000
     * message : success
     * res : {"addCreater":"","addTime":1527727560889,"batteryPercentage":10,"did":137,"dingwei":0,"endTime":20,"equipmentInteractionError":0,"history":1,"id":127,"imei":"867282050070466","imgSave_Time":10,"lowPowerFailureCount":10,"lowPowerPoint":1800,"mobileInteraction":0,"motionSensitivity":10,"orderno":"25-68509","paishe":0,"saveImageSize":1000,"saveResolvingPower":1800,"sendImageSize":1000,"sendInterval":40,"sendIntervalToImage":1800,"shotInterval":30,"startTime":8,"updateCreater":"","updateTime":1527727560889,"uploadPicFailureCount":1,"versionNnumber":"1","wendu":0,"wifiAccount":"","wifiPassword":"","yuying":0}
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
         * addCreater :
         * addTime : 1527727560889
         * batteryPercentage : 10
         * did : 137
         * dingwei : 0
         * endTime : 20
         * equipmentInteractionError : 0
         * history : 1
         * id : 127
         * imei : 867282050070466
         * imgSave_Time : 10
         * lowPowerFailureCount : 10
         * lowPowerPoint : 1800
         * mobileInteraction : 0
         * motionSensitivity : 10
         * orderno : 25-68509
         * paishe : 0
         * saveImageSize : 1000
         * saveResolvingPower : 1800
         * sendImageSize : 1000
         * sendInterval : 40
         * sendIntervalToImage : 1800
         * shotInterval : 30
         * startTime : 8
         * updateCreater :
         * updateTime : 1527727560889
         * uploadPicFailureCount : 1
         * versionNnumber : 1
         * wendu : 0
         * wifiAccount :
         * wifiPassword :
         * yuying : 0
         */

        private String addCreater;
        private long addTime;
        private int batteryPercentage;
        private int did;
        private int dingwei;
        private int endTime;
        private int equipmentInteractionError;
        private int history;
        private int id;
        private String imei;
        private int imgSave_Time;
        private int lowPowerFailureCount;
        private int lowPowerPoint;
        private int mobileInteraction;
        private int motionSensitivity;
        private String orderno;
        private int paishe;
        private int saveImageSize;
        private int saveResolvingPower;
        private int sendImageSize;
        private int sendInterval;
        private int sendIntervalToImage;
        private int shotInterval;
        private int startTime;
        private String updateCreater;
        private long updateTime;
        private int uploadPicFailureCount;
        private String versionNnumber;
        private int wendu;
        private String wifiAccount;
        private String wifiPassword;
        private int yuying;

        public String getAddCreater() {
            return addCreater;
        }

        public void setAddCreater(String addCreater) {
            this.addCreater = addCreater;
        }

        public long getAddTime() {
            return addTime;
        }

        public void setAddTime(long addTime) {
            this.addTime = addTime;
        }

        public int getBatteryPercentage() {
            return batteryPercentage;
        }

        public void setBatteryPercentage(int batteryPercentage) {
            this.batteryPercentage = batteryPercentage;
        }

        public int getDid() {
            return did;
        }

        public void setDid(int did) {
            this.did = did;
        }

        public int getDingwei() {
            return dingwei;
        }

        public void setDingwei(int dingwei) {
            this.dingwei = dingwei;
        }

        public int getEndTime() {
            return endTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public int getEquipmentInteractionError() {
            return equipmentInteractionError;
        }

        public void setEquipmentInteractionError(int equipmentInteractionError) {
            this.equipmentInteractionError = equipmentInteractionError;
        }

        public int getHistory() {
            return history;
        }

        public void setHistory(int history) {
            this.history = history;
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

        public int getImgSave_Time() {
            return imgSave_Time;
        }

        public void setImgSave_Time(int imgSave_Time) {
            this.imgSave_Time = imgSave_Time;
        }

        public int getLowPowerFailureCount() {
            return lowPowerFailureCount;
        }

        public void setLowPowerFailureCount(int lowPowerFailureCount) {
            this.lowPowerFailureCount = lowPowerFailureCount;
        }

        public int getLowPowerPoint() {
            return lowPowerPoint;
        }

        public void setLowPowerPoint(int lowPowerPoint) {
            this.lowPowerPoint = lowPowerPoint;
        }

        public int getMobileInteraction() {
            return mobileInteraction;
        }

        public void setMobileInteraction(int mobileInteraction) {
            this.mobileInteraction = mobileInteraction;
        }

        public int getMotionSensitivity() {
            return motionSensitivity;
        }

        public void setMotionSensitivity(int motionSensitivity) {
            this.motionSensitivity = motionSensitivity;
        }

        public String getOrderno() {
            return orderno;
        }

        public void setOrderno(String orderno) {
            this.orderno = orderno;
        }

        public int getPaishe() {
            return paishe;
        }

        public void setPaishe(int paishe) {
            this.paishe = paishe;
        }

        public int getSaveImageSize() {
            return saveImageSize;
        }

        public void setSaveImageSize(int saveImageSize) {
            this.saveImageSize = saveImageSize;
        }

        public int getSaveResolvingPower() {
            return saveResolvingPower;
        }

        public void setSaveResolvingPower(int saveResolvingPower) {
            this.saveResolvingPower = saveResolvingPower;
        }

        public int getSendImageSize() {
            return sendImageSize;
        }

        public void setSendImageSize(int sendImageSize) {
            this.sendImageSize = sendImageSize;
        }

        public int getSendInterval() {
            return sendInterval;
        }

        public void setSendInterval(int sendInterval) {
            this.sendInterval = sendInterval;
        }

        public int getSendIntervalToImage() {
            return sendIntervalToImage;
        }

        public void setSendIntervalToImage(int sendIntervalToImage) {
            this.sendIntervalToImage = sendIntervalToImage;
        }

        public int getShotInterval() {
            return shotInterval;
        }

        public void setShotInterval(int shotInterval) {
            this.shotInterval = shotInterval;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public String getUpdateCreater() {
            return updateCreater;
        }

        public void setUpdateCreater(String updateCreater) {
            this.updateCreater = updateCreater;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getUploadPicFailureCount() {
            return uploadPicFailureCount;
        }

        public void setUploadPicFailureCount(int uploadPicFailureCount) {
            this.uploadPicFailureCount = uploadPicFailureCount;
        }

        public String getVersionNnumber() {
            return versionNnumber;
        }

        public void setVersionNnumber(String versionNnumber) {
            this.versionNnumber = versionNnumber;
        }

        public int getWendu() {
            return wendu;
        }

        public void setWendu(int wendu) {
            this.wendu = wendu;
        }

        public String getWifiAccount() {
            return wifiAccount;
        }

        public void setWifiAccount(String wifiAccount) {
            this.wifiAccount = wifiAccount;
        }

        public String getWifiPassword() {
            return wifiPassword;
        }

        public void setWifiPassword(String wifiPassword) {
            this.wifiPassword = wifiPassword;
        }

        public int getYuying() {
            return yuying;
        }

        public void setYuying(int yuying) {
            this.yuying = yuying;
        }
    }
}
