package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class CheckMobileOtpInfo extends ParentRequestVO {


    String mStringMobileNO = "";
    String mStringotp= "";
    String mStringUserid= "";



    public CheckMobileOtpInfo(String mStringMobileNO, String mStringotp ,String mStringUserid) {
        this.mStringMobileNO = mStringMobileNO;
        this.mStringotp = mStringotp;
        this.mStringUserid = mStringUserid;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("MobileNo",mStringMobileNO);
            dataJSONTemp.put("OTP",mStringotp);
            dataJSONTemp.put("UserId",mStringUserid);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
