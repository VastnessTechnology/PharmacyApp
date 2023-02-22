package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class BannerMenuListInfo extends ParentRequestVO {


    String MenuName;

    public BannerMenuListInfo(String MenuName) {

        this.MenuName =MenuName;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("MenuName",MenuName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
