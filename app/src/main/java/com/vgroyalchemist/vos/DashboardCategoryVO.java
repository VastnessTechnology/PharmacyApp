package com.vgroyalchemist.vos;

import java.util.ArrayList;

public class DashboardCategoryVO {

    ArrayList<DashboardMedicineCategory> dashboardMedicineCategories;
    ArrayList<CategoryList> mCategoryLists;
    ArrayList<GetAllBannerMenuVO> getAllBannerMenuVOS;
    ArrayList<GetAllBannerMenuVO> getMainBannerMenuVOS;


    public ArrayList<GetAllBannerMenuVO> getGetMainBannerMenuVOS() {
        return getMainBannerMenuVOS;
    }

    public void setGetMainBannerMenuVOS(ArrayList<GetAllBannerMenuVO> getMainBannerMenuVOS) {
        this.getMainBannerMenuVOS = getMainBannerMenuVOS;
    }

    public ArrayList<GetAllBannerMenuVO> getGetAllBannerMenuVOS() {
        return getAllBannerMenuVOS;
    }

    public void setGetAllBannerMenuVOS(ArrayList<GetAllBannerMenuVO> getAllBannerMenuVOS) {
        this.getAllBannerMenuVOS = getAllBannerMenuVOS;
    }

    public ArrayList<CategoryList> getmCategoryLists() {
        return mCategoryLists;
    }

    public void setmCategoryLists(ArrayList<CategoryList> mCategoryLists) {
        this.mCategoryLists = mCategoryLists;
    }

    public ArrayList<DashboardMedicineCategory> getDashboardMedicineCategories() {
        return dashboardMedicineCategories;
    }

    public void setDashboardMedicineCategories(ArrayList<DashboardMedicineCategory> dashboardMedicineCategories) {
        this.dashboardMedicineCategories = dashboardMedicineCategories;
    }
}
