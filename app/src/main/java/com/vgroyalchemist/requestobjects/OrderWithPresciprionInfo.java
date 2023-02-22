package com.vgroyalchemist.requestobjects;


import com.vgroyalchemist.views.FragmentOrderSummary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderWithPresciprionInfo extends ParentRequestVO {


    String mStringAddressId = "";
    String mStringUserId ="";
    ArrayList mStringImageBase64;
    String Image;
    String mStringLatitude="";
    String mStringLongitude ="";

    public OrderWithPresciprionInfo(String mStringUserId , String mStringAddressId ,ArrayList mStringImageBase64 ,String mStringLatitude, String mStringLongitude) {
        this.mStringAddressId = mStringAddressId;
        this.mStringUserId= mStringUserId;
        this.mStringImageBase64= mStringImageBase64;
        this.mStringLatitude= mStringLatitude;
        this.mStringLongitude = mStringLongitude;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("AddressId",mStringAddressId);
            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("Latitude",mStringLatitude);
            dataJSONTemp.put("Longitude", mStringLongitude);
            JSONArray productJSON = new JSONArray();
            productJSON.put(FragmentOrderSummary.createCustomOptionJson());
//            Utility.debugger("VT put productJSON Confrome Order With Pres...." + productJSON);
            Image = productJSON.toString().replace("[", "").replace("]", "");
            dataJSONTemp.put("Img",Image);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
