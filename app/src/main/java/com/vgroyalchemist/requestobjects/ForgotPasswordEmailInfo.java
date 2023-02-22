package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class ForgotPasswordEmailInfo extends ParentRequestVO {

    String mStringGetEmailId;



    public ForgotPasswordEmailInfo(String mStringGetEmailId) {
        this.mStringGetEmailId = mStringGetEmailId;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("EmailId", mStringGetEmailId); // Email id

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJSONTemp;
    }
}