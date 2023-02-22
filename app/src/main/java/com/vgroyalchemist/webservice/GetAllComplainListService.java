package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetAllComplainListInfo;
import com.vgroyalchemist.requestobjects.GetAllComplainMedicineInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.ComplainChatListVO;
import com.vgroyalchemist.vos.ComplainMedicineListVO;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class GetAllComplainListService {

    public JSONObject generateRequestJSON(String mStringUserId ,String mStringMedicineId ,String ComplainId) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetAllComplainListInfo getAllComplainMedicineInfo = new GetAllComplainListInfo(mStringUserId ,mStringMedicineId,ComplainId);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(getAllComplainMedicineInfo);
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

    public ServerResponseVO getallcomplainList(Context context, String url, String mStringUserId ,String mStringMedicineId,String ComplainId) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserId ,mStringMedicineId,ComplainId);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT get complain  list url....." + url);
            Utility.debugger("VT get complain  list request....." + requestJSON);
            Utility.debugger("VT get complain  list.... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

                ComplainChatListVO complainChatListVO = new ComplainChatListVO();
                ArrayList<ComplainChatListVO> voArrayList = new ArrayList<ComplainChatListVO>();

                try {
                    JSONObject responseData = responseJSON.getJSONObject("details");
                    JSONArray responseDataJSONArray = responseData.getJSONArray("detailary");

                    for (int A = 0; A < responseDataJSONArray.length(); A++) {
                        complainChatListVO = new ComplainChatListVO();

                        JSONObject jsonArrayJSONObject = responseDataJSONArray.getJSONObject(A);

                        try {
                            complainChatListVO.setAddedDate(jsonArrayJSONObject.getString("AddedDate"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            complainChatListVO.setAddFrom(jsonArrayJSONObject.getString("AddFrom"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            complainChatListVO.setComplain(jsonArrayJSONObject.getString("Complain"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            complainChatListVO.setDatestr(jsonArrayJSONObject.getString("Datestr"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        voArrayList.add(complainChatListVO);
                    }

                    serverResponseVO.setData(voArrayList);
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