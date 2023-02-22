package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class LoginUserInfo extends ParentRequestVO {


    String mStringEmail = "";
    String mStringPassword= "";
    String DeviceToken="";
    String deviceType= "";


    public LoginUserInfo(String mStringEmail, String mStringPassword ,String DeviceToken ,String deviceType) {
        this.mStringEmail = mStringEmail;
        this.mStringPassword = mStringPassword;
        this.DeviceToken= DeviceToken;
        this.deviceType=deviceType;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("EmailId",mStringEmail);
            dataJSONTemp.put("Password",mStringPassword);
            dataJSONTemp.put("DeviceToken",DeviceToken);
            dataJSONTemp.put("DeviceType",deviceType);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
