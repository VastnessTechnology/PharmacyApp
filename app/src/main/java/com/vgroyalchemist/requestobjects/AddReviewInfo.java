package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class AddReviewInfo extends ParentRequestVO {


    String mStringMedicineId="";
    String mStringUserId ="";
    String mStringReviewText ="";
    String mStringRating ="";

    public AddReviewInfo(String mStringMedicineId, String mStringUserId ,String mStringReviewText,String mStringRating) {
        this.mStringMedicineId=mStringMedicineId;
        this.mStringUserId =mStringUserId;
        this.mStringReviewText=mStringReviewText;
        this.mStringRating=mStringRating;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("MedicineId",mStringMedicineId);
            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("ReviewText",mStringReviewText);
            dataJSONTemp.put("Review",mStringRating);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
