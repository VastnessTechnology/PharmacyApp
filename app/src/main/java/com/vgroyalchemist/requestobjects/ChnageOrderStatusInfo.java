package com.vgroyalchemist.requestobjects;


import com.vgroyalchemist.views.FragmentPaymentDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChnageOrderStatusInfo extends ParentRequestVO {


    String mStringUserId = "";
    String OrderId = "";
    String PaymentType = "";
    String OrderStatus = "";

    public ChnageOrderStatusInfo(String mStringUserId, String OrderId, String PaymentType, String OrderStatus) {
        this.mStringUserId = mStringUserId;
        this.OrderId=OrderId;
        this.PaymentType=PaymentType;
        this.OrderStatus = OrderStatus;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {

            dataJSONTemp.put("UserId", mStringUserId);
            dataJSONTemp.put("OrderId", OrderId);
            dataJSONTemp.put("PaymentType", PaymentType);
            dataJSONTemp.put("OrderStatus", OrderStatus);




        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
