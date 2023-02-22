package com.vgroyalchemist.requestobjects;


import org.json.JSONArray;
import org.json.JSONObject;

public class SaveVitalInfo extends ParentRequestVO {


    String mStringUserId = "";
    String mStringDate = "";
    String mStringTime = "";
    String mStringUnit = "";
    String mStringUnitValue = "";
    String mStringVitalID = "";
    String mStringRemakrs = "";
    String mStringMedicineId = "";
    String mStringWeekDay="";
    JSONArray jsonArray;

    public SaveVitalInfo(String mStringUserId ,String mStringDate, String mStringTime ,String mStringUnit , String mStringUnitValue, String mStringRemakrs,
                         String mStringVitalID) {
        this.mStringUserId = mStringUserId;
        this.mStringDate = mStringDate;
        this.mStringTime = mStringTime;
        this.mStringUnit = mStringUnit;
        this.mStringUnitValue = mStringUnitValue;
        this.mStringVitalID = mStringVitalID;
        this.mStringRemakrs = mStringRemakrs;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("VitalDate",mStringDate);
            dataJSONTemp.put("VitalTime",mStringTime);
            dataJSONTemp.put("Unit",mStringUnit);
            dataJSONTemp.put("Value",mStringUnitValue);
            dataJSONTemp.put("VitalId",mStringVitalID);
            dataJSONTemp.put("Remark",mStringRemakrs);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
