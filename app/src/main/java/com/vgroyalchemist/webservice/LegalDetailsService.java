package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.LegalDetailsInfo;
import com.vgroyalchemist.requestobjects.OrderDetailsInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.LegalDetailsVO;
import com.vgroyalchemist.vos.OrderDetailsVO;
import com.vgroyalchemist.vos.OrderItemListVO;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class LegalDetailsService {

    public JSONObject generateRequestJSON() {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        LegalDetailsInfo mReOrderInfo = new LegalDetailsInfo();
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

    public ServerResponseVO LegalDetails(Context context, String url) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON();

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT legal details ....." + url);
            Utility.debugger("VT legal details request....." + requestJSON);
            Utility.debugger("VT responseJSON  legal..... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));


                LegalDetailsVO legalDetailsVO = new LegalDetailsVO();

                try {
                    JSONObject responseData = responseJSON.getJSONObject("details");

                    try {
                        legalDetailsVO.setPrivacyPolicy(responseData.optString("PrivacyPolicy"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        legalDetailsVO.setReturnRefundPolicy(responseData.optString("ReturnRefundPolicy"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        legalDetailsVO.setTermsCondition(responseData.optString("TermsCondition"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    serverResponseVO.setData(legalDetailsVO);

                }
                catch (Exception e) {
                    e.printStackTrace();
                }



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