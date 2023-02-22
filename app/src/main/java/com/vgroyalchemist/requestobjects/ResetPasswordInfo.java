package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class ResetPasswordInfo extends ParentRequestVO {

    String mStringGetNewPassword;
    String mStringGetEmailId;



    public ResetPasswordInfo(String mStringGetEmailId, String mStringGetNewPassword) {
        this.mStringGetNewPassword = mStringGetNewPassword;
        this.mStringGetEmailId = mStringGetEmailId;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("EmailId",mStringGetEmailId);
            dataJSONTemp.put("Password",mStringGetNewPassword);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJSONTemp;
    }
}