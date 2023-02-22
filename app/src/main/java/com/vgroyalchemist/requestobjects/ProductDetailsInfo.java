package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class ProductDetailsInfo extends ParentRequestVO {

    String mStringmedicineId;
    String mStringUserid;
    String DeviceToken;
    public ProductDetailsInfo(String mStringmedicineId , String mStringUserid, String DeviceToken) {
        this.mStringmedicineId =mStringmedicineId;
        this.mStringUserid= mStringUserid;
        this.DeviceToken= DeviceToken;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {

            dataJSONTemp.put("MedicineID",mStringmedicineId);
            dataJSONTemp.put("UserId",mStringUserid);
            dataJSONTemp.put("DeviceToken",DeviceToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
