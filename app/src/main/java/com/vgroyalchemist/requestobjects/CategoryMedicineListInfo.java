package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class CategoryMedicineListInfo extends ParentRequestVO {


    String mStringategoryid = "";
    String UserId;
    String DeviceToken;

    public CategoryMedicineListInfo(String mStringategoryid ,String UserId,String DeviceToken) {
        this.mStringategoryid = mStringategoryid;
        this.UserId =UserId;
        this.DeviceToken =DeviceToken;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("CategoryId",mStringategoryid);
            dataJSONTemp.put("UserId",UserId);
            dataJSONTemp.put("DeviceToken",DeviceToken);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
