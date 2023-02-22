package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class GetOrderPrescriptionInfo extends ParentRequestVO {


    String mStringOrderId ;
    String mStringUserid;

    public GetOrderPrescriptionInfo(String mStringOrderId,String mStringUserid) {
        this.mStringOrderId = mStringOrderId;
        this.mStringUserid= mStringUserid;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("SrNo",mStringOrderId);
            dataJSONTemp.put("UserId",mStringUserid);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
