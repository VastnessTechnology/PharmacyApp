package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class ScheduleReminderInfo extends ParentRequestVO {


    String mStringReminderId = "";
    String mStringSoonzeTime="";
    String mStringAction="";


    public ScheduleReminderInfo(String mStringReminderId ,String mStringSoonzeTime,String mStringAction) {
        this.mStringReminderId = mStringReminderId;
        this.mStringSoonzeTime = mStringSoonzeTime;
        this.mStringAction = mStringAction;


    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("ScheduleReminderId",mStringReminderId);
            dataJSONTemp.put("snoozeTime",mStringSoonzeTime);
            dataJSONTemp.put("action",mStringAction);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
