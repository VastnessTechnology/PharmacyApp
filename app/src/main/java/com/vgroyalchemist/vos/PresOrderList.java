package com.vgroyalchemist.vos;

public class PresOrderList {

    String OrderId;
    String SrNo;
    String Date;
    String OrderStatus;
    String UserName;
    String ImageName;
    String PrescriptionId;

    public String getPrescriptionId() {
        return PrescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        PrescriptionId = prescriptionId;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getOrderId() {
        return OrderId;
    }

    public String getSrNo() {
        return SrNo;
    }

    public String getDate() {
        return Date;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public String getUserName() {
        return UserName;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public void setSrNo(String srNo) {
        SrNo = srNo;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}

