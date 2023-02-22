package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetAllAddressInfo;
import com.vgroyalchemist.requestobjects.SearchMedicineInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.CategoryList;
import com.vgroyalchemist.vos.MedicineSearchList;
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

public class SearchMedicineInfoService {

    public JSONObject generateRequestJSON(String mStringSearch,String mStringUserid ,String DeviceToken) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        SearchMedicineInfo searchMedicineInfo = new SearchMedicineInfo(mStringSearch,mStringUserid , DeviceToken);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(searchMedicineInfo);
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

    public ServerResponseVO fetchLoginUserInformation(Context context, String url, String mStringSearch ,String mStringUserid , String DeviceToken) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringSearch,mStringUserid,DeviceToken);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Search Medicine....." + url);
            Utility.debugger("VT responseJSON  Search Medicine..... " + responseJSON);
            serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
            serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

            MedicineSearchList medicineSearchList = new MedicineSearchList();
            ArrayList<MedicineSearchList> medicineSearchLists = new ArrayList<MedicineSearchList>();


            try {
                JSONObject responseData = responseJSON.getJSONObject("details");
                JSONArray jsonArray = responseData.getJSONArray("detailary");
                for (int s = 0; s < jsonArray.length(); s++) {
                    medicineSearchList = new MedicineSearchList();

                    JSONObject mJsonObject = jsonArray.getJSONObject(s);

                    try {
                        medicineSearchList.setManufacturer(mJsonObject.getString("Manufacturer"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        medicineSearchList.setMrp(mJsonObject.getString("Mrp"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineSearchList.setDiscount(mJsonObject.getString("Discount"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineSearchList.setPrice(mJsonObject.getString("Price"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineSearchList.setUnitsPerPack(mJsonObject.getString("UnitsPerPack"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineSearchList.setMedicineName(mJsonObject.getString("MedicineName"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineSearchList.setMedicineId(mJsonObject.getString("MedicineId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineSearchList.setImagePath(mJsonObject.getString("ImagePath"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineSearchList.setImagePath(mJsonObject.getString("ImagePath"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineSearchList.setStockAvailablity(mJsonObject.getString("StockAvailablity"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineSearchList.setMedicineDescription(mJsonObject.getString("MedicineDescription"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineSearchList.setAvgReview(mJsonObject.getInt("AvgReview"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineSearchList.setTotalRating(mJsonObject.getString("TotalRating"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineSearchList.setTotalReview(mJsonObject.getString("TotalReview"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        medicineSearchList.setCartQty(mJsonObject.getString("Cartqty"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    medicineSearchLists.add(medicineSearchList);
                }
                serverResponseVO.setData(medicineSearchLists);
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
            Utility.debugger("VT Search Medicine......" + requestObject.toString());
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