package com.vgroyalchemist.vos;

public class AltMedicineVO {

    String MedicineName;
    String MedicineId;
    String AltMedicineId;
    String Manufacturer;
    String Price;
    String MedicineImgPath;
    String StockAvailablity;
    int  AvgReview;
    String TotalRating;
    String TotalReview;
    String CartQty;
    String DefaultImage;
    String MedicineDescription;

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

    public String getStockAvailablity() {
        return StockAvailablity;
    }

    public void setStockAvailablity(String stockAvailablity) {
        StockAvailablity = stockAvailablity;
    }

    public String getMedicineImgPath() {
        return MedicineImgPath;
    }

    public void setMedicineImgPath(String medicineImgPath) {
        MedicineImgPath = medicineImgPath;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
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

    public String getAltMedicineId() {
        return AltMedicineId;
    }

    public void setAltMedicineId(String altMedicineId) {
        AltMedicineId = altMedicineId;
    }
}
