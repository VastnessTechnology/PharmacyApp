package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.SaveMedcineReminderInfo;
import com.vgroyalchemist.requestobjects.SaveVitalInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;

public class SaveVitalService {

    public JSONObject generateRequestJSON(String mStringUserId ,String mStringDate, String mStringTime ,String mStringUnit , String mStringUnitValue, String mStringRemakrs,
                                          String mStringVitalID) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        SaveVitalInfo saveMedcineReminderInfo = new SaveVitalInfo(mStringUserId,mStringDate ,mStringTime,mStringUnit,mStringUnitValue ,mStringRemakrs,mStringVitalID);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(saveMedcineReminderInfo);
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

    public ServerResponseVO AddVital(Context context, String url,String mStringUserId ,String mStringDate, String mStringTime ,String mStringUnit , String mStringUnitValue, String mStringRemakrs,
                                        String mStringVitalID) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserId,mStringDate ,mStringTime,mStringUnit,mStringUnitValue ,mStringRemakrs,mStringVitalID);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT add medicine reminder...." + url);
            Utility.debugger("VT  add medicine reminder Request" + requestJSON.toString());
            Utility.debugger("VT responseJSON  add medicine reminder " + responseJSON);
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