package com.vgroyalchemist.requestobjects;


import org.json.JSONArray;
import org.json.JSONObject;

public class SaveMedcineReminderInfo extends ParentRequestVO {


    String mStringUserId = "";
    String mStringMedicineName = "";
    String mStringSatrtDate = "";
    String mStringEndDate = "";
    String mStringSchedulePattern = "";
    String mStringIntervel = "";
    String mStringRemark = "";
    String mStringMedicineId = "";
    String mStringWeekDay="";
    JSONArray jsonArray;

    public SaveMedcineReminderInfo(String mStringUserId ,String mStringRemark, String mStringMedicineId , String mStringMedicineName , String mStringSatrtDate, String mStringEndDate,
                                   String mStringSchedulePattern, String mStringIntervel,String mStringWeekDay,JSONArray jsonArray) {
        this.mStringUserId = mStringUserId;
        this.mStringMedicineName = mStringMedicineName;
        this.mStringSatrtDate = mStringSatrtDate;
        this.mStringEndDate = mStringEndDate;
        this.mStringSchedulePattern = mStringSchedulePattern;
        this.mStringIntervel = mStringIntervel;
        this.mStringRemark = mStringRemark;
        this.mStringMedicineId = mStringMedicineId;
        this.mStringWeekDay= mStringWeekDay;
        this.jsonArray= jsonArray;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("MedicineName",mStringMedicineName);
            dataJSONTemp.put("StartDate",mStringSatrtDate);
            dataJSONTemp.put("Enddate",mStringEndDate);
            dataJSONTemp.put("SchdulePattener",mStringSchedulePattern);
            dataJSONTemp.put("Interval",mStringIntervel);
            dataJSONTemp.put("Reamrks",mStringRemark);
            dataJSONTemp.put("MedicineId",mStringMedicineId);
            dataJSONTemp.put("WeekDay",mStringWeekDay);
            dataJSONTemp.put("TblScheduleReminder",jsonArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
