package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.MyPrescriptionInfo;
import com.vgroyalchemist.requestobjects.PresOrderListInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.MYPrescriptionVO;
import com.vgroyalchemist.vos.PresOrderList;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class PresOrderListService {

    public JSONObject generateRequestJSON(String mStringUserId) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        PresOrderListInfo presOrderListInfo = new PresOrderListInfo(mStringUserId);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(presOrderListInfo);
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

    public ServerResponseVO GetPresciptionOrderList(Context context, String url, String mStringUserId) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {

            JSONObject requestJSON = generateRequestJSON(mStringUserId);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Get Presciption Order ....." + url);
            Utility.debugger("VT Get Presciption Order  request....." + requestJSON);
            Utility.debugger("VT responseJSON  Presciption Order ..... " + responseJSON);
            serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
            serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

            PresOrderList presOrderList = new PresOrderList();
            ArrayList<PresOrderList> orderLists = new ArrayList<PresOrderList>();

            try {
                JSONObject responseData = responseJSON.getJSONObject("details");
                JSONArray jsonArray = responseData.getJSONArray("detailary");

                for (int m = 0; m < jsonArray.length(); m++) {

                    presOrderList = new PresOrderList();

                    JSONObject mJsonObject = jsonArray.getJSONObject(m);


                    try {
                        presOrderList.setSrNo(mJsonObject.optString("SrNo"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        presOrderList.setDate(mJsonObject.optString("AddedDate"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        presOrderList.setOrderId(mJsonObject.optString("OrderId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        presOrderList.setOrderStatus(mJsonObject.optString("OrderStatus"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        presOrderList.setPrescriptionId(mJsonObject.optString("PrescriptionId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    try {
//                        presOrderList.setUserName(mJsonObject.getString("UserName"));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    orderLists.add(presOrderList);
                }
                serverResponseVO.setData(orderLists);

            }
            catch (Exception e) {
                e.printStackTrace();
                throw e;
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
            Utility.debugger("VT Get Defualt Address......" + requestObject.toString());
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