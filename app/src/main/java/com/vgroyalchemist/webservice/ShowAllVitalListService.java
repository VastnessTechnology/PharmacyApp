package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.ShowAllVitalListInfo;
import com.vgroyalchemist.requestobjects.VitalListInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.ShowAllVitalListVO;
import com.vgroyalchemist.vos.VitalListVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class ShowAllVitalListService {

    public JSONObject generateRequestJSON(String mStringUserid,String mStringVitalId) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        ShowAllVitalListInfo listInfo = new ShowAllVitalListInfo(mStringUserid,mStringVitalId);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(listInfo);
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

    public ServerResponseVO vitalList(Context context, String url ,String mStringUserid,String mStringVitalId) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserid,mStringVitalId);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT show all Vital List....." + url);
            Utility.debugger("VT show all vital Request....." + requestJSON);
            Utility.debugger("VT responseJSON show all vital List..... " + responseJSON);
            serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
            serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

            ShowAllVitalListVO showAllVitalListVO = new ShowAllVitalListVO();
            ArrayList<ShowAllVitalListVO> showAllVitalListVOArrayList = new ArrayList<ShowAllVitalListVO>();

            try {
                JSONObject responseData = responseJSON.getJSONObject("details");
                JSONArray jsonArray = responseData.getJSONArray("detailary");

                for (int m = 0; m < jsonArray.length(); m++) {
                    showAllVitalListVO = new ShowAllVitalListVO();

                    JSONObject mJsonObject = jsonArray.getJSONObject(m);

                    try {
                        showAllVitalListVO.setUnit(mJsonObject.getString("Unit"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        showAllVitalListVO.setVital(mJsonObject.getString("Vital"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        showAllVitalListVO.setVitalID(mJsonObject.getString("VitalID"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        showAllVitalListVO.setValue(mJsonObject.getString("Value"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        showAllVitalListVO.setVitalDate(mJsonObject.getString("VitalDate"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        showAllVitalListVO.setVitalTime(mJsonObject.getString("VitalTime"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        showAllVitalListVO.setId(mJsonObject.getString("Id"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    showAllVitalListVOArrayList.add(showAllVitalListVO);
                }
                serverResponseVO.setData(showAllVitalListVOArrayList);
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