package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class GetZoneCityInfo extends ParentRequestVO {

    public static final String PARAM_COUNTRY_CODE = "country_id";
    String mStringCountryCode;

    public GetZoneCityInfo(String mStringCountryCode){
        this.mStringCountryCode = mStringCountryCode;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put(PARAM_COUNTRY_CODE, mStringCountryCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJSONTemp;
    }
}