package com.vgroyalchemist.vos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class GetCityData implements Parcelable {


    public List<CityDeatils> detail;

    public GetCityData() {

    }

    public List<CityDeatils> getDetail() {
        return detail;
    }

    public void setDetail(List<CityDeatils> detail) {
        this.detail = detail;
    }

    public GetCityData(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetCityData> CREATOR = new Creator<GetCityData>() {
        @Override
        public GetCityData createFromParcel(Parcel in) {
            return new GetCityData(in);
        }

        @Override
        public GetCityData[] newArray(int size) {
            return new GetCityData[size];
        }
    };
    public static class CityDeatils implements Parcelable {

        String CityId;
        String StateId;
        String CityName;

        public CityDeatils() {

        }

        public String getStateId() {
            return StateId;
        }

        public void setStateId(String stateId) {
            StateId = stateId;
        }

        public String getCityId() {
            return CityId;
        }

        public void setCityId(String cityId) {
            CityId = cityId;
        }

        public String getCityName() {
            return CityName;
        }

        public void setCityName(String cityName) {
            CityName = cityName;
        }

        public CityDeatils(Parcel in) {
            CityId = in.readString();
            StateId = in.readString();
            CityName = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(CityId);
            dest.writeString(StateId);
            dest.writeString(CityName);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<CityDeatils> CREATOR = new Creator<CityDeatils>() {
            @Override
            public CityDeatils createFromParcel(Parcel in) {
                return new CityDeatils(in);
            }

            @Override
            public CityDeatils[] newArray(int size) {
                return new CityDeatils[size];
            }
        };
    }
}