package com.vgroyalchemist.requestobjects;


import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.views.FragmentCart;
import com.vgroyalchemist.views.FragmentOrderReturn;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckPrescReqInfo extends ParentRequestVO {


    String OrderId = "";
    String mStringUserId ="";
    String mStringGetResonid="";
    String mStringAdditiontext="";
    ArrayList mStringImageBase64;
    String Image ="";


    public CheckPrescReqInfo( String mStringUserId , ArrayList mStringImageBase64) {
        this.OrderId = OrderId;
        this.mStringUserId= mStringUserId;
        this.mStringImageBase64= mStringImageBase64;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {

            dataJSONTemp.put("Usrid",mStringUserId);

            JSONArray productJSON = new JSONArray();
            productJSON.put(FragmentCart.UploadBase64InOreder());
//            Utility.debugger("VT put productJSON...." + productJSON);
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
