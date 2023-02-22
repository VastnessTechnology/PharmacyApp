package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class CheckUserAddressInfo extends ParentRequestVO {


    String mStringUserId = "";


    public CheckUserAddressInfo(String mStringUserId) {
        this.mStringUserId = mStringUserId;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("Usrid",mStringUserId);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
