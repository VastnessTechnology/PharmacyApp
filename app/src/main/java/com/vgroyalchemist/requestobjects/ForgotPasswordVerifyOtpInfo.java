package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class ForgotPasswordVerifyOtpInfo extends ParentRequestVO {

    String mStringGetotpValue;
    String mStringGetEmailId;



    public ForgotPasswordVerifyOtpInfo(String mStringGetEmailId, String mStringGetotpValue) {
        this.mStringGetotpValue = mStringGetotpValue;
        this.mStringGetEmailId = mStringGetEmailId;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("EmailId",mStringGetEmailId);
            dataJSONTemp.put("OTP", mStringGetotpValue); // Email id

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJSONTemp;
    }
}