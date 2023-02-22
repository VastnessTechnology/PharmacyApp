package com.vgroyalchemist.webservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.vgroyalchemist.requestobjects.GetZoneCityInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.GetCityData;

 
import org.json.JSONObject;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;

public class CityService {

    public static final String PARAM_REGION_ID = "region_id";
    public static final String PARAM_CODE = "code";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_DETAILS = "detail";

    ConnectivityManager mConnectivityManager;
    NetworkInfo mNetworkInfo;
    public boolean isNetError = false;
    public boolean isOtherError = false;
    Context mContext;

    Gson mGson;

    public GetCityData fetchCityData(Context context, String countryCode) {

        this.mContext = context;
        GetCityData getOrderDetailsData = null;
        GetZoneCityInfo myOrdersInfo = new GetZoneCityInfo(countryCode);
        RequestFactory requestFactory = new RequestFactory();
        JSONObject requestJSON = null;
        try {
            requestJSON = requestFactory.getFinalRequestObject(myOrdersInfo);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        String url =  Tags.getCityData;
        Utility.debugger("VT city url......" + url);
        Utility.debugger("VT city request......" + requestJSON);
        getOrderDetailsData = new GetCityData();
        getOrderDetailsData = (GetCityData) sendUrl(url, requestJSON, getOrderDetailsData);
        Utility.debugger("VT city response......" + getOrderDetailsData.toString());
        return getOrderDetailsData;
    }

    public Object sendUrl(String url, JSONObject requestObject, Object mObject) {
        isOtherError = false;


        Object mFillObject = null;

        if (check_Internet()) {
            try {
                mGson = new Gson();
                JSONObject responseObject = HttpConnector.getJSONObject(url, requestObject);
                mFillObject = mGson.fromJson(responseObject.toString(), mObject.getClass());
//                mFillObject = mGson.fromJson(AllFinal, mObject.getClass());

            } catch (Exception e) {
                isOtherError = true;
                e.printStackTrace();
            }
        }
        return mFillObject;
    }

    public boolean check_Internet() {
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (mNetworkInfo != null && mNetworkInfo.isConnectedOrConnecting()) {
            isNetError = false;
            return true;
        } else {
            isNetError = true;
            return false;
        }
    }
}
