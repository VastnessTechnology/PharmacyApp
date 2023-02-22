package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.AddAddressInfo;
import com.vgroyalchemist.requestobjects.GetAllAddressInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.CategoryList;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.UserDetailsVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class GetAllAddressInfoService {

    public JSONObject generateRequestJSON(String mStringUserId) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetAllAddressInfo mGetAllAddressInfo = new GetAllAddressInfo(mStringUserId);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(mGetAllAddressInfo);
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

    public ServerResponseVO fetchLoginUserInformation(Context context, String url, String mStringUserId) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {

            JSONObject requestJSON = generateRequestJSON(mStringUserId);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Get All Address....." + url);
            Utility.debugger("VT responseJSON  Get All Address..... " + responseJSON);
            serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
            serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

            UserDetailsVO addressVO = new UserDetailsVO();
            ArrayList<UserDetailsVO> addressVOArrayList = new ArrayList<UserDetailsVO>();

            try {
                JSONObject responseData = responseJSON.getJSONObject("details");
                JSONArray productDetailJSONArray = responseData.getJSONArray("detailary");


                for (int A = 0; A < productDetailJSONArray.length(); A++) {
                    addressVO = new UserDetailsVO();

                    JSONObject productDetailJSON = productDetailJSONArray.getJSONObject(A);
                    try {
                        addressVO.setAddressId(productDetailJSON.getString("AddressId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        addressVO.setAddress(productDetailJSON.getString("Address"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        addressVO.setPincode(productDetailJSON.getString("Pincode"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        addressVO.setName(productDetailJSON.getString("Name"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        addressVO.setContactNo(productDetailJSON.getString("ContactNo"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        addressVO.setAddressType(productDetailJSON.getString("AddressType"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        addressVO.setCityId(productDetailJSON.getString("CityId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        addressVO.setDefaultAddress(productDetailJSON.getString("DefaultAddress"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        addressVO.setStateId(productDetailJSON.getString("StateId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        addressVO.setStateName(productDetailJSON.getString("StateName"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        addressVO.setCityName(productDetailJSON.getString("CityName"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    addressVOArrayList.add(addressVO);
                }
                serverResponseVO.setData(addressVOArrayList);
            }

            catch (Exception e) {
                e.printStackTrace();
                addressVO = new UserDetailsVO();
                addressVO.setStatus(false);
                addressVO.setMsg("exception");
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
            Utility.debugger("VT Get All Address......" + requestObject.toString());
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