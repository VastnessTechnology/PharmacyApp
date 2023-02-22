package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class ScheduleDetailsInfo extends ParentRequestVO {


    String mStringScheduleId="";


    public ScheduleDetailsInfo(String mStringScheduleId) {
        this.mStringScheduleId=mStringScheduleId;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("ScheduleId",mStringScheduleId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
