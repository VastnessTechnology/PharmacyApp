package com.vgroyalchemist.webservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.vgroyalchemist.requestobjects.GetCityInfo;
import com.vgroyalchemist.requestobjects.GetStateInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.GetCityData;
import com.vgroyalchemist.vos.GetStateData;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;

public class FetchCityService {

    public JSONObject generateRequestJSON(String StateId) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetCityInfo getStateInfo = new GetCityInfo(StateId);
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
    public ServerResponseVO fetchcityData(Context context , String url,String StateId) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try{
            JSONObject requestJSON = generateRequestJSON(StateId);
            Utility.debugger("VT city URL...." + url);
            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT city responseJSON...."+responseJSON);
            if(responseJSON != null){
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

                GetCityData.CityDeatils mCityDeatils = new GetCityData.CityDeatils ();

                ArrayList<GetCityData.CityDeatils> mCityDeatilsArrayList = new ArrayList<GetCityData.CityDeatils>();

                try {
                    JSONObject responseData = responseJSON.getJSONObject("details");
                    JSONArray productDetailJSONArray = responseData.getJSONArray("detailary");

                    for (int A = 0; A < productDetailJSONArray.length(); A++) {
                        mCityDeatils = new GetCityData.CityDeatils();

                        JSONObject productDetailJSON = productDetailJSONArray.getJSONObject(A);

                        try {
                            mCityDeatils.setCityId(productDetailJSON.getString("CityId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            mCityDeatils.setCityName(productDetailJSON.getString("CityName"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            mCityDeatils.setStateId(productDetailJSON.getString("StateId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mCityDeatilsArrayList.add(mCityDeatils);
                    }
                    serverResponseVO.setData(mCityDeatilsArrayList);
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
            Utility.debugger("VT city request...."+requestObject.toString());
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