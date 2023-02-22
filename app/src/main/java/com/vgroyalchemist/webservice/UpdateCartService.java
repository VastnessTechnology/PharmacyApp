package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetAllCartInfo;
import com.vgroyalchemist.requestobjects.UpdateCartInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.AllCartData;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class UpdateCartService {

    public JSONObject generateRequestJSON(String mStringUserId ,String mStringCartId , String mStringMedicineId ,String mStringQty ,String mStringPrice , String DeviceToken) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        UpdateCartInfo updateCartInfo = new UpdateCartInfo(mStringUserId ,mStringCartId,mStringMedicineId ,mStringQty,mStringPrice ,DeviceToken);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(updateCartInfo);
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

    public ServerResponseVO UpdateCartData(Context context, String url, String mStringUserId ,String mStringCartId , String mStringMedicineId ,String mStringQty ,String mStringPrice,String  DeviceToken) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserId ,mStringCartId,mStringMedicineId ,mStringQty,mStringPrice,DeviceToken);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT update cart....." + url);
            Utility.debugger("VT update cart request....." + requestJSON);
            Utility.debugger("VT update Cart.... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

                serverResponseVO.setData(responseJSON);

//                AllCartData allCartData = new AllCartData();
//                ArrayList<AllCartData> allCartDataArrayList = new ArrayList<AllCartData>();
//
//                try {
//                    JSONObject responseData = responseJSON.getJSONObject("details");
//                    JSONArray jsonArray = responseData.getJSONArray("detailary");
//
//
//                    for (int A = 0; A < jsonArray.length(); A++) {
//                        allCartData = new AllCartData();
//
//                        JSONObject jsonArrayJSONObject = jsonArray.getJSONObject(A);
//
//                        try {
//                            allCartData.setCartId(jsonArrayJSONObject.getString("CartId"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            allCartData.setAmount(jsonArrayJSONObject.getString("Amount"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            allCartData.setPrice(jsonArrayJSONObject.getString("Price"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            allCartData.setQty(jsonArrayJSONObject.getString("Qty"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            allCartData.setMedicineId(jsonArrayJSONObject.getString("MedicineId"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//
//                        allCartDataArrayList.add(allCartData);
//                    }
//
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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