package com.vgroyalchemist.requestobjects;


import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.views.FragmentOrderReturn;
import com.vgroyalchemist.views.FragmentOrderSummary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddorderReturnInfo extends ParentRequestVO {


    String OrderId = "";
    String mStringUserId ="";
    String mStringGetResonid="";
    String mStringAdditiontext="";
    ArrayList mStringImageBase64;
    String Image;

    public AddorderReturnInfo(String OrderId , String mStringUserId , String mStringGetResonid , String mStringAdditiontext, ArrayList mStringImageBase64) {
        this.OrderId = OrderId;
        this.mStringUserId= mStringUserId;
        this.mStringGetResonid= mStringGetResonid;
        this.mStringAdditiontext= mStringAdditiontext;
        this.mStringImageBase64= mStringImageBase64;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("OrderId",OrderId);
            dataJSONTemp.put("Usrid",mStringUserId);
            dataJSONTemp.put("Reason",mStringGetResonid);
            dataJSONTemp.put("Description",mStringAdditiontext);

            JSONArray productJSON = new JSONArray();
            productJSON.put(FragmentOrderReturn.createCustomOptionJsonSingleNew());
            Utility.debugger("VT put productJSON...." + productJSON);
            Image = productJSON.toString().replace("[", "").replace("]", "");
            if(Image.equalsIgnoreCase("{}"))
            {
                dataJSONTemp.put("Img","");
            }
            else {

                dataJSONTemp.put("Img",Image);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
