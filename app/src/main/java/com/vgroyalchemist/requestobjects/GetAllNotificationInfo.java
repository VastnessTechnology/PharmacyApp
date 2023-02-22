package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class GetAllNotificationInfo extends ParentRequestVO {

    String UserId;

    public GetAllNotificationInfo(String UserId) {
        this.UserId=UserId;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {

            dataJSONTemp.put("UserId",UserId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
