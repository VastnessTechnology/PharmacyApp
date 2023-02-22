package com.vgroyalchemist.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.vgroyalchemist.db.table.AddressMst;
import com.vgroyalchemist.db.table.CartItemMst;
import com.vgroyalchemist.utils.Utility;
import com.vgroyalchemist.vos.ProductDetailVO;
import com.vgroyalchemist.vos.UserDetailsVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class DBService {

    private SqliteDB sqliteDB;




    public int deleteSingleAddress(Context context, String itemList) {
        int result = -1;
        sqliteDB = new SqliteDB(context);
        try {
            sqliteDB.open();

            result = sqliteDB.database.delete(AddressMst.TABLE_NAME,
                    AddressMst.ADDRESS_ID + " = ?",
                    new String[]{String.valueOf(itemList)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqliteDB != null) {
                try {
                    sqliteDB.close();
                } catch (Exception e) {
                    sqliteDB = null;
                }
            }
        }
        return result;
    }



    public int getAddressRawId(Context context) {
        Cursor cursor = null;
        sqliteDB = new SqliteDB(context);
        int loginUrl = 0;
        try {
            sqliteDB.open();
            cursor = sqliteDB.database.rawQuery("Select * from " + AddressMst.TABLE_NAME + " limit 1", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                loginUrl = cursor.getInt(cursor.getColumnIndex(AddressMst.ADDRESS_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqliteDB != null) {
                try {
                    if (cursor != null)
                        cursor.close();
                    cursor = null;
                    sqliteDB.close();

                    sqliteDB = null;
                } catch (Exception e) {
                    cursor = null;
                    sqliteDB = null;

                }
            }
        }
        return loginUrl;
    }







    public ArrayList<UserDetailsVO> getSingleAddressData(Context context, int mIntRawId, int mIntDefault) {
        Cursor cursor = null;
        sqliteDB = new SqliteDB(context);
        ArrayList<UserDetailsVO> mProductLists = new ArrayList<UserDetailsVO>();
        try {
            sqliteDB.open();

            cursor = sqliteDB.database.rawQuery("Select * from " +
                    AddressMst.TABLE_NAME + " where " +
                    AddressMst.ADDRESS_ID + " = " + mIntRawId, null);

            Utility.debugger("VT get addr in else...." + "Select * from " +
                    AddressMst.TABLE_NAME + " where " +
                    AddressMst.ADDRESS_ID + " = " + mIntRawId);

            cursor.moveToFirst();
            do {
                UserDetailsVO mList = new UserDetailsVO();
                mList.setUserId(cursor.getString(cursor.getColumnIndex(AddressMst.USER_ID)));
                mList.setPincode(cursor.getString(cursor.getColumnIndex(AddressMst.PINCODE)));
                mList.setName(cursor.getString(cursor.getColumnIndex(AddressMst.NAME)));
                mList.setAddressType(cursor.getString(cursor.getColumnIndex(AddressMst.ADDRESS_TYPE)));
                mList.setAddress(cursor.getString(cursor.getColumnIndex(AddressMst.ADDRESS_ID)));
                mList.setContactNo(cursor.getString(cursor.getColumnIndex(AddressMst.CONTACT_NUMBER)));


                mProductLists.add(mList);
            }
            while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqliteDB != null) {
                try {
                    if (cursor != null)
                        cursor.close();
                    cursor = null;
                    sqliteDB.close();

                    sqliteDB = null;
                } catch (Exception e) {
                    cursor = null;
                    sqliteDB = null;
                }
            }
        }
        return mProductLists;
    }

    public ArrayList<UserDetailsVO> getSavedSingleAddressData(Context context, int mIntRawId) {
        Cursor cursor = null;
        sqliteDB = new SqliteDB(context);
        ArrayList<UserDetailsVO> mProductLists = new ArrayList<UserDetailsVO>();
        try {
            sqliteDB.open();


                cursor = sqliteDB.database.rawQuery("Select * from " +
                        AddressMst.TABLE_NAME + " where " +
                        AddressMst.ADDRESS_ID + " = " + mIntRawId, null);

                Utility.debugger("VT get addr in else...." + "Select * from " +
                        AddressMst.TABLE_NAME + " where " +
                        AddressMst.ADDRESS_ID + " = " + mIntRawId);


            cursor.moveToFirst();
            do {
                UserDetailsVO mList = new UserDetailsVO();
                mList.setRaw_id(cursor.getInt(cursor.getColumnIndex(AddressMst.ID)));
                mList.setUserId(cursor.getString(cursor.getColumnIndex(AddressMst.USER_ID)));
                mList.setPincode(cursor.getString(cursor.getColumnIndex(AddressMst.PINCODE)));
                mList.setContactNo(cursor.getString(cursor.getColumnIndex(AddressMst.CONTACT_NUMBER)));
                mList.setAddress(cursor.getString(cursor.getColumnIndex(AddressMst.ADDRESSONE)));
                mList.setAddressType(cursor.getString(cursor.getColumnIndex(AddressMst.ADDRESS_TYPE)));
                mList.setName(cursor.getString(cursor.getColumnIndex(AddressMst.NAME)));
                mList.setAddressId(cursor.getString(cursor.getColumnIndex(AddressMst.ADDRESS_ID)));

                mProductLists.add(mList);
            }
            while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqliteDB != null) {
                try {
                    if (cursor != null)
                        cursor.close();
                    cursor = null;
                    sqliteDB.close();

                    sqliteDB = null;
                } catch (Exception e) {
                    cursor = null;
                    sqliteDB = null;
                }
            }
        }
        return mProductLists;
    }

        public int getTotalCartCount(Context context, String mStringUid) {
        int result = -1;

        sqliteDB = new SqliteDB(context);
        Cursor cursor = null;
        ContentValues values = new ContentValues();
        try {
            sqliteDB.open();
            cursor = sqliteDB.database.rawQuery("Select * from " +
                    CartItemMst.TABLE_NAME + " where " +
                    CartItemMst.UID + " = '" + mStringUid, null);

            if (cursor != null && cursor.getCount() > 0) {
                result = cursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqliteDB != null) {
                try {
                    if (cursor != null)
                        cursor.close();
                    cursor = null;
                    sqliteDB.close();

                    sqliteDB = null;
                } catch (Exception e) {
                    cursor = null;
                    sqliteDB = null;

                }
            }
        }
        return result;
    }

    public boolean isProductAvailableInCart(Context context, String userId ,String medicineId) {

        boolean flag = false;
        sqliteDB = new SqliteDB(context);
        Cursor cursor = null;
        try {
            sqliteDB.open();

                Utility.debugger("vT in cart query iffff...." + "Select * from " +
                        CartItemMst.TABLE_NAME + " where " +
                        CartItemMst.MEDICINEID + " = '" + medicineId + "' AND " +
                        CartItemMst.UID + " = '" + userId + "'");

                cursor = sqliteDB.database.rawQuery("Select * from " +
                        CartItemMst.TABLE_NAME + " where " +
                        CartItemMst.MEDICINEID + " = '" + medicineId + "' AND " +
                        CartItemMst.UID + " = '" + userId + "'", null);

            flag = cursor != null && cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            if (sqliteDB != null) {
                try {
                    if (cursor != null)
                        cursor.close();
                    cursor = null;
                    sqliteDB.close();

                    sqliteDB = null;
                } catch (Exception e) {
                    cursor = null;
                    sqliteDB = null;

                }
            }
        }
        return flag;
    }


    public int addOrUpdateSingleItemToCartDetail(Context context, ProductDetailVO productDetailVO ) {
        int result = -1;

        sqliteDB = new SqliteDB(context);
        Cursor cursor = null;
        ContentValues values = new ContentValues();
        try {
            sqliteDB.open();
            cursor = sqliteDB.database.rawQuery("Select * from " +
                        CartItemMst.TABLE_NAME + " where " +
                    CartItemMst.MEDICINEID + " = '" + productDetailVO.getMedicineId()+
                        CartItemMst.UID + " = '" + productDetailVO.getUid() + "'", null);

            if (cursor != null && cursor.getCount() > 0) {

                values.put(CartItemMst.UID, productDetailVO.getUid());
                values.put(CartItemMst.PRICE, productDetailVO.getPrice());
                values.put(CartItemMst.MEDICINEID, productDetailVO.getMedicineId());
                values.put(CartItemMst.QTY, productDetailVO.getmStringSelectQtyId());
                int minQty = 1;
                sqliteDB.database.execSQL("UPDATE " + CartItemMst.TABLE_NAME + " SET " + CartItemMst.QTY + " = " + CartItemMst.QTY + " + " + productDetailVO.getmStringSelectQtyId() + " WHERE " + CartItemMst.UID + " = '" + productDetailVO.getUid() + "' AND " +  CartItemMst.MEDICINEID + " = '" + productDetailVO.getMedicineId() +"'");
                result = 1;

            } else {
                values.put(CartItemMst.UID, productDetailVO.getUid());
                values.put(CartItemMst.PRICE, productDetailVO.getPrice());
                values.put(CartItemMst.MEDICINEID, productDetailVO.getMedicineId());
                values.put(CartItemMst.QTY, productDetailVO.getmStringSelectQtyId());
                result = (int) sqliteDB.database.insert(CartItemMst.TABLE_NAME, null, values);

                if (result != -1) {
                    result = 2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqliteDB != null) {
                try {
                    if (cursor != null)
                        cursor.close();
                    cursor = null;
                    sqliteDB.close();

                    sqliteDB = null;
                } catch (Exception e) {
                    cursor = null;
                    sqliteDB = null;
                }
            }
        }
        Utility.debugger("VT add item in db..." + result);
        return result;
    }


//
//    public ArrayList<StoreAddressVO> getStoreSingleAddressData(Context context, int mIntRawId) {
//        Cursor cursor = null;
//        sqliteDB = new SqliteDB(context);
//        ArrayList<StoreAddressVO> mProductLists = new ArrayList<StoreAddressVO>();
//        try {
//            sqliteDB.open();
//
//            Utility.debugger("Vt store add..." + "Select * from " +
//                    StoreAddressMst.TABLE_NAME + " where " +
//                    StoreAddressMst.PICKUP_ID + " = " + mIntRawId);
//
//            cursor = sqliteDB.database.rawQuery("Select * from " +
//                    StoreAddressMst.TABLE_NAME + " where " +
//                    StoreAddressMst.PICKUP_ID + " = " + mIntRawId, null);
//
//            cursor.moveToFirst();
//            do {
//                StoreAddressVO mList = new StoreAddressVO();
//                mList.setRaw_id(cursor.getInt(cursor.getColumnIndex(StoreAddressMst.PICKUP_ID)));
//                mList.setPickup_id(cursor.getString(cursor.getColumnIndex(StoreAddressMst.PICKUP_ID)));
//                mList.setTelephone(cursor.getString(cursor.getColumnIndex(StoreAddressMst.MOBILE)));
//                mList.setStreet(cursor.getString(cursor.getColumnIndex(StoreAddressMst.STREET)));
//                mList.setCity(cursor.getString(cursor.getColumnIndex(StoreAddressMst.CITY)));
//                mList.setRegion_name(cursor.getString(cursor.getColumnIndex(StoreAddressMst.REGION_NAME)));
//                mList.setCountry_name(cursor.getString(cursor.getColumnIndex(StoreAddressMst.COUNTRY_NAME)));
//                mList.setCountry_id(cursor.getString(cursor.getColumnIndex(StoreAddressMst.COUNTRY_ID)));
//                mList.setRegion_id(cursor.getString(cursor.getColumnIndex(StoreAddressMst.REGION_ID)));
//                mList.setEmail(cursor.getString(cursor.getColumnIndex(StoreAddressMst.EMAIL)));
//                mList.setStore_name(cursor.getString(cursor.getColumnIndex(StoreAddressMst.STORE_NAME)));
//                mList.setPostcode(cursor.getString(cursor.getColumnIndex(StoreAddressMst.POSTCODE)));
//                mList.setLattitude(cursor.getString(cursor.getColumnIndex(StoreAddressMst.LATTITUDE)));
//                mList.setLongitude(cursor.getString(cursor.getColumnIndex(StoreAddressMst.LONGITUDE)));
//                mList.setDistance(cursor.getString(cursor.getColumnIndex(StoreAddressMst.DISTANCE)));
//
//                mProductLists.add(mList);
//            }
//            while (cursor.moveToNext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (sqliteDB != null) {
//                try {
//                    if (cursor != null)
//                        cursor.close();
//                    cursor = null;
//                    sqliteDB.close();
//
//                    sqliteDB = null;
//                } catch (Exception e) {
//                    cursor = null;
//                    sqliteDB = null;
//                }
//            }
//        }
//        return mProductLists;
//    }
//
//

//    public int insertAllAddress(Context context, ArrayList<UserDetailsVO> itemList) {
//        int result = -1;
//        sqliteDB = new SqliteDB(context);
//        try {
//            sqliteDB.open();
//            ContentValues mContentValues = new ContentValues();
//            sqliteDB.database.delete(AddressMst.TABLE_NAME, null, null);
//            for (int i = 0; i < itemList.size(); i++) {
//                mContentValues.put(AddressMst.USER_ID, itemList.get(i).getQes_app_user_id());
//                mContentValues.put(AddressMst.FIRST_NAME, itemList.get(i).getApp_user_firstname());
//                mContentValues.put(AddressMst.LAST_NAME, itemList.get(i).getApp_user_lastname());
//                mContentValues.put(AddressMst.MOBILE_NUMBER, itemList.get(i).getApp_user_mobileno());
//                mContentValues.put(AddressMst.COMPANY_NAME, itemList.get(i).getApp_user_company_name());
//                mContentValues.put(AddressMst.ADDRESS_ONE, itemList.get(i).getApp_user_address1());
//                mContentValues.put(AddressMst.ADDRESS_TWO, itemList.get(i).getApp_user_address2());
//                mContentValues.put(AddressMst.COUNTRY_NAME, itemList.get(i).getCountry_name());
//                mContentValues.put(AddressMst.STATE_NAME, itemList.get(i).getApp_user_state_name());
//                mContentValues.put(AddressMst.CITY_NAME, itemList.get(i).getApp_user_city_id());
//                mContentValues.put(AddressMst.ZIPCODE, itemList.get(i).getApp_user_zip_id());
//                mContentValues.put(AddressMst.COUNTRY_ID, itemList.get(i).getApp_user_country_id());
//                mContentValues.put(AddressMst.COUNTRY_CODE, itemList.get(i).getCountry_id());
//                mContentValues.put(AddressMst.REGION_ID, itemList.get(i).getApp_user_state_id());
//                mContentValues.put(AddressMst.ADDRESS_ID, itemList.get(i).getAddress_id());
//                mContentValues.put(AddressMst.IS_DEFAULT, itemList.get(i).getIs_default());
//                result = (int) sqliteDB.database.insert(AddressMst.TABLE_NAME, null, mContentValues);
//            }
//            mContentValues = null;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (sqliteDB != null) {
//                try {
//                    sqliteDB.close();
//                } catch (Exception e) {
//                    sqliteDB = null;
//
//                }
//            }
//        }
//        return result;
//    }

//    public int insertAllStoreAddress(Context context, ArrayList<StoreAddressVO> itemList) {
//        int result = -1;
//        sqliteDB = new SqliteDB(context);
//        try {
//            sqliteDB.open();
//            ContentValues mContentValues = new ContentValues();
//            sqliteDB.database.delete(StoreAddressMst.TABLE_NAME, null, null);
//            for (int i = 0; i < itemList.size(); i++) {
//                mContentValues.put(StoreAddressMst.PICKUP_ID, itemList.get(i).getPickup_id());
//                mContentValues.put(StoreAddressMst.STORE_NAME, itemList.get(i).getStore_name());
//                mContentValues.put(StoreAddressMst.STREET, itemList.get(i).getStreet());
//                mContentValues.put(StoreAddressMst.REGION_ID, itemList.get(i).getRegion_id());
//                mContentValues.put(StoreAddressMst.COUNTRY_ID, itemList.get(i).getCountry_id());
//                mContentValues.put(StoreAddressMst.CITY, itemList.get(i).getCity());
//                mContentValues.put(StoreAddressMst.POSTCODE, itemList.get(i).getPostcode());
//                mContentValues.put(StoreAddressMst.MOBILE, itemList.get(i).getTelephone());
//                mContentValues.put(StoreAddressMst.EMAIL, itemList.get(i).getEmail());
//                mContentValues.put(StoreAddressMst.REGION_NAME, itemList.get(i).getRegion_name());
//                mContentValues.put(StoreAddressMst.COUNTRY_NAME, itemList.get(i).getCountry_name());
//                mContentValues.put(StoreAddressMst.LATTITUDE, itemList.get(i).getLattitude());
//                mContentValues.put(StoreAddressMst.LONGITUDE, itemList.get(i).getLongitude());
//                mContentValues.put(StoreAddressMst.DISTANCE, itemList.get(i).getDistance());
//                result = (int) sqliteDB.database.insert(StoreAddressMst.TABLE_NAME, null, mContentValues);
//            }
//            mContentValues = null;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (sqliteDB != null) {
//                try {
//                    sqliteDB.close();
//                } catch (Exception e) {
//                    sqliteDB = null;
//
//                }
//            }
//        }
//        return result;
//    }

//    public ArrayList<StoreAddressVO> getStoreAddressData(Context context) {
//        Cursor cursor = null;
//        sqliteDB = new SqliteDB(context);
//        ArrayList<StoreAddressVO> mProductLists = new ArrayList<StoreAddressVO>();
//        try {
//            sqliteDB.open();
//
//            cursor = sqliteDB.database.rawQuery("Select * from " + StoreAddressMst.TABLE_NAME, null);
//            cursor.moveToFirst();
//            do {
//                StoreAddressVO mList = new StoreAddressVO();
//                mList.setPickup_id(cursor.getString(cursor.getColumnIndex(StoreAddressMst.PICKUP_ID)));
//                mList.setStore_name(cursor.getString(cursor.getColumnIndex(StoreAddressMst.STORE_NAME)));
//                mList.setStreet(cursor.getString(cursor.getColumnIndex(StoreAddressMst.STREET)));
//                mList.setRegion_id(cursor.getString(cursor.getColumnIndex(StoreAddressMst.REGION_ID)));
//                mList.setCountry_id(cursor.getString(cursor.getColumnIndex(StoreAddressMst.COUNTRY_ID)));
//                mList.setCity(cursor.getString(cursor.getColumnIndex(StoreAddressMst.CITY)));
//                mList.setPostcode(cursor.getString(cursor.getColumnIndex(StoreAddressMst.POSTCODE)));
//                mList.setTelephone(cursor.getString(cursor.getColumnIndex(StoreAddressMst.MOBILE)));
//                mList.setEmail(cursor.getString(cursor.getColumnIndex(StoreAddressMst.EMAIL)));
//                mList.setCountry_name(cursor.getString(cursor.getColumnIndex(StoreAddressMst.COUNTRY_NAME)));
//                mList.setRegion_name(cursor.getString(cursor.getColumnIndex(StoreAddressMst.REGION_NAME)));
//                mList.setLattitude(cursor.getString(cursor.getColumnIndex(StoreAddressMst.LATTITUDE)));
//                mList.setLongitude(cursor.getString(cursor.getColumnIndex(StoreAddressMst.LONGITUDE)));
//                mList.setDistance(cursor.getString(cursor.getColumnIndex(StoreAddressMst.DISTANCE)));
//                mProductLists.add(mList);
//            }
//            while (cursor.moveToNext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (sqliteDB != null) {
//                try {
//                    if (cursor != null)
//                        cursor.close();
//                    cursor = null;
//                    sqliteDB.close();
//
//                    sqliteDB = null;
//                } catch (Exception e) {
//                    cursor = null;
//                    sqliteDB = null;
//
//                }
//            }
//        }
//        return mProductLists;
//    }

//    public ArrayList<UserDetailsVO> getAddressData(Context context) {
//        Cursor cursor = null;
//        sqliteDB = new SqliteDB(context);
//        ArrayList<UserDetailsVO> mProductLists = new ArrayList<UserDetailsVO>();
//        try {
//            sqliteDB.open();
//
//            cursor = sqliteDB.database.rawQuery("Select * from " + AddressMst.TABLE_NAME, null);
//            cursor.moveToFirst();
//            do {
//                UserDetailsVO mList = new UserDetailsVO();
//                mList.setRaw_id(cursor.getInt(cursor.getColumnIndex(AddressMst.ID)));
//                mList.setQes_app_user_id(cursor.getString(cursor.getColumnIndex(AddressMst.USER_ID)));
//                mList.setApp_user_firstname(cursor.getString(cursor.getColumnIndex(AddressMst.FIRST_NAME)));
//                mList.setApp_user_lastname(cursor.getString(cursor.getColumnIndex(AddressMst.LAST_NAME)));
//                mList.setApp_user_mobileno(cursor.getString(cursor.getColumnIndex(AddressMst.MOBILE_NUMBER)));
//                mList.setApp_user_contactno(cursor.getString(cursor.getColumnIndex(AddressMst.CONTACT_NUMBER)));
//                mList.setApp_user_address1(cursor.getString(cursor.getColumnIndex(AddressMst.ADDRESS_ONE)));
//                mList.setApp_user_address2(cursor.getString(cursor.getColumnIndex(AddressMst.ADDRESS_TWO)));
//                mList.setCountry_name(cursor.getString(cursor.getColumnIndex(AddressMst.COUNTRY_NAME)));
//                mList.setApp_user_state_name(cursor.getString(cursor.getColumnIndex(AddressMst.STATE_NAME)));
//                mList.setApp_user_city_id(cursor.getString(cursor.getColumnIndex(AddressMst.CITY_NAME)));
//                mList.setApp_user_zip_id(cursor.getString(cursor.getColumnIndex(AddressMst.ZIPCODE)));
//                mList.setApp_user_company_name(cursor.getString(cursor.getColumnIndex(AddressMst.COMPANY_NAME)));
//                mList.setApp_user_country_id(cursor.getString(cursor.getColumnIndex(AddressMst.COUNTRY_ID)));
//                mList.setCountry_code(cursor.getString(cursor.getColumnIndex(AddressMst.COUNTRY_ID)));
//                mList.setApp_user_state_id(cursor.getString(cursor.getColumnIndex(AddressMst.REGION_ID)));
//                mList.setAddress_id(cursor.getString(cursor.getColumnIndex(AddressMst.ADDRESS_ID)));
//                mList.setIs_default(cursor.getString(cursor.getColumnIndex(AddressMst.IS_DEFAULT)));
//                mProductLists.add(mList);
//            }
//            while (cursor.moveToNext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (sqliteDB != null) {
//                try {
//                    if (cursor != null)
//                        cursor.close();
//                    cursor = null;
//                    sqliteDB.close();
//
//                    sqliteDB = null;
//                } catch (Exception e) {
//                    cursor = null;
//                    sqliteDB = null;
//
//                }
//            }
//        }
//        return mProductLists;
//    }

    public int insertAddress(Context context, UserDetailsVO itemList, String mStringRawId) {
        int result = -1;
        sqliteDB = new SqliteDB(context);
        Cursor cursor = null;
        ContentValues mContentValues = new ContentValues();
        try {
            sqliteDB.open();
            cursor = sqliteDB.database.rawQuery("Select * from " +
                    AddressMst.TABLE_NAME + " where " +
                    AddressMst.ADDRESS_ID + " = " + mStringRawId, null);

            if (cursor != null && cursor.getCount() > 0) {

                mContentValues.put(AddressMst.USER_ID, itemList.getUserId());
                mContentValues.put(AddressMst.PINCODE, itemList.getPincode());
                mContentValues.put(AddressMst.ADDRESSONE, itemList.getAddress());
                mContentValues.put(AddressMst.NAME, itemList.getName());
                mContentValues.put(AddressMst.CONTACT_NUMBER, itemList.getContactNo());
                mContentValues.put(AddressMst.STATE_NAME, "");
                mContentValues.put(AddressMst.CITY_NAM, "");
                mContentValues.put(AddressMst.ADDRESS_TYPE, itemList.getAddressType());
                mContentValues.put(AddressMst.ADDRESS_ID, itemList.getAddressId());

                result = sqliteDB.database.update(AddressMst.TABLE_NAME, mContentValues,
                        AddressMst.ADDRESS_ID + " = " + itemList.getAddressId(), null);
            } else {
                mContentValues.put(AddressMst.USER_ID, itemList.getUserId());
                mContentValues.put(AddressMst.PINCODE, itemList.getPincode());
                mContentValues.put(AddressMst.ADDRESSONE, itemList.getAddress());
                mContentValues.put(AddressMst.NAME, itemList.getName());
                mContentValues.put(AddressMst.CONTACT_NUMBER, itemList.getContactNo());
                mContentValues.put(AddressMst.STATE_NAME, "");
                mContentValues.put(AddressMst.CITY_NAM, "");
                mContentValues.put(AddressMst.ADDRESS_TYPE, itemList.getAddressType());
                mContentValues.put(AddressMst.ADDRESS_ID, itemList.getAddressId());

                result = (int) sqliteDB.database.insert(AddressMst.TABLE_NAME, null, mContentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqliteDB != null) {
                try {
                    if (cursor != null)
                        cursor.close();
                    cursor = null;
                    sqliteDB.close();

                    sqliteDB = null;
                } catch (Exception e) {
                    cursor = null;
                    sqliteDB = null;

                }
            }
        }
        return result;
    }


}
