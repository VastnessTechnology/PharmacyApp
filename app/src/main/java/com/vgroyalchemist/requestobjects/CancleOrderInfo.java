package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class CancleOrderInfo extends ParentRequestVO {


    String orderid="";
    String Userid ="";
    String mStringComment="";

    public CancleOrderInfo(String orderid , String Userid ,String mStringComment) {
        this.orderid=orderid;
        this.Userid =Userid;
        this.mStringComment= mStringComment;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("OrderId",orderid);
            dataJSONTemp.put("Usrid",Userid);
            dataJSONTemp.put("CancelReason",mStringComment);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
