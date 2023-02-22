package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class SocialUserInfo extends ParentRequestVO {


    String mStringEmailId = "";
    String mStringFirstName= "";
    String mStringLastName= "";
    String mStringGoogleId= "";
    String mStringFacebookId= "";
    String mStringScoialType= "";
    String mStringDeviceToken= "";
    String mStringDeviceType= "";



    public SocialUserInfo(String mStringFirstName, String mStringLastName ,String mStringEmailId ,String mStringGoogleId, String mStringFacebookId, String mStringScoialType,
                          String mStringDeviceToken ,String mStringDeviceType) {
        this.mStringFirstName = mStringFirstName;
        this.mStringLastName = mStringLastName;
        this.mStringEmailId = mStringEmailId;
        this.mStringGoogleId = mStringGoogleId;
        this.mStringFacebookId = mStringFacebookId;
        this.mStringScoialType = mStringScoialType;
        this.mStringDeviceToken = mStringDeviceToken;
        this.mStringDeviceType = mStringDeviceType;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("firstname",mStringFirstName);
            dataJSONTemp.put("lastname",mStringLastName);
            dataJSONTemp.put("emailid",mStringEmailId);
            dataJSONTemp.put("GoogleId",mStringGoogleId);
            dataJSONTemp.put("FacebookId",mStringFacebookId);
            dataJSONTemp.put("Socialtype",mStringScoialType);
            dataJSONTemp.put("DeviceToken",mStringDeviceToken);
            dataJSONTemp.put("DeviceType",mStringDeviceType);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
