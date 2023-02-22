package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.BookMarkArticlesListInfo;
import com.vgroyalchemist.requestobjects.GetAllArticlesListInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.AllArticlesListVo;
import com.vgroyalchemist.vos.ServerResponseVO;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


public class BookMarkArticlesListInfoService {

    public JSONObject generateRequestJSON(String mStringUserid , String Devicetoken) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        BookMarkArticlesListInfo searchMedicineInfo = new BookMarkArticlesListInfo(mStringUserid ,Devicetoken);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(searchMedicineInfo);
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

    public ServerResponseVO BookmarkAll(Context context, String url ,String mStringUserid , String Devicetoken) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringUserid,Devicetoken);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT Articles book mark list....." + url);
            Utility.debugger("VT Articles bookmark list......" + requestJSON);
            Utility.debugger("VT responseJSON  Articles bookmark list..... " + responseJSON);
            serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
            serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

            AllArticlesListVo allArticlesListVo = new AllArticlesListVo();
            ArrayList<AllArticlesListVo> listVos = new ArrayList<AllArticlesListVo>();


            try {
                JSONObject responseData = responseJSON.getJSONObject("details");
                JSONArray jsonArray = responseData.getJSONArray("detailary");
                for (int s = 0; s < jsonArray.length(); s++) {
                    allArticlesListVo = new AllArticlesListVo();

                    JSONObject mJsonObject = jsonArray.getJSONObject(s);

                    try {
                        allArticlesListVo.setBookMark(mJsonObject.getString("BookMark"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        allArticlesListVo.setId(mJsonObject.getString("Id"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    try {
//                        allArticlesListVo.setImageName(mJsonObject.getString("ImageName"));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    try {
                        allArticlesListVo.setShortDesc(mJsonObject.getString("ShortDesc"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        allArticlesListVo.setLongDesc(mJsonObject.getString("LongDesc"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        allArticlesListVo.setImageName(mJsonObject.getString("ImagePath"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    listVos.add(allArticlesListVo);
                }
                serverResponseVO.setData(listVos);
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