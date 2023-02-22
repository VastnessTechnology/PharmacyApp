package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetAllComplainMedicineInfo;
import com.vgroyalchemist.requestobjects.GetAllOrderInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.ComplainMedicineListVO;
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


public class GetAllComplainMedicineService {

    public JSONObject generateRequestJSON(String mStringUserId) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetAllComplainMedicineInfo getAllComplainMedicineInfo = new GetAllComplainMedicineInfo(mStringUserId);
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

    public ServerResponseVO getallMedicineList(Context context, String url, String mStringUserId) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserId);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT get complain medicine list url....." + url);
            Utility.debugger("VT get complain medicine list request....." + requestJSON);
            Utility.debugger("VT get complain medicine list.... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

                ComplainMedicineListVO complainMedicineListVO = new ComplainMedicineListVO();
                ArrayList<ComplainMedicineListVO> medicineListVOArrayList = new ArrayList<ComplainMedicineListVO>();

                try {
                    JSONObject responseData = responseJSON.getJSONObject("details");
                    JSONArray responseDataJSONArray = responseData.getJSONArray("detailary");

                    for (int A = 0; A < responseDataJSONArray.length(); A++) {
                        complainMedicineListVO = new ComplainMedicineListVO();

                        JSONObject jsonArrayJSONObject = responseDataJSONArray.getJSONObject(A);

                        try {
                            complainMedicineListVO.setMedicineId(jsonArrayJSONObject.getString("MedicineId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            complainMedicineListVO.setMedicineName(jsonArrayJSONObject.getString("MedicineName"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            complainMedicineListVO.setStatus(jsonArrayJSONObject.getString("status"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            complainMedicineListVO.setOrderId(jsonArrayJSONObject.getString("OrderId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            complainMedicineListVO.setAddedDate(jsonArrayJSONObject.getString("AddedDate"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            complainMedicineListVO.setComplainId(jsonArrayJSONObject.getString("ID"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            complainMedicineListVO.setOrderNo(jsonArrayJSONObject.getString("OrderNo"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        medicineListVOArrayList.add(complainMedicineListVO);
                    }

                    serverResponseVO.setData(medicineListVOArrayList);
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