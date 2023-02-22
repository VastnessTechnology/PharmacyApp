package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.OrderDetailsInfo;
import com.vgroyalchemist.requestobjects.ScheduleDetailsInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.OrderDetailsVO;
import com.vgroyalchemist.vos.OrderItemListVO;
import com.vgroyalchemist.vos.ReminderScheduleDetailsVO;
import com.vgroyalchemist.vos.ScheduleDetailsVO;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class ScheduleDeatilsService {

    public JSONObject generateRequestJSON(String mStringScheduleId) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        ScheduleDetailsInfo scheduleDetailsInfo = new ScheduleDetailsInfo(mStringScheduleId);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(scheduleDetailsInfo);
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

    public ServerResponseVO Scheduleetails(Context context, String url, String mStringScheduleId) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringScheduleId);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Schdeule details ....." + url);
            Utility.debugger("VT Schdeule details request....." + requestJSON);
            Utility.debugger("VT responseJSON Schdeule details..... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));


                ScheduleDetailsVO scheduleDetailsVO = new ScheduleDetailsVO();

                try {
                    JSONObject responseData = responseJSON.getJSONObject("details");

                    try {
                        scheduleDetailsVO.setStartDate(responseData.getString("StartDate"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        scheduleDetailsVO.setEndDate(responseData.getString("EndDate"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        scheduleDetailsVO.setSchedulePattener(responseData.getString("SchedulePattener"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        scheduleDetailsVO.setMedicineName(responseData.getString("MedicineName"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    JSONArray jsonArray = responseData.getJSONArray("childdata");
                    ArrayList<ReminderScheduleDetailsVO> reminderScheduleDetailsVOS = new ArrayList<ReminderScheduleDetailsVO>();
                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            ReminderScheduleDetailsVO detailsVO = new ReminderScheduleDetailsVO();

                            detailsVO.setDateday(jsonArray.getJSONObject(i).getString("dateday"));
                            detailsVO.setDosage(jsonArray.getJSONObject(i).getString("dosage"));
                            detailsVO.setTime(jsonArray.getJSONObject(i).getString("Time"));
                            detailsVO.setReminderDateTime(jsonArray.getJSONObject(i).getString("ReminderDateTime"));


                            reminderScheduleDetailsVOS.add(detailsVO);
                        }
                        scheduleDetailsVO.setmReminderScheduleDetailsVOS(reminderScheduleDetailsVOS);
                    }

                    serverResponseVO.setData(scheduleDetailsVO);
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