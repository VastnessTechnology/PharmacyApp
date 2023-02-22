package com.vgroyalchemist.requestobjects;


import org.json.JSONObject;

public class AddCartInfo extends ParentRequestVO {


    String mStringCartid;
    String mStringUserid;
    String mStringPrice;
    String mStringQty;
    String mStringMedicinId;
    String DeviceToken;

    public AddCartInfo(String mStringcartid ,String mStringUserid,String mStringPrice, String mStringQty, String mStringMedicinId , String DeviceToken) {

        this.mStringCartid =mStringcartid;
        this.mStringUserid =mStringUserid;
        this.mStringPrice =mStringPrice;
        this.mStringQty=mStringQty;
        this.mStringMedicinId=mStringMedicinId;
        this.DeviceToken= DeviceToken;
    }

    @Override
    public JSONObject generateRequest() {

        JSONObject dataJSONTemp = new JSONObject();
        try {
//            dataJSONTemp.put("CartId", mStringCartid);
            dataJSONTemp.put("UserId",mStringUserid);
            dataJSONTemp.put("Price", mStringPrice);
            dataJSONTemp.put("Qty",mStringQty);
            dataJSONTemp.put("MedicineId",mStringMedicinId);
            dataJSONTemp.put("devicetoken",DeviceToken);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataJSONTemp;
    }
}
