package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.CategoryMedicineListInfo;
import com.vgroyalchemist.requestobjects.SearchMedicineInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.MedicineCategoryListVO;
import com.vgroyalchemist.vos.MedicineSearchList;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


public class CategoryMedicineListInfoService {

    public JSONObject generateRequestJSON(String mStringategoryid ,String UserId,String DeviceToken) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        CategoryMedicineListInfo categoryMedicineListInfo = new CategoryMedicineListInfo(mStringategoryid ,UserId ,DeviceToken);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(categoryMedicineListInfo);
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

    public ServerResponseVO ListInfomation(Context context, String url, String mStringategoryid,String UserId,String DeviceToken) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringategoryid ,UserId ,DeviceToken);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);

            Utility.debugger("VT  Medicine category....." + url);
            Utility.debugger("VT category medicine......" + requestJSON);
            Utility.debugger("VT responseJSON  category Medicine..... " + responseJSON);
            serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
            serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

            MedicineCategoryListVO  medicineCategoryListVO = new MedicineCategoryListVO();
            ArrayList<MedicineCategoryListVO> medicineCategoryListVOS = new ArrayList<MedicineCategoryListVO>();


            try {
                JSONObject responseData = responseJSON.getJSONObject("details");
                JSONArray jsonArray = responseData.getJSONArray("detailary");
                for (int s = 0; s < jsonArray.length(); s++) {
                    medicineCategoryListVO = new MedicineCategoryListVO();

                    JSONObject mJsonObject = jsonArray.getJSONObject(s);

                    try {
                        medicineCategoryListVO.setMrp(mJsonObject.getString("Mrp"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineCategoryListVO.setDiscount(mJsonObject.getString("Discount"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineCategoryListVO.setPrice(mJsonObject.getString("Price"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineCategoryListVO.setUnitsPerPack(mJsonObject.getString("UnitsPerPack"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineCategoryListVO.setMedicineName(mJsonObject.getString("MedicineName"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineCategoryListVO.setMedicineId(mJsonObject.getString("MedicineId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineCategoryListVO.setImagePath(mJsonObject.getString("ImagePath"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineCategoryListVO.setStockAvailablity(mJsonObject.getString("StockAvailablity"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineCategoryListVO.setAvgReview(mJsonObject.getInt("AvgReview"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        medicineCategoryListVO.setCartQty(mJsonObject.getString("CartQty"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        medicineCategoryListVO.setDefaultImage(mJsonObject.getString("DefaultImage"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        medicineCategoryListVO.setTotalRating(mJsonObject.getString("TotalRating"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        medicineCategoryListVO.setTotalReview(mJsonObject.getString("TotalReview"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineCategoryListVO.setMedicineDescription(mJsonObject.getString("MedicineDescription"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineCategoryListVO.setManufacturer(mJsonObject.getString("Manufacturer"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    medicineCategoryListVOS.add(medicineCategoryListVO);
                }
                serverResponseVO.setData(medicineCategoryListVOS);
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

            responseObject = HttpConnector.getJSONObject(url,requestObject);
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