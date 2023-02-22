package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class CheckPatientUserInfo extends ParentRequestVO {

    String mStringDeviceToken;
    String mStringPatientId;
    String mStringDeviceType;


    public CheckPatientUserInfo(String mStringPatientId,
                                String mStringDeviceToken, String mStringDeviceType) {

        this.mStringPatientId = mStringPatientId;
        this.mStringDeviceToken = mStringDeviceToken;
        this.mStringDeviceType = mStringDeviceType;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {

            dataJSONTemp.put("PatientId", mStringPatientId); // MobileNo
            dataJSONTemp.put("devicetype", mStringDeviceType); // Device Type
            dataJSONTemp.put("devicetoken", mStringDeviceToken); // Device Token


        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJSONTemp;
    }
}