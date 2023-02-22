package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class GetSingalAddressInfo extends ParentRequestVO {


    String Addressid = "";


    public GetSingalAddressInfo(String Addressid) {
            this.Addressid = Addressid;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("AddressId",Addressid);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
