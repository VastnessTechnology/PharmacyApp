package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.AddAddressInfo;
import com.vgroyalchemist.requestobjects.GetAllCartInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.AllCartData;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.TaxChargeVO;
import com.vgroyalchemist.vos.TotalArrayVo;
import com.vgroyalchemist.vos.UserDetailsVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class GetAllCartService {

    public JSONObject generateRequestJSON(String mStringUserId ,String mStringAddressId ,String CoupenCode ,String DeviceToken) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetAllCartInfo getAllCartInfo = new GetAllCartInfo(mStringUserId ,mStringAddressId,CoupenCode ,DeviceToken);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(getAllCartInfo);
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

    public ServerResponseVO getAllCart(Context context, String url, String mStringUserId ,String mStringAddressId,String CoupenCode ,String DeviceToken) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserId ,mStringAddressId ,CoupenCode ,DeviceToken);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Get All Cart url....." + url);
            Utility.debugger("VT Get All Cart request....." + requestJSON);
            Utility.debugger("VT Get All Cart.... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

                TaxChargeVO  mTaxChargeVO = new TaxChargeVO();
                ArrayList<AllCartData> allCartDataArrayList = new ArrayList<AllCartData>();

                try {
                    JSONObject responseData = responseJSON.getJSONObject("details");
                    JSONArray jsonArray = responseData.getJSONArray("detailary");


                    for (int A = 0; A < jsonArray.length(); A++) {
                        AllCartData    allCartData = new AllCartData();

                        JSONObject jsonArrayJSONObject = jsonArray.getJSONObject(A);

                        try {
                            allCartData.setCartId(jsonArrayJSONObject.getString("CartId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            allCartData.setAmount(jsonArrayJSONObject.getString("Amount"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            allCartData.setPrice(jsonArrayJSONObject.getString("Price"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            allCartData.setQty(jsonArrayJSONObject.getString("Qty"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            allCartData.setMedicineId(jsonArrayJSONObject.getString("MedicineId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            allCartData.setMedicineImgPath(jsonArrayJSONObject.getString("MedicineImgPath"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            allCartData.setMedicineName(jsonArrayJSONObject.getString("MedicineName"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            allCartData.setContent(jsonArrayJSONObject.getString("Content"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        allCartDataArrayList.add(allCartData);
                    }

                    mTaxChargeVO.setmAllCartData(allCartDataArrayList);

                    JSONObject mJsonObjectTotal = responseData.getJSONObject("TotalDetailAry");

                    TotalArrayVo mTotalArrayVo = new TotalArrayVo();
                    ArrayList<TotalArrayVo> mTotalArrayVos = new ArrayList<TotalArrayVo>();

                    try {
                        mTotalArrayVo.setDiscRs(mJsonObjectTotal.getString("DiscRs"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        mTotalArrayVo.setNetRs(mJsonObjectTotal.getString("NetRs"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        mTotalArrayVo.setShippingCharge(mJsonObjectTotal.getString("ShippingCharge"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        mTotalArrayVo.setSubTotalRs(mJsonObjectTotal.getString("SubTotalRs"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        mTotalArrayVo.setTotallRs(mJsonObjectTotal.getString("TotallRs"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        mTotalArrayVo.setCoupenRs(mJsonObjectTotal.getString("CoupenRs"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    mTaxChargeVO.setmTotalArrayVo(mTotalArrayVo);


                    serverResponseVO.setData(mTaxChargeVO);

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