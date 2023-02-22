package com.vgroyalchemist.vos;

import android.os.Parcel;
import android.os.Parcelable;

public class MedicineSearchList implements Parcelable {

    String MedicineId;
    String MedicineName;
    String MedicineContent;
    String Manufacturer;
    String Mrp;
    String Discount;
    String Price;
    String PrescriptionReq;
    String UnitsPerPack;
    String ImagePath;
    String StockAvailablity;
    String MedicineDescription;
    String TotalRating;
    String TotalReview;
    String CartQty;
    int AvgReview;



    public int getAvgReview() {
        return AvgReview;
    }

    public void setAvgReview(int avgReview) {
        AvgReview = avgReview;
    }

    public String getMedicineDescription() {
        return MedicineDescription;
    }

    public void setMedicineDescription(String medicineDescription) {
        MedicineDescription = medicineDescription;
    }

    public String getTotalRating() {
        return TotalRating;
    }

    public void setTotalRating(String totalRating) {
        TotalRating = totalRating;
    }

    public String getTotalReview() {
        return TotalReview;
    }

    public void setTotalReview(String totalReview) {
        TotalReview = totalReview;
    }

    public String getCartQty() {
        return CartQty;
    }

    public void setCartQty(String cartQty) {
        CartQty = cartQty;
    }



    public MedicineSearchList() {

    }

    public String getStockAvailablity() {
        return StockAvailablity;
    }

    public void setStockAvailablity(String stockAvailablity) {
        StockAvailablity = stockAvailablity;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getMedicineContent() {
        return MedicineContent;
    }

    public void setMedicineContent(String medicineContent) {
        MedicineContent = medicineContent;
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

    public String getMrp() {
        return Mrp;
    }

    public void setMrp(String mrp) {
        Mrp = mrp;
    }

    public String getPrescriptionReq() {
        return PrescriptionReq;
    }

    public void setPrescriptionReq(String prescriptionReq) {
        PrescriptionReq = prescriptionReq;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getUnitsPerPack() {
        return UnitsPerPack;

    }

    public void setUnitsPerPack(String unitsPerPack) {
        UnitsPerPack = unitsPerPack;
    }

    public MedicineSearchList(Parcel in) {
        MedicineId = in.readString();
        MedicineName = in.readString();
        MedicineContent = in.readString();
        Manufacturer = in.readString();
        Mrp = in.readString();
        Discount = in.readString();
        Price = in.readString();
        PrescriptionReq = in.readString();
        UnitsPerPack = in.readString();
    }

    public static final Creator<MedicineSearchList> CREATOR = new Creator<MedicineSearchList>() {
        @Override
        public MedicineSearchList createFromParcel(Parcel in) {
            return new MedicineSearchList(in);
        }

        @Override
        public MedicineSearchList[] newArray(int size) {
            return new MedicineSearchList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(MedicineId);
        dest.writeString(MedicineName);
        dest.writeString(MedicineContent);
        dest.writeString(Manufacturer);
        dest.writeString(Mrp);
        dest.writeString(Discount);
        dest.writeString(Price);
        dest.writeString(PrescriptionReq);
        dest.writeString(UnitsPerPack);
    }
}
