package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class VitalGraphDataInfo extends ParentRequestVO {


    String mStringUserid;
    String mStringVitalId;
    String currentDate;
    String FromDate;
    String Type;


    public VitalGraphDataInfo(String mStringUserid,String mStringVitalId ,String Type) {
        this.mStringUserid= mStringUserid;
        this.mStringVitalId=mStringVitalId;

        this.Type=Type;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserId",mStringUserid);
            dataJSONTemp.put("VitalId",mStringVitalId);
            dataJSONTemp.put("Type",Type);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
