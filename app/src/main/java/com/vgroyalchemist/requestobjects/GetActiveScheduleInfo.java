package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class GetActiveScheduleInfo extends ParentRequestVO {


    String mStringUserId = "";
    String currentDate="";


    public GetActiveScheduleInfo(String mStringUserId ,String currentDate) {
        this.mStringUserId = mStringUserId;
        this.currentDate =currentDate;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("currentDate",currentDate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
