package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class UpdateCartInfo extends ParentRequestVO {


    String mStringUserId = "";
    String mStringCartId = "";
    String mStringMedicineId = "";
    String mStringQty = "";
    String mStringPrice = "";
    String DeviceToken= "";


    public UpdateCartInfo(String mStringUserId,String mStringCartId , String mStringMedicineId ,String mStringQty ,String mStringPrice, String DeviceToken) {
        this.mStringUserId = mStringUserId;
        this.mStringCartId = mStringCartId;
        this.mStringMedicineId = mStringMedicineId;
        this.mStringQty = mStringQty;
        this.mStringPrice = mStringPrice;
        this.DeviceToken= DeviceToken;


    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
            dataJSONTemp.put("UserId",mStringUserId);
            dataJSONTemp.put("CartId",mStringCartId);
            dataJSONTemp.put("MedicineId",mStringMedicineId);
            dataJSONTemp.put("Qty",mStringQty);
            dataJSONTemp.put("Price",mStringPrice);
            dataJSONTemp.put("devicetoken",DeviceToken);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
