package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class AddDeliveryFeedbackInfo extends ParentRequestVO {


    String mStringOrderId="";
    String mStringReviewText ="";
    String mStringRating ="";

    public AddDeliveryFeedbackInfo(String mStringOrderId ,String mStringReviewText,String mStringRating) {
        this.mStringOrderId=mStringOrderId;
        this.mStringReviewText=mStringReviewText;
        this.mStringRating=mStringRating;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("OrderID",mStringOrderId);
            dataJSONTemp.put("ReviewText",mStringReviewText);
            dataJSONTemp.put("Review",mStringRating);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
