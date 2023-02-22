package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetAllOrderInfo;
import com.vgroyalchemist.requestobjects.TrackOrderInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.OrderDataVo;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;
import com.vgroyalchemist.vos.TrackOrderVO;


public class TrackOrderService {

    public JSONObject generateRequestJSON(String mStringUserId , String OrderId) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        TrackOrderInfo mGetAllOrderInfo = new TrackOrderInfo(mStringUserId,OrderId);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(mGetAllOrderInfo);
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

    public ServerResponseVO TrackOrder(Context context, String url, String mStringUserId , String OrderId) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserId ,OrderId);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Track Order url....." + url);
            Utility.debugger("VT Track Order request....." + requestJSON);
            Utility.debugger("VT Track Order Cart.... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

                TrackOrderVO TrackDataVo = new TrackOrderVO();
                ArrayList<TrackOrderVO> trackOrderVOS = new ArrayList<TrackOrderVO>();

                try {
//                    JSONObject responseData = responseJSON.getJSONObject("details");
                    JSONArray responseDataJSONArray = responseJSON.getJSONArray("details");

                    for (int A = 0; A < responseDataJSONArray.length(); A++) {
                        TrackDataVo = new TrackOrderVO();

                        JSONObject jsonArrayJSONObject = responseDataJSONArray.getJSONObject(A);

                        try {
                            TrackDataVo.setOrderStatusId(jsonArrayJSONObject.getString("OrderStatusId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            TrackDataVo.setOrderStatusName(jsonArrayJSONObject.getString("OrderStatusName"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            TrackDataVo.setOrderStatusValue(jsonArrayJSONObject.getString("OrderStatusValue"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            TrackDataVo.setDeliveryboyid(jsonArrayJSONObject.getString("deliveryboyid"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        trackOrderVOS.add(TrackDataVo);
                    }

                    serverResponseVO.setData(trackOrderVOS);
                }
                catch (Exception e)
                {
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