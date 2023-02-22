package com.vgroyalchemist.vos;

import java.util.ArrayList;

public class ProductDetailVO {

    String MedicineId;
    String MedicineName;
    String BatchNo;
    String ScheduleDrug;
    String HsnCode;
    String MedicineContent;
    String Manufacturer;
    String CategoryId;
    String Mrp;
    String Discount;
    String Price;
    String PrescriptionReq;
    String PackingUnit;
    String SellingUnit;
    String UnitsPerPack;
    String GstApplicable;
    String GST;
    String CGST;
    String SGST;
    String Storage;
    String Description;
    String Uses;
    String SideEffects;
    String UseMedicine;
    String MedicineWorks;
    String QuickTips;
    String MissedDosage;
    String Alcohol;
    String Pregnancy;
    String Breastfeeding;
    String Driving;
    String Kidney;
    String Liver;
    String AddedBy;
    String AddedDate;
    String UpdateBy;
    String UpdateDate;
    ArrayList<MedicineImagevo> medicineImagevos;
    String uid;
    String mStringSelectQtyId;
    String MediCartCount;
    String TotalCartCount;
    String AvgReview;
    String TotalRating;
    String  TotalReview;
    ArrayList<AltMedicineVO> altMedicineVOArrayList ;
    String StockAvailablity;
    String MedicineDescription;
    String Cartqty;

    public String getMedicineDescription() {
        return MedicineDescription;
    }

    public void setMedicineDescription(String medicineDescription) {
        MedicineDescription = medicineDescription;
    }

    public String getCartqty() {
        return Cartqty;
    }

    public void setCartqty(String cartqty) {
        Cartqty = cartqty;
    }

    public String getStockAvailablity() {
        return StockAvailablity;
    }

    public void setStockAvailablity(String stockAvailablity) {
        StockAvailablity = stockAvailablity;
    }

    public ArrayList<AltMedicineVO> getAltMedicineVOArrayList() {
        return altMedicineVOArrayList;
    }

    public void setAltMedicineVOArrayList(ArrayList<AltMedicineVO> altMedicineVOArrayList) {
        this.altMedicineVOArrayList = altMedicineVOArrayList;
    }

    public String getAvgReview() {
        return AvgReview;
    }

    public void setAvgReview(String avgReview) {
        AvgReview = avgReview;
    }

    public String getTotalRating() {
        return TotalRating;
    }

    public void setTotalRating(String totalRating) {
        TotalRating = totalRating;
    }

    public void setTotalReview(String totalReview) {
        TotalReview = totalReview;
    }

    public String getTotalReview() {
        return TotalReview;
    }


    public String getMediCartCount() {
        return MediCartCount;
    }

    public void setMediCartCount(String mediCartCount) {
        MediCartCount = mediCartCount;
    }

    public String getTotalCartCount() {
        return TotalCartCount;
    }

    public void setTotalCartCount(String totalCartCount) {
        TotalCartCount = totalCartCount;
    }

    public String getmStringSelectQtyId() {
        return mStringSelectQtyId;
    }

    public void setmStringSelectQtyId(String mStringSelectQtyId) {
        this.mStringSelectQtyId = mStringSelectQtyId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<MedicineImagevo> getMedicineImagevos() {
        return medicineImagevos;
    }

    public void setMedicineImagevos(ArrayList<MedicineImagevo> medicineImagevos) {
        this.medicineImagevos = medicineImagevos;
    }

    public String getMedicineId() {
        return MedicineId;
    }

    public void setMedicineId(String medicineId) {
        MedicineId = medicineId;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getDiscount() {
        return Discount;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public String getStorage() {
        return Storage;
    }

    public void setStorage(String storage) {
        Storage = storage;
    }

    public String getMrp() {
        return Mrp;
    }

    public void setMrp(String mrp) {
        Mrp = mrp;
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

    public String getPrescriptionReq() {
        return PrescriptionReq;
    }

    public void setPrescriptionReq(String prescriptionReq) {
        PrescriptionReq = prescriptionReq;
    }

    public String getMedicineContent() {
        return MedicineContent;
    }

    public void setMedicineContent(String medicineContent) {
        MedicineContent = medicineContent;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public String getCGST() {
        return CGST;
    }

    public void setCGST(String CGST) {
        this.CGST = CGST;
    }

    public String getDescription() {
        return Description;
    }

    public void setGST(String GST) {
        this.GST = GST;
    }

    public String getGST() {
        return GST;
    }

    public void setGstApplicable(String gstApplicable) {
        GstApplicable = gstApplicable;
    }

    public String getGstApplicable() {
        return GstApplicable;
    }

    public void setHsnCode(String hsnCode) {
        HsnCode = hsnCode;
    }

    public String getHsnCode() {
        return HsnCode;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPackingUnit() {
        return PackingUnit;
    }

    public void setPackingUnit(String packingUnit) {
        PackingUnit = packingUnit;
    }

    public String getScheduleDrug() {
        return ScheduleDrug;
    }

    public void setMedicineWorks(String medicineWorks) {
        MedicineWorks = medicineWorks;
    }

    public String getMedicineWorks() {
        return MedicineWorks;
    }

    public void setScheduleDrug(String scheduleDrug) {
        ScheduleDrug = scheduleDrug;
    }

    public String getSellingUnit() {
        return SellingUnit;
    }

    public void setSellingUnit(String sellingUnit) {
        SellingUnit = sellingUnit;
    }

    public String getAddedBy() {
        return AddedBy;
    }

    public void setSGST(String SGST) {
        this.SGST = SGST;
    }

    public String getMissedDosage() {
        return MissedDosage;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    public String getSGST() {
        return SGST;
    }

    public void setQuickTips(String quickTips) {
        QuickTips = quickTips;
    }

    public String getQuickTips() {
        return QuickTips;
    }

    public void setSideEffects(String sideEffects) {
        SideEffects = sideEffects;
    }

    public String getAlcohol() {
        return Alcohol;
    }

    public void setAlcohol(String alcohol) {
            Alcohol = alcohol;
    }

    public String getBreastfeeding() {
        return Breastfeeding;
    }

    public void setBreastfeeding(String breastfeeding) {
        Breastfeeding = breastfeeding;
    }

    public String getSideEffects() {
        return SideEffects;
    }

    public String getUseMedicine() {
        return UseMedicine;
    }

    public void setMissedDosage(String missedDosage) {
        MissedDosage = missedDosage;
    }

    public String getUses() {
        return Uses;
    }

    public void setPregnancy(String pregnancy) {
        Pregnancy = pregnancy;
    }

    public String getAddedDate() {
        return AddedDate;
    }

    public void setDriving(String driving) {
        Driving = driving;
    }

    public String getDriving() {
        return Driving;
    }

    public void setUseMedicine(String useMedicine) {
        UseMedicine = useMedicine;
    }

    public String getKidney() {
        return Kidney;
    }

    public void setKidney(String kidney) {
        Kidney = kidney;
    }

    public String getLiver() {
        return Liver;
    }

    public void setUses(String uses) {
        Uses = uses;
    }

    public String getPregnancy() {
        return Pregnancy;
    }

    public void setAddedDate(String addedDate) {
        AddedDate = addedDate;
    }

    public String getUpdateBy() {
        return UpdateBy;
    }

    public void setLiver(String liver) {
        Liver = liver;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateBy(String updateBy) {
        UpdateBy = updateBy;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

}
