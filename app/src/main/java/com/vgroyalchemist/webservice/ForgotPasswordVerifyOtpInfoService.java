package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.ForgotPasswordEmailInfo;
import com.vgroyalchemist.requestobjects.ForgotPasswordVerifyOtpInfo;
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


public class ForgotPasswordVerifyOtpInfoService {

    public JSONObject generateRequestJSON(String mStringGetEmailId, String mStringGetotpValue) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        ForgotPasswordVerifyOtpInfo mFetchDeviceInfo = new ForgotPasswordVerifyOtpInfo(mStringGetEmailId,mStringGetotpValue);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(mFetchDeviceInfo);
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

    public ServerResponseVO fetchSignupUserInformation(Context context, String url,String mStringGetEmailId, String mStringGetotpValue) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();
        try {
            JSONObject requestJSON = generateRequestJSON(mStringGetEmailId,mStringGetotpValue);


            Utility.debugger("VT ForgotPassword otp url...." + url);
            Utility.debugger("VT ForgotPassword otp request...." + requestJSON);
            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT ForgotPassword otp responseJSON..." + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));
                serverResponseVO.setUsrid(responseJSON.getJSONObject("details").getString("Usrid"));
                serverResponseVO.setEmailId(responseJSON.getJSONObject("details").getString("EmailId"));
//
                serverResponseVO.setIsActive(responseJSON.getJSONObject("details").getString("IsActive"));

//                if (serverResponseVO.getStatus().equalsIgnoreCase("1")) {
                    serverResponseVO.setData(responseJSON);
//                }
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