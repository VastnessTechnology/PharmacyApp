package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class EditAddressInfo extends ParentRequestVO {


    String mStringUserId = "";
    String mStringPincode="";
    String mStringHouseNumber="";
    String mStringContactNumber="";
    String mStringName="";
    String mStringSelectLocation="";
    String mStringAddressId="";
    String mStringSwitchValue;
    String mStringStateID;
    String mStringCityId;
    String AddressType;

    public EditAddressInfo(String mStringUserId, String mStringAddressId, String mStringPincode , String mStringHouseNumber, String mStringName,
                           String mStringContactNumber , String mStringSelectLocation,String mStringSwitchValue,String mStringStateID,String mStringCityId) {
        this.mStringUserId = mStringUserId;
        this.mStringAddressId=mStringAddressId;
        this.mStringPincode=mStringPincode;
        this.mStringHouseNumber=mStringHouseNumber;
        this.mStringContactNumber=mStringContactNumber;
        this.mStringName=mStringName;
        this.mStringSelectLocation=mStringSelectLocation;
        this.mStringCityId =mStringCityId;
        this.mStringSwitchValue=mStringSwitchValue;
        this.mStringStateID=mStringStateID;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("AddressId",mStringAddressId);
            dataJSONTemp.put("Pincode",mStringPincode);
            dataJSONTemp.put("Address",mStringHouseNumber);
            dataJSONTemp.put("ContactNo",mStringContactNumber);
            dataJSONTemp.put("Name",mStringName);
            dataJSONTemp.put("AddressType",mStringSelectLocation);
            dataJSONTemp.put("DefaultAddress",mStringSwitchValue);
            dataJSONTemp.put("StateId",mStringStateID);
            dataJSONTemp.put("CityId",mStringCityId);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
