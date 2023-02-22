package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.AddAddressInfo;
import com.vgroyalchemist.requestobjects.EditAddressInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;

 
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


public class EditAddressInfoService {

    public JSONObject generateRequestJSON(String mStringUserId,String mStringAddressId,String mStringPincode ,String mStringHouseNumber,String mStringName,
                                          String mStringContactNumber ,String mStringSelectLocation,String mStringSwitchValue,String mStringStateID,String mStringCityId) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        EditAddressInfo mEditAddressInfo = new EditAddressInfo(mStringUserId ,mStringAddressId ,mStringPincode ,mStringHouseNumber ,mStringName,mStringContactNumber ,
                mStringSelectLocation ,mStringSwitchValue,mStringStateID,mStringCityId );
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

    public ServerResponseVO fetcheditaddress(Context context, String url, String mStringUserId,String mStringAddressId ,String mStringPincode ,String mStringHouseNumber,String mStringName, String mStringContactNumber
            ,String mStringSelectLocation,String mStringSwitchValue,String mStringStateID,String mStringCityId) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserId ,mStringAddressId ,mStringPincode,mStringHouseNumber,mStringName,mStringContactNumber,
                    mStringSelectLocation,mStringSwitchValue,mStringStateID,mStringCityId );

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Edit Address....." + url);
            Utility.debugger("VT responseJSON  Edit Address..... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));
                if (serverResponseVO.getStatus().equalsIgnoreCase("true")){
                    serverResponseVO.setAddressId(responseJSON.getJSONObject("details").getString("AddressId"));
                }

                serverResponseVO.setData(responseJSON);
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
            Utility.debugger("VT Edit Address......" + requestObject.toString());
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