package com.vgroyalchemist.vos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OrderFiltersVo  implements Parcelable {

    ArrayList<TimeFilterVO> mTimeFilterVOS;
    ArrayList<OrderFlterMenu> orderFlterMenus;

    public OrderFiltersVo(Parcel in) {
    }

    public static final Creator<OrderFiltersVo> CREATOR = new Creator<OrderFiltersVo>() {
        @Override
        public OrderFiltersVo createFromParcel(Parcel in) {
            return new OrderFiltersVo(in);
        }

        @Override
        public OrderFiltersVo[] newArray(int size) {
            return new OrderFiltersVo[size];
        }
    };

    public OrderFiltersVo() {

    }

    public ArrayList<OrderFlterMenu> getOrderFlterMenus() {
        return orderFlterMenus;
    }

    public void setOrderFlterMenus(ArrayList<OrderFlterMenu> orderFlterMenus) {
        this.orderFlterMenus = orderFlterMenus;
    }

    public ArrayList<TimeFilterVO> getmTimeFilterVOS() {
        return mTimeFilterVOS;
    }

    public void setmTimeFilterVOS(ArrayList<TimeFilterVO> mTimeFilterVOS) {
        this.mTimeFilterVOS = mTimeFilterVOS;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
