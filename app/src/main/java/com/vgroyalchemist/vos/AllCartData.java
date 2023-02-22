package com.vgroyalchemist.vos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AllCartData  {

    String CartId;
    String UserId;
    String MedicineId;
    String Qty;
    String Price;
    String Amount;
    String AddedBy;
    String AddedOn;
    String UpdateBy;
    String UpdateOn;
    String MedicineName;
    String Content;
    String MedicineImgPath;



    public AllCartData(Parcel in) {
        CartId = in.readString();
        UserId = in.readString();
        MedicineId = in.readString();
        Qty = in.readString();
        Price = in.readString();
        Amount = in.readString();
        AddedBy = in.readString();
        AddedOn = in.readString();
        UpdateBy = in.readString();
        UpdateOn = in.readString();
        MedicineName = in.readString();
        Content = in.readString();
        MedicineImgPath = in.readString();

    }




    public AllCartData() {

    }



    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMedicineImgPath() {
        return MedicineImgPath;
    }

    public void setMedicineImgPath(String medicineImgPath) {
        MedicineImgPath = medicineImgPath;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    public String getAddedOn() {
        return AddedOn;
    }

    public void setAddedOn(String addedOn) {
        AddedOn = addedOn;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getCartId() {
        return CartId;
    }

    public void setCartId(String cartId) {
        CartId = cartId;
    }

    public String getMedicineId() {
        return MedicineId;
    }

    public void setMedicineId(String medicineId) {
        MedicineId = medicineId;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getUpdateBy() {
        return UpdateBy;
    }

    public void setUpdateBy(String updateBy) {
        UpdateBy = updateBy;
    }

    public String getUpdateOn() {
        return UpdateOn;
    }

    public void setUpdateOn(String updateOn) {
        UpdateOn = updateOn;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }


}
