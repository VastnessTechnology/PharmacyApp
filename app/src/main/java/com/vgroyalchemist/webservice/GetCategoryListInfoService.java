package com.vgroyalchemist.webservice;

import android.content.Context;

import com.vgroyalchemist.requestobjects.MedicineListInfo;
import com.vgroyalchemist.utils.Tags;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.utils.network.HttpConnector;
import com.vgroyalchemist.vos.CategoryList;
import com.vgroyalchemist.vos.DashboardCategoryVO;
import com.vgroyalchemist.vos.DashboardMedicineCategory;
import com.vgroyalchemist.vos.GetAllBannerMenuVO;
import com.vgroyalchemist.vos.MedicineSearchList;
import com.vgroyalchemist.vos.ServerResponseVO;

 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;


public class GetCategoryListInfoService {

    public JSONObject generateRequestJSON() {

        JSONObject requestObject = null;
        JSONObject mObject = null;
        RequestFactory mRequestFactory = new RequestFactory();
        MedicineListInfo listInfo = new MedicineListInfo();
        try {
            requestObject = mRequestFactory.getFinalRequestObject(listInfo);
            try {
                mObject = requestObject.getJSONObject("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        return mObject;
    }

    public ServerResponseVO GetCategoryList(Context context, String url) {
        ServerResponseVO serverResponseVO = new ServerResponseVO();

        try {
            JSONObject requestJSON = generateRequestJSON();

            JSONObject responseJSON = sendRequest(url, requestJSON, context);
            Utility.debugger("VT GetCategoryList List....." + url);
            Utility.debugger("VT GetCategoryList Request....." + requestJSON);
            Utility.debugger("VT responseJSON  GetCategoryList..... " + responseJSON);
            serverResponseVO.setStatus(responseJSON.getString(Tags.PARAM_STATUS));
            serverResponseVO.setMsg(responseJSON.getString(Tags.PARAM_MSG));


            DashboardCategoryVO mDashboardCategoryVO = new DashboardCategoryVO();
            DashboardMedicineCategory dashboardMedicineCategory = new DashboardMedicineCategory();
            ArrayList<DashboardMedicineCategory> dashboardMedicineCategoryArrayList = new ArrayList<DashboardMedicineCategory>();

            try {
                JSONObject responseData = responseJSON.getJSONObject("details");
                JSONArray DashboardMedicineCategory = responseData.getJSONArray("DashboardMedicineCategory");
                JSONObject mJsonObject = null;
                for (int m = 0; m < DashboardMedicineCategory.length(); m++) {
                    dashboardMedicineCategory = new DashboardMedicineCategory();

                    mJsonObject = DashboardMedicineCategory.getJSONObject(m);

                    try {
                        dashboardMedicineCategory.setMedicineCategoryId(mJsonObject.getString("MedicineCategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        dashboardMedicineCategory.setMedicineCategoryName(mJsonObject.getString("MedicineCategoryName"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONArray Medicines = mJsonObject.getJSONArray("Medicines");
                    ArrayList<MedicineSearchList> medicineSearchLists = new ArrayList<MedicineSearchList>();
                    JSONObject MedicineValue;
                    if (Medicines.length() != 0) {
                        for (int i = 0; i < Medicines.length(); i++) {

                            MedicineSearchList medicineSearchList = new MedicineSearchList();
                            MedicineValue = Medicines.getJSONObject(i);
                            try {
                                medicineSearchList.setManufacturer(MedicineValue.getString("Manufacturer"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                medicineSearchList.setMrp(MedicineValue.getString("Mrp"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                medicineSearchList.setDiscount(MedicineValue.getString("Discount"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                medicineSearchList.setPrice(MedicineValue.getString("Price"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                medicineSearchList.setMedicineName(MedicineValue.getString("MedicineName"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                medicineSearchList.setMedicineId(MedicineValue.getString("MedicineId"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                medicineSearchList.setImagePath(MedicineValue.getString("ImagePath"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                medicineSearchList.setStockAvailablity(MedicineValue.getString("StockAvailablity"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                medicineSearchList.setMedicineDescription(MedicineValue.getString("MedicineDescription"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                medicineSearchList.setAvgReview(MedicineValue.optInt("AvgReview"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                medicineSearchList.setTotalRating(MedicineValue.getString("TotalRating"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                medicineSearchList.setTotalReview(MedicineValue.getString("TotalReview"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                medicineSearchList.setCartQty(MedicineValue.getString("Cartqty"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            medicineSearchLists.add(medicineSearchList);
                            dashboardMedicineCategory.setMedicineSearchLists(medicineSearchLists);
                        }

                        dashboardMedicineCategoryArrayList.add(dashboardMedicineCategory);

                        mDashboardCategoryVO.setDashboardMedicineCategories(dashboardMedicineCategoryArrayList);
                    }
                }


                CategoryList mCategoryList = new CategoryList();
                ArrayList<CategoryList> mCategoryLists = new ArrayList<CategoryList>();

                JSONArray PopularMedicineCategory = responseData.getJSONArray("PopularMedicineCategory");

                for (int m = 0; m < PopularMedicineCategory.length(); m++) {
                    mCategoryList = new CategoryList();

                    JSONObject MedicineCategory = PopularMedicineCategory.getJSONObject(m);

                    try {
                        mCategoryList.setMedicineCategoryName(MedicineCategory.getString("MedicineCategoryName"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        mCategoryList.setMedicineCategoryId(MedicineCategory.getString("MedicineCategoryId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        mCategoryList.setImagePath(MedicineCategory.getString("ImagePath"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mCategoryLists.add(mCategoryList);
                    mDashboardCategoryVO.setmCategoryLists(mCategoryLists);
                }


                GetAllBannerMenuVO getAllBannerMenuVO = new GetAllBannerMenuVO();
                ArrayList<GetAllBannerMenuVO> bannerMenuVOS = new ArrayList<GetAllBannerMenuVO>();
                ArrayList<GetAllBannerMenuVO> banneHomeVOS = new ArrayList<GetAllBannerMenuVO>();
                JSONArray offerBanner = responseData.getJSONArray("OfferBannerList");
                JSONArray mainBanner = responseData.getJSONArray("BannerList");

                for (int m = 0; m < offerBanner.length(); m++) {
                    getAllBannerMenuVO = new GetAllBannerMenuVO();
                    JSONObject BannerList = offerBanner.getJSONObject(m);
                    try {
                        getAllBannerMenuVO.setBanner(BannerList.getString("Banner"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    bannerMenuVOS.add(getAllBannerMenuVO);

                }
                mDashboardCategoryVO.setGetAllBannerMenuVOS(bannerMenuVOS);
                for (int m = 0; m < mainBanner.length(); m++) {
                    getAllBannerMenuVO = new GetAllBannerMenuVO();
                    JSONObject BannerList = mainBanner.getJSONObject(m);
                    try {
                        getAllBannerMenuVO.setBanner(BannerList.getString("Banner"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    banneHomeVOS.add(getAllBannerMenuVO);

                }
                mDashboardCategoryVO.setGetMainBannerMenuVOS(banneHomeVOS);
                serverResponseVO.setData(mDashboardCategoryVO);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverResponseVO;
    }


    public JSONObject sendRequest(String url, JSONObject requestObject, Context context) throws
            Exception {
        JSONObject responseObject = null;
        ArrayList<Long> list = new ArrayList<Long>(0);
        try {
            responseObject = HttpConnector.getJSONObject(url, requestObject);
        } catch (MalformedURLException mue) {
            Utility.debugger("MalformedURLException");
            mue.printStackTrace();
            throw mue;
        } catch (SocketTimeoutException ste) {
            Utility.debugger("SocketTimeoutException");
            ste.printStackTrace();
            throw ste;
        } catch (IOException ioe) {
            Utility.debugger("IOException");
            ioe.printStackTrace();
            throw ioe;
        } catch (JSONException je) {
            Utility.debugger("JSONException");
            je.printStackTrace();
            throw je;
        } catch (Exception e) {
            Utility.debugger("Exception");
            e.printStackTrace();
            throw e;
        } finally {
            //return isError;
        }
        return responseObject;
    }
}