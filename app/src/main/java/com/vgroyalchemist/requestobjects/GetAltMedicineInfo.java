package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class GetAltMedicineInfo extends ParentRequestVO {


    String mStringMedicineId = "";
    String UserId ="";
    String DeviceToken="";

    public GetAltMedicineInfo(String mStringMedicineId ,String UserId,String DeviceToken) {
        this.mStringMedicineId = mStringMedicineId;
        this.UserId=UserId;
        this.DeviceToken= DeviceToken;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("MedicineID",mStringMedicineId);
            dataJSONTemp.put("UserId",UserId);
            dataJSONTemp.put("DeviceToken",DeviceToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
