package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class ShowAllVitalListInfo extends ParentRequestVO {

        String mStringUserid;
        String mStringVitalId;



    public ShowAllVitalListInfo(String mStringUserid,String mStringVitalId) {
        this.mStringUserid=mStringUserid;
        this.mStringVitalId=mStringVitalId;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {

            dataJSONTemp.put("UserId",mStringUserid);
            dataJSONTemp.put("VitalID",mStringVitalId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
