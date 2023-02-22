package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class SignUpUserInfo extends ParentRequestVO {

    String mStringDeviceToken;
    String mStringEmail;
    String mStringPhoneNumber;
    String mStringDeviceType;


    public SignUpUserInfo( String mStringPhoneNumber,String mStringEmail,
                          String mStringDeviceToken, String mStringDeviceType) {

        this.mStringPhoneNumber = mStringPhoneNumber;
        this.mStringEmail = mStringEmail;
        this.mStringDeviceToken = mStringDeviceToken;
        this.mStringDeviceType = mStringDeviceType;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {

            dataJSONTemp.put("mobileno", mStringPhoneNumber); // MobileNo
            dataJSONTemp.put("emailid", mStringEmail); // Email
            dataJSONTemp.put("devicetype", mStringDeviceType); // Device Type
            dataJSONTemp.put("devicetoken", mStringDeviceToken); // Device Token
            dataJSONTemp.put("userid", "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJSONTemp;
    }
}