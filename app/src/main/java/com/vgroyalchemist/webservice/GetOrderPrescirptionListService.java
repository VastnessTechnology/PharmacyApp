package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetOrderPrescriptionInfo;
import com.vgroyalchemist.requestobjects.MyPrescriptionInfo;
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


public class GetOrderPrescirptionListService {

    public JSONObject generateRequestJSON(String mStringOrderId ,String mStringUserid) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetOrderPrescriptionInfo getOrderPrescriptionInfo = new GetOrderPrescriptionInfo(mStringOrderId ,mStringUserid);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(getOrderPrescriptionInfo);
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

    public ServerResponseVO GetPresciptionList(Context context, String url, String mStringOrderId ,String mStringUserid) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {

            JSONObject requestJSON = generateRequestJSON(mStringOrderId ,mStringUserid);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT  Presciption....." + url);
            Utility.debugger("VT  Presciption request....." + requestJSON);
            Utility.debugger("VT responseJSON  Presciption..... " + responseJSON);
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
                        presOrderList.setImageName(mJsonObject.getString("ImageName"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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