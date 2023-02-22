package com.vgroyalchemist.vos;

import java.util.ArrayList;

public class DashboardMedicineCategory {

    String MedicineCategoryId;
    String MedicineCategoryName;


    ArrayList<MedicineSearchList> medicineSearchLists;


    public String getMedicineCategoryId() {
        return MedicineCategoryId;
    }

    public void setMedicineCategoryId(String medicineCategoryId) {
        MedicineCategoryId = medicineCategoryId;
    }

    public String getMedicineCategoryName() {
        return MedicineCategoryName;
    }

    public void setMedicineCategoryName(String medicineCategoryName) {
        MedicineCategoryName = medicineCategoryName;
    }

    public ArrayList<MedicineSearchList> getMedicineSearchLists() {
        return medicineSearchLists;
    }

    public void setMedicineSearchLists(ArrayList<MedicineSearchList> medicineSearchLists) {
        this.medicineSearchLists = medicineSearchLists;
    }
}
