package com.vgroyalchemist.vos;

import java.util.ArrayList;

public class OrderDetailsVO {

    String OrderId;
    String SrNo;
    String Date;
    String GrossRs;
    String TotalRs;
    String DeliveryCharge;
    String CoupenDisc;
    String  TotalQty;
    String OrderStatus;
    String UserName;
    String Address;
    String Pincode;
    String City;
    String State;
    String ExpectedDelvDate;
    String TrackLink;
    ArrayList<OrderItemListVO> mOrderItemListVOS;
    String DeliveryDate;
    String OrderNo;
    String InvoicePdfLink;
    String IsInvoiceCreated;
    String ItemTotalDisc;
    String TotalMRPRs;
    String delvType;


    public String getDelvType() {
        return delvType;
    }

    public void setDelvType(String delvType) {
        this.delvType = delvType;
    }

    public String getTotalMRPRs() {
        return TotalMRPRs;
    }

    public void setTotalMRPRs(String totalMRPRs) {
        TotalMRPRs = totalMRPRs;
    }

    public String getItemTotalDisc() {
        return ItemTotalDisc;
    }

    public void setItemTotalDisc(String itemTotalDisc) {
        ItemTotalDisc = itemTotalDisc;
    }

    public String getIsInvoiceCreated() {
        return IsInvoiceCreated;
    }

    public void setIsInvoiceCreated(String isInvoiceCreated) {
        IsInvoiceCreated = isInvoiceCreated;
    }

    public String getInvoicePdfLink() {
        return InvoicePdfLink;
    }

    public void setInvoicePdfLink(String invoicePdfLink) {
        InvoicePdfLink = invoicePdfLink;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public ArrayList<OrderItemListVO> getmOrderItemListVOS() {
        return mOrderItemListVOS;
    }

    public void setmOrderItemListVOS(ArrayList<OrderItemListVO> mOrderItemListVOS) {
        this.mOrderItemListVOS = mOrderItemListVOS;
    }

    public String getTotalRs() {
        return TotalRs;
    }

    public void setTotalRs(String totalRs) {
        TotalRs = totalRs;
    }

    public String getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(String totalQty) {
        TotalQty = totalQty;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCoupenDisc() {
        return CoupenDisc;
    }

    public void setCoupenDisc(String coupenDisc) {
        CoupenDisc = coupenDisc;
    }

    public String getDeliveryCharge() {
        return DeliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        DeliveryCharge = deliveryCharge;
    }

    public String getExpectedDelvDate() {
        return ExpectedDelvDate;
    }

    public void setExpectedDelvDate(String expectedDelvDate) {
        ExpectedDelvDate = expectedDelvDate;
    }

    public String getGrossRs() {
        return GrossRs;
    }

    public void setGrossRs(String grossRs) {
        GrossRs = grossRs;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public String getSrNo() {
        return SrNo;
    }

    public void setSrNo(String srNo) {
        SrNo = srNo;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getTrackLink() {
        return TrackLink;
    }

    public void setTrackLink(String trackLink) {
        TrackLink = trackLink;
    }

}
