package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class OrderDetailsInfo extends ParentRequestVO {


    String orderid;


    public OrderDetailsInfo(String orderid) {
        this.orderid=orderid;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("OrderId",orderid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
