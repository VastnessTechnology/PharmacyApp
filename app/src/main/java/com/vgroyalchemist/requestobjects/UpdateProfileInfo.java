package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class UpdateProfileInfo extends ParentRequestVO {


    String mStringUserId = "";
    String FirstName="";
    String LastName="";
    String ContactNumber="";
    String EmailAddress="";

    public UpdateProfileInfo(String mStringUserId,String FirstName ,String LastName,String ContactNumber, String EmailAddress) {
        this.mStringUserId = mStringUserId;
        this.FirstName=FirstName;
        this.LastName=LastName;
        this.ContactNumber=ContactNumber;
        this.EmailAddress=EmailAddress;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("FirstName",FirstName);
            dataJSONTemp.put("LastName",LastName);
            dataJSONTemp.put("ContactNumber",ContactNumber);
            dataJSONTemp.put("EmailAddress",EmailAddress);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
