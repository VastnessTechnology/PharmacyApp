package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.DeleteAddressInfo;
import com.vgroyalchemist.requestobjects.ReOrderInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class ReOrderService {

    public JSONObject generateRequestJSON(String orderid, String Userid) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        ReOrderInfo mReOrderInfo = new ReOrderInfo(orderid ,Userid);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(mReOrderInfo);
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

    public ServerResponseVO ReOrders(Context context, String url, String orderid ,String Userid) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(orderid ,Userid);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Re order ....." + url);
            Utility.debugger("VT Re order request....." + requestJSON);
            Utility.debugger("VT responseJSON  Re order..... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.optString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.optString(Tags.PARAM_MSG));
                if(responseJSON.getJSONObject("details").optString("CartCount") != null) {
                    serverResponseVO.setCartCount(responseJSON.optJSONObject("details").optString("CartCount"));
                }

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