package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class AddBookMarkInfo extends ParentRequestVO {

    String mStringArticlesId;
    String mStringUserid;


    public AddBookMarkInfo(String mStringArticlesId ,String mStringUserid) {
        this.mStringArticlesId =mStringArticlesId;
        this.mStringUserid=mStringUserid;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("ArticleId",mStringArticlesId);
            dataJSONTemp.put("UserId",mStringUserid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
