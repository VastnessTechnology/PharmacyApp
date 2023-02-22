package com.vgroyalchemist.vos;

public class ComplainMedicineListVO {

    String MedicineId;
    String MedicineName;
    String status;
    String OrderId;
    String AddedDate;
    String ComplainId;
    String OrderNo;


    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getComplainId() {
        return ComplainId;
    }

    public void setComplainId(String complainId) {
        ComplainId = complainId;
    }

    public String getAddedDate() {
        return AddedDate;
    }

    public void setAddedDate(String addedDate) {
        AddedDate = addedDate;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public void setMedicineId(String medicineId) {
        MedicineId = medicineId;
    }

    public String getMedicineId() {
        return MedicineId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
