package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class BookMarkArticlesListInfo extends ParentRequestVO {

    String mStringUserid;
    String Devicetoken;
    public BookMarkArticlesListInfo(String mStringUserid , String Devicetoken) {
        this.mStringUserid=mStringUserid;
        this.Devicetoken= Devicetoken;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserID",mStringUserid);
            dataJSONTemp.put("devicetoken",Devicetoken);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
