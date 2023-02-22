package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class GetAllReviewInfo extends ParentRequestVO {


    String mStringMedicineId = "";
    String mStringAddressId ="";

    public GetAllReviewInfo(String mStringMedicineId ) {
        this.mStringMedicineId = mStringMedicineId;


    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("MedicineId",mStringMedicineId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
