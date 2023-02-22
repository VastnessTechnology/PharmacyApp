package com.vgroyalchemist.webservice;

import android.content.Context;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;
import com.vgroyalchemist.requestobjects.AddCartInfo;
import com.vgroyalchemist.requestobjects.MedicineListInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.CategoryList;
import com.vgroyalchemist.vos.ServerResponseVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


public class AddCartInfoService {

    public JSONObject generateRequestJSON(String mStringcartid ,String mStringUserid,String mStringprice, String mStringQty, String mStringMedicinId , String DeviceToken) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        AddCartInfo addCartInfo = new AddCartInfo(mStringcartid,mStringUserid,mStringprice,mStringQty,mStringMedicinId,DeviceToken);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(addCartInfo);
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

    public ServerResponseVO AddCartinfo(Context context, String url ,String mStringcartid ,String mStringUserid,String mStringprice, String mStringQty, String mStringMedicinId , String DeviceToken) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringcartid,mStringUserid,mStringprice,mStringQty,mStringMedicinId ,DeviceToken);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Add to cart....." + url);
            Utility.debugger("VT Add to cart...." + requestJSON);
            Utility.debugger("VT Add to cart..... " + responseJSON);
            serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
            serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

            if(responseJSON.getJSONObject("details").getString("TotalCount") != null)
            {
                serverResponseVO.setCartCount(responseJSON.getJSONObject("details").getString("TotalCount"));
            }

            serverResponseVO.setData(responseJSON);


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