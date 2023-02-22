package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetCountDataInfo;
import com.vgroyalchemist.requestobjects.SearchMedicineInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.MedicineSearchList;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class GetCountDataService {

    public JSONObject generateRequestJSON(String mStringUserid , String DeviceToken) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetCountDataInfo searchMedicineInfo = new GetCountDataInfo(mStringUserid ,DeviceToken);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(searchMedicineInfo);
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

    public ServerResponseVO getdatacount(Context context, String url, String mStringUserid, String DeviceToken) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserid ,DeviceToken);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT get count url ....." + url);
            Utility.debugger("VT  request get count ....." + requestJSON);
            Utility.debugger("VT responseJSON get count... " + responseJSON);
            serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
            serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

            if(responseJSON.getJSONObject("details").getString("CartCount") != null) {
                serverResponseVO.setCartCount(responseJSON.getJSONObject("details").getString("CartCount"));
            }
            if(responseJSON.getJSONObject("details").getString("NotiCount") != null) {
                serverResponseVO.setNotiCount(responseJSON.getJSONObject("details").getString("NotiCount"));
            }
            serverResponseVO.setData(serverResponseVO);


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