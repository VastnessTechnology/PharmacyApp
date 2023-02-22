package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetActiveScheduleInfo;
import com.vgroyalchemist.requestobjects.GetAllCartInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.AllCartData;
import com.vgroyalchemist.vos.ScheduleActiveVO;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.TaxChargeVO;
import com.vgroyalchemist.vos.TotalArrayVo;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class GetActiveScheduleService {

    public JSONObject generateRequestJSON(String mStringUserId ,String currentDate) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetActiveScheduleInfo getActiveScheduleInfo = new GetActiveScheduleInfo(mStringUserId ,currentDate);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(getActiveScheduleInfo);
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

    public ServerResponseVO getAllSchedule(Context context, String url, String mStringUserId,String currentDate) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserId ,currentDate);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Get Active Schedule url....." + url);
            Utility.debugger("VT Get Active Schedule request....." + requestJSON);
            Utility.debugger("VT Get Active Schedule.... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));


                ScheduleActiveVO scheduleActiveVO = new ScheduleActiveVO();
                ArrayList<ScheduleActiveVO> scheduleActiveVOArrayList = new ArrayList<ScheduleActiveVO>();
//
                try {
                    JSONObject responseData = responseJSON.getJSONObject("details");
                    JSONArray jsonArray = responseData.getJSONArray("detailary");


                    for (int A = 0; A < jsonArray.length(); A++) {
                        scheduleActiveVO  = new ScheduleActiveVO();

                        JSONObject jsonArrayJSONObject = jsonArray.getJSONObject(A);

                        try {
                            scheduleActiveVO.setScheduleReminderId(jsonArrayJSONObject.getString("ScheduleReminderId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            scheduleActiveVO.setScheduleId(jsonArrayJSONObject.getString("ScheduleId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            scheduleActiveVO.setAction(jsonArrayJSONObject.getString("action"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            scheduleActiveVO.setDateday(jsonArrayJSONObject.getString("dateday"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            scheduleActiveVO.setReminderTime(jsonArrayJSONObject.getString("ReminderTime"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            scheduleActiveVO.setMedicineId(jsonArrayJSONObject.getString("MedicineId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            scheduleActiveVO.setMedicineName(jsonArrayJSONObject.getString("MedicineName"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            scheduleActiveVO.setTime(jsonArrayJSONObject.getString("Time"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            scheduleActiveVO.setDosage(jsonArrayJSONObject.getString("dosage"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        scheduleActiveVOArrayList.add(scheduleActiveVO);
                    }

                    serverResponseVO.setData(scheduleActiveVOArrayList);

                } catch (Exception e) {
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