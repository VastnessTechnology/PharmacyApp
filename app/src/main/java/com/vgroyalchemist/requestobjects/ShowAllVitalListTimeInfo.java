package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class ShowAllVitalListTimeInfo extends ParentRequestVO {

        String mStringUserid;
        String mStringVitalId;
        String mStringVitalDate;


    public ShowAllVitalListTimeInfo(String mStringUserid, String mStringVitalId,String mStringVitalDate) {
        this.mStringUserid=mStringUserid;
        this.mStringVitalId=mStringVitalId;
        this.mStringVitalDate= mStringVitalDate;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {

            dataJSONTemp.put("UserId",mStringUserid);
            dataJSONTemp.put("VitalID",mStringVitalId);
            dataJSONTemp.put("Date",mStringVitalDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
