package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class GetAllCartInfo extends ParentRequestVO {


    String mStringUserId = "";
    String mStringAddressId ="";
    String CoupenCode="";
    String DeviceToken;

    public GetAllCartInfo(String mStringUserId ,String mStringAddressId ,String CoupenCode, String DeviceToken) {
        this.mStringUserId = mStringUserId;
        this.mStringAddressId= mStringAddressId;
        this.CoupenCode= CoupenCode;
        this.DeviceToken= DeviceToken;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("Usrid",mStringUserId);
            dataJSONTemp.put("AddressId",mStringAddressId);
            dataJSONTemp.put("CoupenCode",CoupenCode);
            dataJSONTemp.put("devicetoken",DeviceToken);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
