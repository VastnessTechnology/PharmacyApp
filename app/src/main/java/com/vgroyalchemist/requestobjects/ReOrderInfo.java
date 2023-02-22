package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class ReOrderInfo extends ParentRequestVO {


    String orderid="";
    String Userid ="";

    public ReOrderInfo(String orderid ,String Userid) {
        this.orderid=orderid;
        this.Userid =Userid;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("OrderId",orderid);
            dataJSONTemp.put("UserId",Userid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
