package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;


public class GetCityInfo extends ParentRequestVO {

    String StateId;

    public GetCityInfo(String StateId) {
        this.StateId=StateId;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();

        try {
            dataJSONTemp.put("StateId",StateId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJSONTemp;
    }
}
