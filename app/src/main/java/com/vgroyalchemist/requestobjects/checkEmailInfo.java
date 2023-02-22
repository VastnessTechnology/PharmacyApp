package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class checkEmailInfo extends ParentRequestVO {

    String mStringEmail;
    String mStringUSerID;


    public checkEmailInfo(String mStringUSerID, String mStringEmail) {

        this.mStringUSerID = mStringUSerID;
        this.mStringEmail = mStringEmail;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {

            dataJSONTemp.put("emailid", mStringEmail); // MobileNo
            dataJSONTemp.put("userid", mStringUSerID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJSONTemp;
    }
}