package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetAllAddressInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
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


public class GetDefualtAddressInfoService {

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

    public ServerResponseVO GetDefualtAddress(Context context, String url, String mStringUserId) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {

            JSONObject requestJSON = generateRequestJSON(mStringUserId);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Get default Address....." + url);
            Utility.debugger("VT Get default Address....." + requestJSON);
            Utility.debugger("VT responseJSON  Get default Address..... " + responseJSON);
            serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
            serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

            JSONObject mJsonObject = responseJSON.getJSONObject("details");
            UserDetailsVO mUserDetailsVO= new UserDetailsVO();
            ArrayList<UserDetailsVO> mUserDetailsVOS= new ArrayList<UserDetailsVO>();
            mUserDetailsVO.setAddressId(mJsonObject.getString("AddressId"));
            mUserDetailsVO.setStateName(mJsonObject.getString("StateName"));
            mUserDetailsVO.setName(mJsonObject.getString("Name"));
            mUserDetailsVO.setStateId(mJsonObject.getString("StateId"));
            mUserDetailsVO.setCityId(mJsonObject.getString("CityId"));
            mUserDetailsVO.setCityName(mJsonObject.getString("CityName"));
            mUserDetailsVO.setDefaultAddress(mJsonObject.getString("DefaultAddress"));
            mUserDetailsVO.setAddressType(mJsonObject.getString("AddressType"));
            mUserDetailsVO.setContactNo(mJsonObject.getString("ContactNo"));
            mUserDetailsVO.setPincode(mJsonObject.getString("Pincode"));
            mUserDetailsVO.setAddress(mJsonObject.getString("Address"));


            mUserDetailsVOS.add(mUserDetailsVO);
            serverResponseVO.setData(mUserDetailsVOS);

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
            Utility.debugger("VT Get Defualt Address......" + requestObject.toString());
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