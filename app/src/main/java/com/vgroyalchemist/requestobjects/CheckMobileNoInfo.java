package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class CheckMobileNoInfo extends ParentRequestVO {


    String mStringUserId = "";
    String ContactNumber="";


    public CheckMobileNoInfo(String mStringUserId,String ContactNumber) {
        this.mStringUserId = mStringUserId;
        this.ContactNumber=ContactNumber;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("MobileNo",ContactNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
