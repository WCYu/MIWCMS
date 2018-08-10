package com.rxjy.iwc2.api.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DeviceOnlineBean implements Serializable{

	private String apiKey;
	private String apiSign;
	private String timeStamp;
	private String IMEI;
	private String ConfVerCode;

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

	public String getConfVerCode() {
		return ConfVerCode;
	}

	public void setConfVerCode(String confVerCode) {
		ConfVerCode = confVerCode;
	}

}
