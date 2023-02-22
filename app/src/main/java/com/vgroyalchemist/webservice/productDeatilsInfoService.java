package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetAllArticlesListInfo;
import com.vgroyalchemist.requestobjects.ProductDetailsInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.AllArticlesListVo;
import com.vgroyalchemist.vos.AltMedicineVO;
import com.vgroyalchemist.vos.MedicineImagevo;
import com.vgroyalchemist.vos.ProductDetailVO;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;

public class productDeatilsInfoService {

    public JSONObject generateRequestJSON(String mStringmedicineId , String mStringUserid ,String DeviceToken) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        ProductDetailsInfo productDetailsInfo = new ProductDetailsInfo(mStringmedicineId ,mStringUserid,DeviceToken);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(productDetailsInfo);
            try {
                mObject = requestObject.getJSONObject("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        return mObject;
    }

    public ServerResponseVO getproductdetails(Context context, String url, String mStringmedicineId,String mStringUserid, String DeviceToken) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringmedicineId ,mStringUserid ,DeviceToken);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT product deatils ....." + url);
            Utility.debugger("VT product deatils ......" + requestJSON);
            Utility.debugger("VT responseJSON  product deatils ..... " + responseJSON);
            serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
            serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));


            ProductDetailVO productDetailVO = new ProductDetailVO();

            try {
                JSONObject responseData = responseJSON.getJSONObject("details");

                try {
                    productDetailVO.setMedicineId(responseData.getString("MedicineId"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setAddedBy(responseData.getString("AddedBy"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setMedicineName(responseData.getString("MedicineName"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setBatchNo(responseData.getString("BatchNo"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setAddedDate(responseData.getString("AddedDate"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setAlcohol(responseData.getString("Alcohol"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setBreastfeeding(responseData.getString("Breastfeeding"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setCategoryId(responseData.getString("CategoryId"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setMrp(responseData.getString("Mrp"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setDiscount(responseData.getString("Discount"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setPrice(responseData.getString("Price"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setDescription(responseData.getString("Description"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setDriving(responseData.getString("Driving"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setUnitsPerPack(responseData.getString("UnitsPerPack"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setUses(responseData.getString("Uses"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setPregnancy(responseData.getString("Pregnancy"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setUseMedicine(responseData.getString("UseMedicine"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setMedicineWorks(responseData.getString("MedicineWorks"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setKidney(responseData.getString("Kidney"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setLiver(responseData.getString("Liver"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setSideEffects(responseData.getString("SideEffects"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setManufacturer(responseData.getString("Manufacturer"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setStorage(responseData.getString("Storage"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    productDetailVO.setMedicineContent(responseData.getString("MedicineContent"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setMedicineWorks(responseData.getString("MedicineWorks"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    productDetailVO.setUseMedicine(responseData.getString("UseMedicine"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    productDetailVO.setQuickTips(responseData.getString("QuickTips"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    productDetailVO.setMissedDosage(responseData.getString("MissedDosage"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    productDetailVO.setMediCartCount(responseData.getString("MediCartCount"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setTotalCartCount(responseData.getString("TotalCartCount"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setAvgReview(responseData.getString("AvgReview"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setTotalRating(responseData.getString("TotalRating"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setTotalReview(responseData.getString("TotalReview"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setPrescriptionReq(responseData.getString("PrescriptionReq"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setStockAvailablity(responseData.getString("StockAvailablity"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setMedicineDescription(responseData.getString("MedicineDescription"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    productDetailVO.setCartqty(responseData.getString("Cartqty"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JSONArray mJsonArray = responseData.getJSONArray("mstMedicineImages");

                ArrayList<MedicineImagevo> medicineImagevos = new ArrayList<MedicineImagevo>();
                if (mJsonArray.length() != 0) {
                    for (int i = 0; i < mJsonArray.length(); i++) {

                        MedicineImagevo medicineImagevo = new MedicineImagevo();
                        medicineImagevo.setImagePath( mJsonArray.getJSONObject(i).getString("ImagePath"));

                        medicineImagevos.add(medicineImagevo);

                    }
                    productDetailVO.setMedicineImagevos(medicineImagevos);
                }

                JSONArray mJsonArrayAltMedicine = responseData.getJSONArray("mstAltMedicine");

                ArrayList<AltMedicineVO> altMedicineVOS = new ArrayList<AltMedicineVO>();
                if (mJsonArrayAltMedicine.length() != 0) {
                    for (int i = 0; i < mJsonArrayAltMedicine.length(); i++) {

                        AltMedicineVO altMedicineVO = new AltMedicineVO();

                        altMedicineVO.setMedicineName( mJsonArrayAltMedicine.getJSONObject(i).getString("MedicineName"));
                        altMedicineVO.setMedicineId( mJsonArrayAltMedicine.getJSONObject(i).getString("MedicineId"));
                        altMedicineVO.setManufacturer( mJsonArrayAltMedicine.getJSONObject(i).getString("Manufacturer"));
                        altMedicineVO.setPrice( mJsonArrayAltMedicine.getJSONObject(i).getString("Price"));
                        altMedicineVO.setMedicineImgPath( mJsonArrayAltMedicine.getJSONObject(i).getString("MedicineImgPath"));
                        altMedicineVO.setAltMedicineId(mJsonArrayAltMedicine.getJSONObject(i).getString("AltMedicineId"));
                        altMedicineVOS.add(altMedicineVO);

                    }
                    productDetailVO.setAltMedicineVOArrayList(altMedicineVOS);
                }


            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            serverResponseVO.setData(productDetailVO);


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