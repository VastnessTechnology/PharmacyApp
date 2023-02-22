package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class DeleteNotiInfo extends ParentRequestVO {


    String stringNotiId="";

    public DeleteNotiInfo(String stringNotiId) {
        this.stringNotiId=stringNotiId;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("NotiId",stringNotiId);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
