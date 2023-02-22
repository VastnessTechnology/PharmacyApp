package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class TrackOrderInfo extends ParentRequestVO {


    String mStringUserId = "";
    String OrderId ="";

    public TrackOrderInfo(String mStringUserId , String OrderId) {
        this.mStringUserId = mStringUserId;
        this.OrderId= OrderId;


    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("OrderId",OrderId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
