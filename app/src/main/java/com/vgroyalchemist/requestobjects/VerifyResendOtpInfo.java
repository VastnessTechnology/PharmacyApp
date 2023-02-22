package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class VerifyResendOtpInfo extends ParentRequestVO {

    String mStringGetotpValue;
    String mStringGetEmailId;



    public VerifyResendOtpInfo(String mStringGetEmailId, String mStringGetotpValue) {
        this.mStringGetotpValue = mStringGetotpValue;
        this.mStringGetEmailId = mStringGetEmailId;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("EmailId",mStringGetEmailId);
            dataJSONTemp.put("OTP", mStringGetotpValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJSONTemp;
    }
}