package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class GetAllArticlesListInfo extends ParentRequestVO {

    String mStringUserid;
    String mStringDeviceToken;
    public GetAllArticlesListInfo(String mStringUserid , String mStringDeviceToken) {

        this.mStringUserid=mStringUserid;
        this.mStringDeviceToken = mStringDeviceToken;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {

            dataJSONTemp.put("UserId",mStringUserid);
            dataJSONTemp.put("devicetoken",mStringDeviceToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
