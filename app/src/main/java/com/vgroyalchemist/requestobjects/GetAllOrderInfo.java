package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class GetAllOrderInfo extends ParentRequestVO {


    String mStringUserId = "";
    String mStringAddressId ="";

    public GetAllOrderInfo(String mStringUserId ) {
        this.mStringUserId = mStringUserId;


    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserId",mStringUserId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
