package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class SearchMedicineInfo extends ParentRequestVO {


    String mStringSearch = "";
    String DeviceToken= "";
    String mStringUserid="";

    public SearchMedicineInfo(String mStringSearch,String mStringUserid , String DeviceToken) {
        this.mStringSearch = mStringSearch;
        this.mStringUserid = mStringUserid;
        this.DeviceToken = DeviceToken;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("Medicine",mStringSearch);
            dataJSONTemp.put("UserId",mStringUserid);
            dataJSONTemp.put("DeviceToken",DeviceToken);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
