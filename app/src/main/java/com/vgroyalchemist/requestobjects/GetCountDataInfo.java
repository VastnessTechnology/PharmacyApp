package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class GetCountDataInfo extends ParentRequestVO {


    String mStringUserid = "";
    String DeviceToken ="";

    public GetCountDataInfo(String mStringUserid,String DeviceToken) {
        this.mStringUserid = mStringUserid;
        this.DeviceToken = DeviceToken;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("Usrid",mStringUserid);
            dataJSONTemp.put("devicetoken",DeviceToken);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
