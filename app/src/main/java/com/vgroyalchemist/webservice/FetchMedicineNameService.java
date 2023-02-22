package com.vgroyalchemist.webservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.vgroyalchemist.requestobjects.GetMedicinenameInfo;
import com.vgroyalchemist.requestobjects.GetStateInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.GetStateData;
import com.vgroyalchemist.vos.MedicineNameVo;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;

public class FetchMedicineNameService {


    public boolean isNetError = false;
    public boolean isOtherError = false;


    public JSONObject generateRequestJSON() {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        GetMedicinenameInfo getMedicinenameInfo = new GetMedicinenameInfo();
        try {
            requestObject = mRequestFactory.getFinalRequestObject(getMedicinenameInfo);
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
    public ServerResponseVO fetchMedicineData(Context context , String url) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try{
            JSONObject requestJSON = generateRequestJSON();
            Utility.debugger("VT medicine name URL...." + url);
            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT medicine name responseJSON...."+responseJSON);
            if(responseJSON != null){
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));

                MedicineNameVo medicineNameVo = new MedicineNameVo ();
                ArrayList<MedicineNameVo> medicineNameVos = new ArrayList<MedicineNameVo>();

                try {
                    JSONArray responseData = responseJSON.getJSONArray("details");
//                    JSONArray productDetailJSONArray = responseData.getJSONArray("detailary");

                    for (int A = 0; A < responseData.length(); A++) {
                        medicineNameVo = new MedicineNameVo();

                        JSONObject productDetailJSON = responseData.getJSONObject(A);

                        try {
                            medicineNameVo.setMedicineId(productDetailJSON.getString("MedicineId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            medicineNameVo.setMedicineName(productDetailJSON.getString("MedicineName"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        medicineNameVos.add(medicineNameVo);
                    }
                    serverResponseVO.setData(medicineNameVos);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return serverResponseVO;

    }

    public JSONObject sendRequest(String url, JSONObject requestObject, Context context)throws
            Exception{
        JSONObject responseObject = null;
        ArrayList<Long> list = new ArrayList<Long>(0);
        try {
            Utility.debugger("VT medicine name...."+requestObject.toString());
            responseObject = HttpConnector.getJSONObject(url, requestObject);
        }catch(MalformedURLException mue){
            Utility.debugger("MalformedURLException");
            mue.printStackTrace();
            throw mue;
        }catch(SocketTimeoutException ste){
            Utility.debugger("SocketTimeoutException");
            ste.printStackTrace();
            throw ste;
        }catch (IOException ioe) {
            Utility.debugger("IOException");
            ioe.printStackTrace();
            throw ioe;
        }catch(JSONException je){
            Utility.debugger("JSONException");
            je.printStackTrace();
            throw je;
        }catch(Exception e){
            Utility.debugger("Exception");
            e.printStackTrace();
            throw e;
        }finally {
            //return isError;
        }
        return responseObject;
    }
}