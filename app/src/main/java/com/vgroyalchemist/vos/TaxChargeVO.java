package com.vgroyalchemist.vos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TaxChargeVO  implements Parcelable {

    ArrayList<AllCartData> mAllCartData;
    ArrayList<TotalArrayVo> mTotalArrayVos;

    TotalArrayVo mTotalArrayVo;

    public TaxChargeVO(Parcel in) {
    }

    public static final Creator<TaxChargeVO> CREATOR = new Creator<TaxChargeVO>() {
        @Override
        public TaxChargeVO createFromParcel(Parcel in) {
            return new TaxChargeVO(in);
        }

        @Override
        public TaxChargeVO[] newArray(int size) {
            return new TaxChargeVO[size];
        }
    };

    public TaxChargeVO() {

    }

    public TotalArrayVo getmTotalArrayVo() {
        return mTotalArrayVo;
    }

    public void setmTotalArrayVo(TotalArrayVo mTotalArrayVo) {
        this.mTotalArrayVo = mTotalArrayVo;
    }

    public ArrayList<TotalArrayVo> getmTotalArrayVos() {
        return mTotalArrayVos;
    }

    public void setmTotalArrayVos(ArrayList<TotalArrayVo> mTotalArrayVos) {
        this.mTotalArrayVos = mTotalArrayVos;
    }

    public ArrayList<AllCartData> getmAllCartData() {
        return mAllCartData;
    }

    public void setmAllCartData(ArrayList<AllCartData> mAllCartData) {
        this.mAllCartData = mAllCartData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
