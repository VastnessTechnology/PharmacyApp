package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetAllAddressInfo;
import com.vgroyalchemist.requestobjects.GetAllNotificationInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.NotificationVO;
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


public class GetNotificationService {

    public JSONObject generateRequestJSON(String UserId) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetAllNotificationInfo getAllNotificationInfo = new GetAllNotificationInfo(UserId);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(getAllNotificationInfo);
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

    public ServerResponseVO GetNotification(Context context, String url ,String UserId) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {

                    JSONObject requestJSON = generateRequestJSON(UserId);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Get notification....." + url);
            Utility.debugger("VT responseJSON  Get notification..... " + responseJSON);
            serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
            serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

            NotificationVO notificationVO = new NotificationVO();
            ArrayList<NotificationVO> notificationVOS = new ArrayList<NotificationVO>();

            try {
                JSONObject responseData = responseJSON.getJSONObject("details");
                JSONArray productDetailJSONArray = responseData.getJSONArray("detailary");


                for (int A = 0; A < productDetailJSONArray.length(); A++) {
                    notificationVO = new NotificationVO();

                    JSONObject productDetailJSON = productDetailJSONArray.getJSONObject(A);
                    try {
                        notificationVO.setNotiId(productDetailJSON.getString("NotiId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        notificationVO.setMessage(productDetailJSON.getString("Message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        notificationVO.setTitle(productDetailJSON.getString("Title"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        notificationVO.setAddedDate(productDetailJSON.getString("AddedDate"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        notificationVO.setIsRead(productDetailJSON.getString("IsRead"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    notificationVOS.add(notificationVO);
                }
                serverResponseVO.setData(notificationVOS);
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
            Utility.debugger("VT Get notification......" + requestObject.toString());
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