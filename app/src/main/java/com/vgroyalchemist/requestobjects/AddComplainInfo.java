package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class AddComplainInfo extends ParentRequestVO {


    String mStringMedicineId="";
    String mStringUserId ="";
    String mStringOrderId ="";
    String mStringComplainMsg ="";
    String Status="";
    String ComplainId="";
    String ComplainFromOrder="";


    public AddComplainInfo(String mStringMedicineId, String mStringUserId ,String mStringOrderId,String mStringComplainMsg ,String Status ,String ComplainId, String ComplainFromOrder) {
        this.mStringMedicineId=mStringMedicineId;
        this.mStringUserId =mStringUserId;
        this.mStringOrderId=mStringOrderId;
        this.mStringComplainMsg=mStringComplainMsg;
        this.Status = Status;
        this.ComplainId= ComplainId;
        this.ComplainFromOrder=ComplainFromOrder;

    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("MedicineId",mStringMedicineId);
            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("OrderId",mStringOrderId);
            dataJSONTemp.put("Complain",mStringComplainMsg);
            dataJSONTemp.put("Status",Status);
            dataJSONTemp.put("ComplainId",ComplainId);
            dataJSONTemp.put("ComplainFromOrder",ComplainFromOrder);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
