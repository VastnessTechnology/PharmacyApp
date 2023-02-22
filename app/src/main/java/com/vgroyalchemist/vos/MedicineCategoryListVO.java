package com.vgroyalchemist.vos;

import android.os.Parcel;
import android.os.Parcelable;

public class MedicineCategoryListVO implements Parcelable {

    String MedicineId;
    String MedicineName;
    String CategoryId;
    String Mrp;
    String Discount;
    String Price;
    String UnitsPerPack;
    String Storage;
    String ImagePath;
    String StockAvailablity;
    int  AvgReview;
    String TotalRating;
    String TotalReview;
    String CartQty;
    String DefaultImage;
    String MedicineDescription;
    String Manufacturer;

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getMedicineDescription() {
        return MedicineDescription;
    }

    public void setMedicineDescription(String medicineDescription) {
        MedicineDescription = medicineDescription;
    }

    public int getAvgReview() {
        return AvgReview;
    }

    public void setAvgReview(int avgReview) {
        AvgReview = avgReview;
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

    public String getDefaultImage() {
        return DefaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        DefaultImage = defaultImage;
    }

    public MedicineCategoryListVO() {

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

    public String getUnitsPerPack() {
        return UnitsPerPack;

    }

    public void setUnitsPerPack(String unitsPerPack) {
        UnitsPerPack = unitsPerPack;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMrp() {
        return Mrp;
    }

    public void setMrp(String mrp) {
        Mrp = mrp;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public String getMedicineId() {
        return MedicineId;
    }

    public void setMedicineId(String medicineId) {
        MedicineId = medicineId;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getDiscount() {
        return Discount;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getStorage() {
        return Storage;

    }

    public void setStorage(String storage) {
        Storage = storage;
    }


    public MedicineCategoryListVO(Parcel in) {
        MedicineId = in.readString();
        MedicineName = in.readString();
        CategoryId = in.readString();
        Mrp = in.readString();
        Discount = in.readString();
        Price = in.readString();
        UnitsPerPack = in.readString();
        Storage = in.readString();
    }

    public static final Creator<MedicineCategoryListVO> CREATOR = new Creator<MedicineCategoryListVO>() {
        @Override
        public MedicineCategoryListVO createFromParcel(Parcel in) {
            return new MedicineCategoryListVO(in);
        }

        @Override
        public MedicineCategoryListVO[] newArray(int size) {
            return new MedicineCategoryListVO[size];
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
        dest.writeString(CategoryId);
        dest.writeString(Mrp);
        dest.writeString(Discount);
        dest.writeString(Price);
        dest.writeString(UnitsPerPack);
        dest.writeString(Storage);
    }
}
