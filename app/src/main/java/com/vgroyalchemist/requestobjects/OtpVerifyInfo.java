package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class OtpVerifyInfo extends ParentRequestVO {


    String mStringEmail = "";
    String mStringotp= "";
    String MobileNo="";


    public OtpVerifyInfo(String mStringEmail,String MobileNo, String mStringotp) {
        this.mStringEmail = mStringEmail;
        this.mStringotp = mStringotp;
        this.MobileNo = MobileNo;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("EmailId",mStringEmail);
            dataJSONTemp.put("OTP",mStringotp);
            dataJSONTemp.put("MobileNo",MobileNo);
            dataJSONTemp.put("userid","");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
