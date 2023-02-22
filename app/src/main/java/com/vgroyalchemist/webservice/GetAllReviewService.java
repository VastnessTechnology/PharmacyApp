package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.GetAllOrderInfo;
import com.vgroyalchemist.requestobjects.GetAllReviewInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.GetAllReviewsVO;
import com.vgroyalchemist.vos.OrderDataVo;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class GetAllReviewService {

    public JSONObject generateRequestJSON(String mStringMedicineId) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetAllReviewInfo mGetAllOrderInfo = new GetAllReviewInfo(mStringMedicineId );
        try {
            requestObject = mRequestFactory.getFinalRequestObject(mGetAllOrderInfo);
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

    public ServerResponseVO getAllreviews(Context context, String url, String mStringMedicineId) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(mStringMedicineId);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT get all reviews url....." + url);
            Utility.debugger("VT get all reviews request....." + requestJSON);
            Utility.debugger("VT get all reviews.... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

                GetAllReviewsVO getAllReviewsVO = new GetAllReviewsVO();
                ArrayList<GetAllReviewsVO> mGetAllReviewsVOS = new ArrayList<GetAllReviewsVO>();

                try {
                    JSONObject responseData = responseJSON.getJSONObject("details");
                    JSONArray responseDataJSONArray = responseData.getJSONArray("detailary");

                    for (int A = 0; A < responseDataJSONArray.length(); A++) {
                        getAllReviewsVO = new GetAllReviewsVO();

                        JSONObject jsonArrayJSONObject = responseDataJSONArray.getJSONObject(A);

                        try {
                            getAllReviewsVO.setReview(jsonArrayJSONObject.getString("Review"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            getAllReviewsVO.setReviewDate(jsonArrayJSONObject.getString("ReviewDate"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            getAllReviewsVO.setReviewText(jsonArrayJSONObject.getString("ReviewText"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            getAllReviewsVO.setUserName(jsonArrayJSONObject.getString("UserName"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        mGetAllReviewsVOS.add(getAllReviewsVO);
                    }

                    serverResponseVO.setData(mGetAllReviewsVOS);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


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