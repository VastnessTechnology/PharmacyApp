package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class GetCoupenInfo extends ParentRequestVO {


    String mStringUserid;

    public GetCoupenInfo (String mStringUserid) {

    this.mStringUserid = mStringUserid;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserID",mStringUserid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
