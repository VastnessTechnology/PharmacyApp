package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class CheckMobileResendOtpInfo extends ParentRequestVO {

    String mStringGetotpValue;
    String mStringMobileNo;
String mStringUserId;


    public CheckMobileResendOtpInfo(String mStringMobileNo, String mStringGetotpValue ,String mStringUserId) {
        this.mStringGetotpValue = mStringGetotpValue;
        this.mStringMobileNo = mStringMobileNo;
        this.mStringUserId = mStringUserId;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("MobileNo",mStringMobileNo);
            dataJSONTemp.put("OTP", mStringGetotpValue);
            dataJSONTemp.put("UserId", mStringUserId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJSONTemp;
    }
}