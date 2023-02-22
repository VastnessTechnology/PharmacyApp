package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetCoupenInfo;
import com.vgroyalchemist.requestobjects.PlaceOrederInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.CounponlistTypeVO;
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


public class GetCoupenService {

    public JSONObject generateRequestJSON(String mStringUserid ) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetCoupenInfo placeOrederInfo = new GetCoupenInfo(mStringUserid);
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

    public ServerResponseVO getcoupen(Context context, String url,String mStringUserid) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserid);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT get coupen url....." + url);
            Utility.debugger("VT get coupen request....." + requestJSON);
            Utility.debugger("VT get coupen Cart.... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

                CounponlistTypeVO counponlistTypeVO = new CounponlistTypeVO();
                ArrayList<CounponlistTypeVO> counponlistTypeVOArrayList = new ArrayList<CounponlistTypeVO>();

                try {
                    JSONObject responseData = responseJSON.getJSONObject("details");
                    JSONArray jsonArray = responseData.getJSONArray("detailary");


                    for (int A = 0; A < jsonArray.length(); A++) {
                        counponlistTypeVO = new CounponlistTypeVO();

                        JSONObject productDetailJSON = jsonArray.getJSONObject(A);

                        try {
                            counponlistTypeVO.setCoupenCode(productDetailJSON.getString("CoupenCode"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            counponlistTypeVO.setDescription(productDetailJSON.getString("Description"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            counponlistTypeVO.setExpiryDate(productDetailJSON.getString("EndDate"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        counponlistTypeVOArrayList.add(counponlistTypeVO);

                    }

                    serverResponseVO.setData(counponlistTypeVOArrayList);
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