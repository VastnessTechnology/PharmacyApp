package com.vgroyalchemist.webservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.vgroyalchemist.requestobjects.GetStateInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.GetStateData;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.UserDetailsVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;

public class FetchStateService {

    ConnectivityManager mConnectivityManager;
    NetworkInfo mNetworkInfo;
    public boolean isNetError = false;
    public boolean isOtherError = false;
    Context mContext;

    Gson mGson;
    public JSONObject generateRequestJSON() {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetStateInfo getStateInfo = new GetStateInfo();
        try {
            requestObject = mRequestFactory.getFinalRequestObject(getStateInfo);
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
    public ServerResponseVO fetchStateData(Context context , String url) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try{
            JSONObject requestJSON = generateRequestJSON();
            Utility.debugger("VT fetchCountryInfo URL...." + url);
            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT fetchCountryInfo responseJSON...."+responseJSON);
            if(responseJSON != null){
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

                GetStateData.StateDetail stateDetail = new GetStateData.StateDetail ();
                GetStateData getStateData = new GetStateData();
                ArrayList<GetStateData.StateDetail> stateDetails = new ArrayList<GetStateData.StateDetail>();

                try {
                    JSONObject responseData = responseJSON.getJSONObject("details");
                    JSONArray productDetailJSONArray = responseData.getJSONArray("detailary");

                    for (int A = 0; A < productDetailJSONArray.length(); A++) {
                        stateDetail = new GetStateData.StateDetail();

                        JSONObject productDetailJSON = productDetailJSONArray.getJSONObject(A);

                        try {
                            stateDetail.setState(productDetailJSON.getString("State"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            stateDetail.setStateId(productDetailJSON.getString("StateId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        stateDetails.add(stateDetail);
                    }
                    serverResponseVO.setData(stateDetails);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return serverResponseVO;

    }

    public JSONObject sendRequest(String url, JSONObject requestObject, Context context)throws
            Exception{
        JSONObject responseObject = null;
        ArrayList<Long> list = new ArrayList<Long>(0);
        try {
            Utility.debugger("VT URL req...."+requestObject.toString());
            responseObject = HttpConnector.getJSONObject(url, requestObject);
        }catch(MalformedURLException mue){
            Utility.debugger("MalformedURLException");
            mue.printStackTrace();
            throw mue;
        }catch(SocketTimeoutException ste){
            Utility.debugger("SocketTimeoutException");
            ste.printStackTrace();
            throw ste;
        }catch (IOException ioe) {
            Utility.debugger("IOException");
            ioe.printStackTrace();
            throw ioe;
        }catch(JSONException je){
            Utility.debugger("JSONException");
            je.printStackTrace();
            throw je;
        }catch(Exception e){
            Utility.debugger("Exception");
            e.printStackTrace();
            throw e;
        }finally {
            //return isError;
        }
        return responseObject;
    }
}