package com.vgroyalchemist.webservice;

import android.content.Context;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;
import com.vgroyalchemist.requestobjects.GetAllCartInfo;
import com.vgroyalchemist.requestobjects.PlaceOrederInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.AllCartData;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.TaxChargeVO;
import com.vgroyalchemist.vos.TotalArrayVo;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


public class PlaceOrderService {

    public JSONObject generateRequestJSON(String mStringUserId,String AddressID ,String mStringSubtotal,String ShippingCharge,String GorssTotal,String CoupneDisc,String mStringDiscount ,String PaymentMethod ,
                                          String OrderStatus ,String PaymentReceived ,ArrayList mImageVOArrayList , String mStringLatitude , String mStringLongitude) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        PlaceOrederInfo placeOrederInfo = new PlaceOrederInfo(mStringUserId ,AddressID ,mStringSubtotal ,ShippingCharge ,GorssTotal ,CoupneDisc,mStringDiscount,PaymentMethod ,OrderStatus,PaymentReceived ,mImageVOArrayList ,mStringLatitude,mStringLongitude);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(placeOrederInfo);
            try {
                mObject =  requestObject.getJSONObject("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        return mObject;
    }

    public ServerResponseVO getAllCart(Context context, String url, String mStringUserId,String AddressID ,String mStringSubtotal,String ShippingCharge,String GorssTotal,String CoupneDisc ,String mStringDiscount,String PaymentMethod
            ,String OrderStatus ,String PaymentReceived ,ArrayList mImageVOArrayList , String mStringLatitude , String mStringLongitude) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserId,AddressID,mStringSubtotal ,ShippingCharge ,GorssTotal ,CoupneDisc ,mStringDiscount,PaymentMethod,OrderStatus,PaymentReceived,mImageVOArrayList ,mStringLatitude,mStringLongitude);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Place Order url....." + url);
            Utility.debugger("VT Place Order request....." + requestJSON);
            Utility.debugger("VT Place Order.... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));
                serverResponseVO.setOrderNo(responseJSON.getJSONObject("details").optString("OrderNo"));
                serverResponseVO.setOrderStatus(responseJSON.getJSONObject("details").optString("OrderStatus"));
                serverResponseVO.setData(responseJSON);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return serverResponseVO;
    }

    public JSONObject sendRequest(String url, JSONObject requestObject, Context context) throws
            Exception {
        JSONObject responseObject = null;
        ArrayList<Long> list = new ArrayList<Long>(0);
        try {

            responseObject = HttpConnector.getJSONObject(url, requestObject);
        } catch (MalformedURLException mue) {
            Utility.debugger("MalformedURLException");
            mue.printStackTrace();
            throw mue;
        } catch (SocketTimeoutException ste) {
            Utility.debugger("SocketTimeoutException");
            ste.printStackTrace();
            throw ste;
        } catch (IOException ioe) {
            Utility.debugger("IOException");
            ioe.printStackTrace();
            throw ioe;
        } catch (JSONException je) {
            Utility.debugger("JSONException");
            je.printStackTrace();
            throw je;
        } catch (Exception e) {
            Utility.debugger("Exception");
            e.printStackTrace();
            throw e;
        } finally {
            //return isError;
        }
        return responseObject;
    }
}