package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetAllOrderInfo;
import com.vgroyalchemist.requestobjects.OrderFilterApplyInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.OrderDataVo;
import com.vgroyalchemist.vos.OrderFiltersVo;
import com.vgroyalchemist.vos.OrderFlterMenu;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.TimeFilterVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class GetOrderFilterApplyService {

    public JSONObject generateRequestJSON(String mStringUserId ,String mStringOrderid , String mStringTimeId) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        OrderFilterApplyInfo orderFilterApplyInfo = new OrderFilterApplyInfo(mStringUserId ,mStringOrderid ,mStringTimeId);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(orderFilterApplyInfo);
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

    public ServerResponseVO OrderFilterApply(Context context, String url, String mStringUserId ,String mStringOrderid , String mStringTimeId) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserId ,mStringOrderid ,mStringTimeId);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT  order filter apply url....." + url);
            Utility.debugger("VT  order filter apply request....." + requestJSON);
            Utility.debugger("VT  order filter apply response.... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

                OrderDataVo orderDataVo = new OrderDataVo();
                ArrayList<OrderDataVo> orderDataVos = new ArrayList<OrderDataVo>();

                try {
                    JSONObject responseData = responseJSON.getJSONObject("details");
                    JSONArray responseDataJSONArray = responseData.getJSONArray("detailary");

                    for (int A = 0; A < responseDataJSONArray.length(); A++) {
                        orderDataVo = new OrderDataVo();

                        JSONObject jsonArrayJSONObject = responseDataJSONArray.getJSONObject(A);

                        try {
                            orderDataVo.setOrderId(jsonArrayJSONObject.getString("OrderId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            orderDataVo.setTotalQty(jsonArrayJSONObject.getString("TotalQty"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            orderDataVo.setOrderStatus(jsonArrayJSONObject.getString("OrderStatus"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            orderDataVo.setUserName(jsonArrayJSONObject.getString("UserName"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

//                        try {
//                            orderDataVo.setUserId(jsonArrayJSONObject.getString("UserId"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        try {
                            orderDataVo.setTotalRs(jsonArrayJSONObject.getString("TotalRs"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            orderDataVo.setDate(jsonArrayJSONObject.getString("Date"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            orderDataVo.setOrderNo(jsonArrayJSONObject.getString("OrderNo"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            orderDataVo.setGrossRs(jsonArrayJSONObject.getString("GrossRs"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            orderDataVo.setPaymentstatus(jsonArrayJSONObject.getString("PaymentStatus"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        orderDataVos.add(orderDataVo);
                    }
                    serverResponseVO.setData(orderDataVos);
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