package com.vgroyalchemist.webservice;

import android.content.Context;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;
import com.vgroyalchemist.requestobjects.MedicineListInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.CategoryList;
import com.vgroyalchemist.vos.GetAllBannerMenuVO;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


public class GetAllBannerMenuInfoService {

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
            Utility.debugger("VT Banner menu List....." + url);
            Utility.debugger("VT Banner menu Request....." + requestJSON);
            Utility.debugger("VT responseJSON  Banner menu List..... " + responseJSON);
            serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
            serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

            GetAllBannerMenuVO getAllBannerMenuVO = new GetAllBannerMenuVO();
            ArrayList<GetAllBannerMenuVO> bannerMenuVOS = new ArrayList<GetAllBannerMenuVO>();

            try {
                JSONObject responseData = responseJSON.getJSONObject("details");
                JSONArray jsonArray = responseData.getJSONArray("detailary");

                for (int m = 0; m < jsonArray.length(); m++) {
                    getAllBannerMenuVO = new GetAllBannerMenuVO();

                    JSONObject mJsonObject = jsonArray.getJSONObject(m);

                    try {
                        getAllBannerMenuVO.setMenuId(mJsonObject.getString("MenuId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        getAllBannerMenuVO.setMenuName(mJsonObject.getString("MenuName"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bannerMenuVOS.add(getAllBannerMenuVO);
                }
                serverResponseVO.setData(bannerMenuVOS);
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