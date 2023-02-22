package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class ReadNotiInfo extends ParentRequestVO {


    String stringNotiId="";
    String mStringUserId ="";


    public ReadNotiInfo(String stringNotiId ,String mStringUserId) {
        this.stringNotiId=stringNotiId;
        this.mStringUserId = mStringUserId;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("NotiId",stringNotiId);
            dataJSONTemp.put("UserId",mStringUserId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
