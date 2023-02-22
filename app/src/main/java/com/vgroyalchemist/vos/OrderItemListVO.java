package com.vgroyalchemist.vos;

public class OrderItemListVO {


    String OrderId;
    String Date;
    String TotalRs;
    String OrderStatus;
    String UserName;
    String MedicineId;
    String Qty;
    String Price;
    String Amount;
    String MedicineName;
    String MedicineImgPath;

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

    public String getTotalRs() {
        return TotalRs;
    }

    public void setTotalRs(String totalRs) {
        TotalRs = totalRs;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMedicineImgPath() {
        return MedicineImgPath;
    }

    public void setMedicineImgPath(String medicineImgPath) {
        MedicineImgPath = medicineImgPath;
    }

    public String getMedicineId() {
        return MedicineId;
    }

    public void setMedicineId(String medicineId) {
        MedicineId = medicineId;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

}
