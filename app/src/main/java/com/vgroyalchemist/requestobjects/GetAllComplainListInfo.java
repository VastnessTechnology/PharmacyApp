package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class GetAllComplainListInfo extends ParentRequestVO {


    String mStringUserId = "";
    String mStringMedicineId="";
    String ComplainId="";

    public GetAllComplainListInfo(String mStringUserId ,String mStringMedicineId ,String ComplainId) {
        this.mStringUserId = mStringUserId;
            this.mStringMedicineId= mStringMedicineId;
            this.ComplainId=ComplainId;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("MedicineId",mStringMedicineId);
            dataJSONTemp.put("ComplainId",ComplainId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
