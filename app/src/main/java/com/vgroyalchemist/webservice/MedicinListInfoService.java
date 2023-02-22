package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.MedicineListInfo;
import com.vgroyalchemist.requestobjects.SearchMedicineInfo;
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

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;

public class MedicinListInfoService {

    public JSONObject generateRequestJSON() {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        MedicineListInfo listInfo = new MedicineListInfo();
        try {
            requestObject = mRequestFactory.getFinalRequestObject(listInfo);
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

    public ServerResponseVO fetchLoginUserInformation(Context context, String url) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON();

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Medicine categgory List....." + url);
            Utility.debugger("VT Medicine List Request....." + requestJSON);
            Utility.debugger("VT responseJSON  Medicine List..... " + responseJSON);
            serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
            serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

            CategoryList mCategoryList = new CategoryList();
            ArrayList<CategoryList> mCategoryLists = new ArrayList<CategoryList>();

            try {
                JSONObject responseData = responseJSON.getJSONObject("details");
                JSONArray jsonArray = responseData.getJSONArray("detailary");

                for (int m = 0; m < jsonArray.length(); m++) {
                    mCategoryList = new CategoryList();

                    JSONObject mJsonObject = jsonArray.getJSONObject(m);

                    try {
                        mCategoryList.setMedicineCategoryName(mJsonObject.optString("MedicineCategoryName"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        mCategoryList.setMedicineCategoryId(mJsonObject.optString("MedicineCategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        mCategoryList.setImagePath(mJsonObject.optString("ImagePath"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mCategoryLists.add(mCategoryList);
                }
                serverResponseVO.setData(mCategoryLists);
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