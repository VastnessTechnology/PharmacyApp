package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class DeleteCartInfo extends ParentRequestVO {


    String mStringCartId="";

    public DeleteCartInfo(String mStringCartId) {

        this.mStringCartId=mStringCartId;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("CartId",mStringCartId);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
