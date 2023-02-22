package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class DeleteVitalInfo extends ParentRequestVO {


    String mstringvidalId="";

    public DeleteVitalInfo(String mstringvidalId) {
        this.mstringvidalId=mstringvidalId;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("VitalId",mstringvidalId);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
