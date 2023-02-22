package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetAllOrderInfo;
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


public class GetOrderFilterMenuService {

    public JSONObject generateRequestJSON(String mStringUserId) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetAllOrderInfo mGetAllOrderInfo = new GetAllOrderInfo(mStringUserId );
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

    public ServerResponseVO getAllOrder(Context context, String url, String mStringUserId) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserId);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT get order filter list url....." + url);
            Utility.debugger("VT get order filter list request....." + requestJSON);
            Utility.debugger("VT get order filter list response.... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

                OrderFlterMenu orderFlterMenu = new OrderFlterMenu();
                ArrayList<OrderFlterMenu> orderFlterMenus = new ArrayList<OrderFlterMenu>();
                OrderFiltersVo mOrderFiltersVo = new  OrderFiltersVo();


                try {
                    JSONObject responseData = responseJSON.getJSONObject("details");
                    JSONArray responseDataJSONArray = responseData.getJSONArray("orderFilter");

                    for (int A = 0; A < responseDataJSONArray.length(); A++) {
                        orderFlterMenu = new OrderFlterMenu();

                        JSONObject jsonArrayJSONObject = responseDataJSONArray.getJSONObject(A);

                        try {
                            orderFlterMenu.setId(jsonArrayJSONObject.getString("Id"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            orderFlterMenu.setName(jsonArrayJSONObject.getString("Name"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        orderFlterMenus.add(orderFlterMenu);
                    }
                    mOrderFiltersVo.setOrderFlterMenus(orderFlterMenus);


                    TimeFilterVO timeFilterVO = new TimeFilterVO();
                    ArrayList<TimeFilterVO> timeFilterVOS = new ArrayList<TimeFilterVO>();
                    JSONArray jsonArray = responseData.getJSONArray("timeFilter");

                    for (int A = 0; A < jsonArray.length(); A++) {

                        timeFilterVO = new TimeFilterVO();
                        JSONObject jsonObject = jsonArray.getJSONObject(A);

                        try {
                            timeFilterVO.setId(jsonObject.getString("Id"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            timeFilterVO.setName(jsonObject.getString("Name"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        timeFilterVOS.add(timeFilterVO);
                    }
                    mOrderFiltersVo.setmTimeFilterVOS(timeFilterVOS);

                    serverResponseVO.setData(mOrderFiltersVo);
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