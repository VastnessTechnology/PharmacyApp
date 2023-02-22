package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class ChnagePasswordInfo extends ParentRequestVO {


    String mStringUserId = "";
    String mStringCurrentPassword="";
    String mStringNewPassword="";


    public ChnagePasswordInfo(String mStringUserId, String mStringCurrentPassword , String mStringNewPassword) {
        this.mStringUserId = mStringUserId;
        this.mStringCurrentPassword=mStringCurrentPassword;
        this.mStringNewPassword=mStringNewPassword;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("CurrentPassword",mStringCurrentPassword);
            dataJSONTemp.put("NewPassword",mStringNewPassword);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
