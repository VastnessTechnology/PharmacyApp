package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetAllReviewInfo;
import com.vgroyalchemist.requestobjects.GetAltMedicineInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.AltMedicineVO;
import com.vgroyalchemist.vos.GetAllReviewsVO;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class GetAltMedicineService {

    public JSONObject generateRequestJSON(String mStringMedicineId ,String UserId , String DeviceToken) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetAltMedicineInfo getAltMedicineInfo = new GetAltMedicineInfo(mStringMedicineId ,UserId ,DeviceToken);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(getAltMedicineInfo);
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

    public ServerResponseVO getAltMedicine(Context context, String url, String mStringMedicineId ,String UserId, String DeviceToken) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringMedicineId ,UserId ,DeviceToken);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT get find substituet url....." + url);
            Utility.debugger("VT get find substituet request....." + requestJSON);
            Utility.debugger("VT get find substituet.... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

                AltMedicineVO altMedicineVO = new AltMedicineVO();
                ArrayList<AltMedicineVO> altMedicineVOS = new ArrayList<AltMedicineVO>();

                try {
//                    JSONObject responseData = responseJSON.getJSONObject("details");
                    JSONArray detailary = responseJSON.getJSONArray("details");

                    for (int A = 0; A < detailary.length(); A++) {
                        altMedicineVO = new AltMedicineVO();

                        JSONObject jsonArrayJSONObject = detailary.getJSONObject(A);

                        try {
                            altMedicineVO.setMedicineId(jsonArrayJSONObject.getString("MedicineId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            altMedicineVO.setAltMedicineId(jsonArrayJSONObject.getString("AltMedicineId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            altMedicineVO.setMedicineName(jsonArrayJSONObject.getString("MedicineName"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            altMedicineVO.setManufacturer(jsonArrayJSONObject.getString("Manufacturer"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            altMedicineVO.setPrice(jsonArrayJSONObject.getString("Price"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            altMedicineVO.setMedicineImgPath(jsonArrayJSONObject.getString("MedicineImgPath"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            altMedicineVO.setStockAvailablity(jsonArrayJSONObject.getString("StockAvailablity"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            altMedicineVO.setAvgReview(jsonArrayJSONObject.getInt("AvgReview"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            altMedicineVO.setCartQty(jsonArrayJSONObject.getString("CartQty"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            altMedicineVO.setDefaultImage(jsonArrayJSONObject.getString("DefaultImage"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            altMedicineVO.setTotalRating(jsonArrayJSONObject.getString("TotalRating"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            altMedicineVO.setTotalReview(jsonArrayJSONObject.getString("TotalReview"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            altMedicineVO.setMedicineDescription(jsonArrayJSONObject.getString("MedicineDescription"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        altMedicineVOS.add(altMedicineVO);
                    }
                    serverResponseVO.setData(altMedicineVOS);
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