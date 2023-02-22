package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class OrderFilterApplyInfo extends ParentRequestVO {


    String mStringUserId = "";
    String mStringOrderid = "";
    String mStringTimeId = "";


    public OrderFilterApplyInfo(String mStringUserId ,String mStringOrderid , String mStringTimeId ) {
        this.mStringUserId = mStringUserId;
        this.mStringOrderid = mStringOrderid;
        this.mStringTimeId = mStringTimeId;


    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("orderFilterId",mStringOrderid);
            dataJSONTemp.put("timeFilterId",mStringTimeId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
