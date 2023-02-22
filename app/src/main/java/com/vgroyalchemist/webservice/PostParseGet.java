package com.vgroyalchemist.webservice;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.params.BasicHttpParams;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.params.HttpConnectionParams;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.params.HttpParams;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.vos.GetLoginData;


import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.json.JSONObject;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;


@SuppressWarnings("deprecation")
public class PostParseGet {

    public Context mContext;
    public boolean isNetError = false;
    public boolean isOtherError = false;
    List<NameValuePair> nameValuePairs;

    ConnectivityManager mConnectivityManager;
    NetworkInfo mNetworkInfo;

    Gson mGson;

    public PostParseGet(Context context) {
        mContext = context;
        mGson = new Gson();
    }

    /**
     * @param url
     * @param nameValuePairs
     * @param mObject
     * @return
     */
    public Object postHttpURL(String url, List<NameValuePair> nameValuePairs, Object mObject) {
        HttpPost httppost;
        HttpParams httpParameters;
        int timeoutConnection = 50000;
        HttpClient httpclient = null;
        HttpResponse response = null;
        String data = "";

        isOtherError = false;

        Object mFillObject = null;

        if (check_Internet()) {
            try {
                mFillObject = mObject.getClass().newInstance();

                url = URLDecoder.decode(url, "UTF-8");
                url = url.replaceAll(" ", "%20");

                httppost = new HttpPost(url);
                httpParameters = (HttpParams) new BasicHttpParams();
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutConnection);
                httpclient = new DefaultHttpClient(httpParameters);

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                if (nameValuePairs != null) {
                    for (int index = 0; index < nameValuePairs.size(); index++) {
                        String paramName = nameValuePairs.get(index).getName();
                        String paramValue = nameValuePairs.get(index).getValue();

                        if (paramName.equalsIgnoreCase("pic")) {
                            entity.addPart(paramName, (ContentBody) new FileBody(new File(paramValue)));
                        } else {
                            entity.addPart(nameValuePairs.get(index).getName(), (ContentBody) new StringBody(nameValuePairs.get(index).getValue()));
                        }
                    }
                    httppost.setEntity((HttpEntity) entity);
                }

//				System.out.println("VT url..."+url.toString());

                // Execute HTTP Post Request
                response = httpclient.execute(httppost);
                data = EntityUtils.toString(response.getEntity());

//				System.out.println("VT result Data...." + data);

                mFillObject = mGson.fromJson(data, mFillObject.getClass());

            } catch (Exception e) {

                isOtherError = true;
                e.printStackTrace();
            }
        }
        return mFillObject;
    }

    /**
     * Function
     * Takes and keeps a reference of the passed context in order to Check whether Internet is available or not.
     *
     * @return The return will be true if the Internet is available and false if not.
     */
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

    public Object GetSignupData(Object object,String mStringFirstName, String mStringLastName, String mStringPhoneNumber,String mStringEmail, String mStringPassword, String mStringReferalCode,
                                String mStringDeviceToken, String mStringDeviceType) {
        nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("FirstName", mStringFirstName));
        nameValuePairs.add(new BasicNameValuePair("LastName", mStringLastName));
        nameValuePairs.add(new BasicNameValuePair("MobileNo", mStringPhoneNumber));
        nameValuePairs.add(new BasicNameValuePair("EmailId", mStringEmail));
        nameValuePairs.add(new BasicNameValuePair("Password", mStringPassword));
        nameValuePairs.add(new BasicNameValuePair("ReferralCode", mStringReferalCode));
        nameValuePairs.add(new BasicNameValuePair("DeviceToken", mStringDeviceToken));
        nameValuePairs.add(new BasicNameValuePair("DeviceType", mStringDeviceType));

        return postHttpURL(Tags.Signup, nameValuePairs, object);
    }

    public void setUserDataObjs(Activity mActivity, Object mGetLoginData) {

        SharedPreferences.Editor editor = mActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_MYID, Context.MODE_PRIVATE).edit();
        editor.putString(Tags.INTENT_TAG_MYID_OBJECT, mGetLoginData.toString());
        editor.commit();
    }

    public GetLoginData getUserDataObj(Context mActivity) {
        SharedPreferences prefs = mActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_MYID, Context.MODE_PRIVATE);
        String restoredText = prefs.getString(Tags.INTENT_TAG_MYID_OBJECT, "");
//		System.out.println("VT restored....."+restoredText);
        GetLoginData mGetMyID = new Gson().fromJson(restoredText, GetLoginData.class);

        if (restoredText.equalsIgnoreCase(""))
            mGetMyID = new GetLoginData();

        return mGetMyID;
    }


    public void setUserDataObj(Context mActivity, GetLoginData mGetLoginData) {

        String Json = new Gson().toJson(mGetLoginData);

        SharedPreferences.Editor editor = mActivity.getSharedPreferences(Tags.TAG_SHARED_PREFERENCE_MYID, Context.MODE_PRIVATE).edit();
        editor.putString(Tags.INTENT_TAG_MYID_OBJECT, Json);
        editor.commit();
    }



}
