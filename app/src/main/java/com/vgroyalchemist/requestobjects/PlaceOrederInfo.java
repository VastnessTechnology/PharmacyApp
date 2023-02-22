package com.vgroyalchemist.requestobjects;


import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.views.FragmentCart;
import com.vgroyalchemist.views.FragmentPaymentDetails;
import com.vgroyalchemist.views.FragmentSearchOrderSummary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaceOrederInfo extends ParentRequestVO {


    String mStringUserId = "";
    String mStringSubtotal ="";
    String ShippingCharge="";
    String GorssTotal="";
    String CoupneDisc="";
    String AddressID="";
    String Image ="";
    String mStringDiscount="";
    ArrayList mImageVOArrayList;
    String OrderStatus="";
    String PaymentMethod="";
    String PaymentReceived="";
    String mStringLatitude="";
    String mStringLongitude="";


    public PlaceOrederInfo(String mStringUserId , String AddressID , String mStringSubtotal, String ShippingCharge, String GorssTotal, String CoupneDisc ,String mStringDiscount,String PaymentMethod,String OrderStatus,
                           String PaymentReceived, ArrayList mImageVOArrayList ,  String mStringLatitude , String mStringLongitude) {
        this.mStringUserId = mStringUserId;
        this.mStringSubtotal= mStringSubtotal;
        this.ShippingCharge =ShippingCharge;
        this.GorssTotal =GorssTotal;
        this.CoupneDisc=CoupneDisc;
        this.AddressID =AddressID;
        this.mImageVOArrayList= mImageVOArrayList;
        this.PaymentMethod=PaymentMethod;
        this.mStringDiscount=mStringDiscount;
        this.OrderStatus=OrderStatus;
        this.PaymentReceived=PaymentReceived;
        this.mStringLatitude= mStringLatitude;
        this.mStringLongitude=mStringLongitude;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {

            dataJSONTemp.put("Usrid",mStringUserId);
            dataJSONTemp.put("AddressId",AddressID);
            dataJSONTemp.put("TotalRs",mStringSubtotal);
            dataJSONTemp.put("DeliveryCharge",ShippingCharge);
            dataJSONTemp.put("GrossRs",GorssTotal);
            dataJSONTemp.put("CoupenDisc",CoupneDisc);
            dataJSONTemp.put("ItemTotalDisc",mStringDiscount);
            dataJSONTemp.put("PaymentType",PaymentMethod);
            dataJSONTemp.put("OrderStatus",OrderStatus);
            dataJSONTemp.put("PaymentReceived",PaymentReceived);
            dataJSONTemp.put("latitude",mStringLatitude);
            dataJSONTemp.put("longitude",mStringLongitude);

            JSONArray productJSON = new JSONArray();
            productJSON.put(FragmentPaymentDetails.CreatePlaceOrderJson());
//            Utility.debugger("VT put productJSON...." + productJSON);
            Image = productJSON.toString().replace("[", "").replace("]", "");
            dataJSONTemp.put("Img",Image);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
