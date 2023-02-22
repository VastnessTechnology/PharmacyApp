package com.vgroyalchemist.vos;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderDataVo implements Parcelable {

    String OrderId;
    String UserId;
    String Date;
    String TotalQty;
    String TotalRs;
    String OrderStatus;
    String UserName;
    String OrderNo;
    String GrossRs;
    String paymentstatus;



    public OrderDataVo(Parcel in) {
        OrderId = in.readString();
        UserId = in.readString();
        Date = in.readString();
        TotalQty = in.readString();
        TotalRs = in.readString();
        OrderStatus = in.readString();
        UserName = in.readString();
        OrderNo = in.readString();
        GrossRs = in.readString();
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public static final Creator<OrderDataVo> CREATOR = new Creator<OrderDataVo>() {
        @Override
        public OrderDataVo createFromParcel(Parcel in) {
            return new OrderDataVo(in);
        }

        @Override
        public OrderDataVo[] newArray(int size) {
            return new OrderDataVo[size];
        }
    };

    public OrderDataVo() {

    }

    public String getGrossRs() {
        return GrossRs;
    }

    public void setGrossRs(String grossRs) {
        GrossRs = grossRs;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public void setTotalQty(String totalQty) {
        TotalQty = totalQty;
    }

    public String getTotalQty() {
        return TotalQty;
    }

    public void setTotalRs(String totalRs) {
        TotalRs = totalRs;
    }

    public String getTotalRs() {
        return TotalRs;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserName() {
        return UserName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OrderId);
        dest.writeString(UserId);
        dest.writeString(Date);
        dest.writeString(TotalQty);
        dest.writeString(TotalRs);
        dest.writeString(OrderStatus);
        dest.writeString(UserName);
        dest.writeString(GrossRs);
    }
}
