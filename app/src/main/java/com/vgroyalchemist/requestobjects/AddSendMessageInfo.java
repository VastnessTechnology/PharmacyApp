package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class AddSendMessageInfo extends ParentRequestVO {

    String mStringUserId ="";
    String mStringOrderId ="";
    String mStringComplainMsg ="";


    public AddSendMessageInfo(String  mStringUserId , String mStringOrderId, String mStringComplainMsg ) {
        this.mStringUserId =mStringUserId;
        this.mStringOrderId=mStringOrderId;
        this.mStringComplainMsg=mStringComplainMsg;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {

            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("OrderId",mStringOrderId);
            dataJSONTemp.put("ordmessage",mStringComplainMsg);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
