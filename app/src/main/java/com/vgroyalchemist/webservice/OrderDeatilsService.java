package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.OrderDetailsInfo;
import com.vgroyalchemist.requestobjects.ReOrderInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.AltMedicineVO;
import com.vgroyalchemist.vos.OrderDetailsVO;
import com.vgroyalchemist.vos.OrderItemListVO;
import com.vgroyalchemist.vos.ProductDetailVO;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class OrderDeatilsService {

    public JSONObject generateRequestJSON(String orderid) {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        OrderDetailsInfo mReOrderInfo = new OrderDetailsInfo(orderid);
        try {
            requestObject = mRequestFactory.getFinalRequestObject(mReOrderInfo);
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

    public ServerResponseVO Orderdetails(Context context, String url, String orderid) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON(orderid);

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT order details ....." + url);
            Utility.debugger("VT order details request....." + requestJSON);
            Utility.debugger("VT responseJSON  details..... " + responseJSON);
            if (responseJSON != null) {
                serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
                serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));


                OrderDetailsVO orderDetailsVO = new OrderDetailsVO();

                try {
                    JSONObject responseData = responseJSON.getJSONObject("details");

                    try {
                        orderDetailsVO.setAddress(responseData.optString("Address"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setCity(responseData.optString("City"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setCoupenDisc(responseData.optString("CoupenDisc"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setDate(responseData.optString("Date"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setDeliveryCharge(responseData.optString("DeliveryCharge"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setExpectedDelvDate(responseData.optString("ExpectedDelvDate"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setGrossRs(responseData.optString("GrossRs"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setOrderId(responseData.optString("OrderId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setOrderStatus(responseData.optString("OrderStatus"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        orderDetailsVO.setPincode(responseData.optString("Pincode"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setSrNo(responseData.optString("SrNo"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setTrackLink(responseData.optString("TrackLink"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setTotalQty(responseData.optString("TotalQty"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setTotalRs(responseData.optString("TotalRs"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setUserName(responseData.optString("UserName"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setState(responseData.optString("State"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setDeliveryDate(responseData.optString("DeliveryDate"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setOrderNo(responseData.optString("OrderNo"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        orderDetailsVO.setInvoicePdfLink(responseData.optString("InvoicePdfLink"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        orderDetailsVO.setIsInvoiceCreated(responseData.optString("IsInvoiceCreated"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        orderDetailsVO.setItemTotalDisc(responseData.optString("ItemTotalDisc"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        orderDetailsVO.setTotalMRPRs(responseData.optString("TotalMRPRs"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        orderDetailsVO.setDelvType(responseData.optString("delvType"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONArray mJsonArrayOrderItem = responseData.getJSONArray("childdata");
                    ArrayList<OrderItemListVO> altMedicineVOS = new ArrayList<OrderItemListVO>();
                    if (mJsonArrayOrderItem.length() != 0) {
                        for (int i = 0; i < mJsonArrayOrderItem.length(); i++) {

                            OrderItemListVO orderItemListVO = new OrderItemListVO();

                            orderItemListVO.setMedicineName(mJsonArrayOrderItem.getJSONObject(i).optString("MedicineName"));
                            orderItemListVO.setMedicineId(mJsonArrayOrderItem.getJSONObject(i).optString("MedicineId"));
                            orderItemListVO.setQty(mJsonArrayOrderItem.getJSONObject(i).optString("Qty"));
                            orderItemListVO.setPrice(mJsonArrayOrderItem.getJSONObject(i).optString("Price"));
                            orderItemListVO.setMedicineImgPath(mJsonArrayOrderItem.getJSONObject(i).optString("MedicineImgPath"));

                            altMedicineVOS.add(orderItemListVO);
                        }
                        orderDetailsVO.setmOrderItemListVOS(altMedicineVOS);
                    }

                    serverResponseVO.setData(orderDetailsVO);

                }
                catch (Exception e) {
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