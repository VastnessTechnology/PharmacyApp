package com.vgroyalchemist.webservice;

import android.content.Context;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;
import com.vgroyalchemist.requestobjects.AddComplainInfo;
import com.vgroyalchemist.requestobjects.AddReviewInfo;
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


public class AddComplainService {

    public JSONObject generateRequestJSON(String mStringMedicineId, String mStringUserId ,String mStringOrderId,String mStringComplainMsg ,String Status ,String ComplainId, String ComplainFromOrder) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        AddComplainInfo mAddComplainInfo = new AddComplainInfo(mStringMedicineId,mStringUserId ,mStringOrderId,mStringComplainMsg,Status ,ComplainId,ComplainFromOrder);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(mAddComplainInfo);
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

    public ServerResponseVO AddReview(Context context, String url, String mStringMedicineId, String mStringUserId ,String mStringOrderId,String mStringComplainMsg ,String Status ,String ComplainId, String ComplainFromOrder) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringMedicineId ,mStringUserId ,mStringOrderId,mStringComplainMsg ,Status,ComplainId ,ComplainFromOrder);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Add complain ....." + url);
            Utility.debugger("VT Add complain request....." + requestJSON);
            Utility.debugger("VT responseJSON Add complain..... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

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