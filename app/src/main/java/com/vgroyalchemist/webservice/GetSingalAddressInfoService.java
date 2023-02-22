package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.EditAddressInfo;
import com.vgroyalchemist.requestobjects.GetSingalAddressInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.vgroyalchemist.vos.UserDetailsVO;

 
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class GetSingalAddressInfoService {

    public JSONObject generateRequestJSON(String Addressid) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetSingalAddressInfo mEditAddressInfo = new GetSingalAddressInfo(Addressid );
        try {
            requestObject = mRequestFactory.getFinalRequestObject(mEditAddressInfo);
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

    public ServerResponseVO fetcheditaddress(Context context, String url, String Addressid) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(Addressid);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT get Address....." + url);
            Utility.debugger("VT responseJSON  get Address..... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

                JSONObject mJsonObject = responseJSON.getJSONObject("details");
                UserDetailsVO mUserDetailsVO= new UserDetailsVO();
                ArrayList<UserDetailsVO> mUserDetailsVOS= new ArrayList<UserDetailsVO>();
                mUserDetailsVO.setAddressId(mJsonObject.optString("AddressId"));
                mUserDetailsVO.setStateName(mJsonObject.optString("StateName"));
                mUserDetailsVO.setName(mJsonObject.optString("Name"));
                mUserDetailsVO.setStateId(mJsonObject.optString("StateId"));
                mUserDetailsVO.setCityId(mJsonObject.optString("CityId"));
                mUserDetailsVO.setCityName(mJsonObject.optString("CityName"));
                mUserDetailsVO.setDefaultAddress(mJsonObject.optString("DefaultAddress"));
                mUserDetailsVO.setAddressType(mJsonObject.optString("AddressType"));
                mUserDetailsVO.setContactNo(mJsonObject.optString("ContactNo"));
                mUserDetailsVO.setPincode(mJsonObject.optString("Pincode"));
                mUserDetailsVO.setAddress(mJsonObject.optString("Address"));


                mUserDetailsVOS.add(mUserDetailsVO);

                serverResponseVO.setData(mUserDetailsVOS);
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
            Utility.debugger("VT get Address......" + requestObject.toString());
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