package com.vgroyalchemist.vos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GetStateData implements Parcelable {

    public List<StateDetail> detail;
    public String status = "";
    public String msg = "";

    public GetStateData() {

    }

    protected GetStateData(Parcel in) {
        detail = in.createTypedArrayList(StateDetail.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(detail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetStateData> CREATOR = new Creator<GetStateData>() {
        @Override
        public GetStateData createFromParcel(Parcel in) {
            return new GetStateData(in);
        }

        @Override
        public GetStateData[] newArray(int size) {
            return new GetStateData[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<StateDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<StateDetail> detail) {
        this.detail = detail;
    }



    public static class StateDetail implements Parcelable {

        String StateId;
        String State;

        public static final Creator<StateDetail> CREATOR = new Creator<StateDetail>() {
            @Override
            public StateDetail createFromParcel(Parcel in) {
                return new StateDetail(in);
            }

            @Override
            public StateDetail[] newArray(int size) {
                return new StateDetail[size];
            }
        };

        public StateDetail() {

        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public String getStateId() {
            return StateId;
        }

        public String setStateId(String stateId) {
            StateId = stateId;
            return stateId;
        }

        public StateDetail(Parcel in) {
            StateId = in.readString();
            State = in.readString();
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(StateId);
            dest.writeString(State);
        }
    }

}