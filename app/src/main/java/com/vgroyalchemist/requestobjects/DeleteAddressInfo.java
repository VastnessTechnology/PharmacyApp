package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class DeleteAddressInfo extends ParentRequestVO {


    String mStringAddressId="";

    public DeleteAddressInfo( String mStringAddressId) {
        this.mStringAddressId=mStringAddressId;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("AddressId",mStringAddressId);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
