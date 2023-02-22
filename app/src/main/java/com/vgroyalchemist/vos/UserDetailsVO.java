package com.vgroyalchemist.vos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.recyclerview.widget.RecyclerView;

public class UserDetailsVO  implements Parcelable {

    String AddressId;
    String Address;
    String Pincode;
    String Name;
    String ContactNo;
    String AddressType;
    String UserId;
    public int raw_id = 0;
    public String msg = "";
    private boolean status = false;
    String DefaultAddress;
    String StateId;
    String CityId;
    String StateName;
    String CityName;

    String IsSelected;


    public UserDetailsVO(Parcel in) {
        AddressId = in.readString();
        Address = in.readString();
        Pincode = in.readString();
        Name = in.readString();
        ContactNo = in.readString();
        AddressType = in.readString();
        UserId = in.readString();
        raw_id = in.readInt();
        DefaultAddress=in.readString();
        StateId =in.readString();
        CityId =in.readString();
        StateName =in.readString();
        CityName =in.readString();
        IsSelected =in.readString();
    }

    public static final Creator<UserDetailsVO> CREATOR = new Creator<UserDetailsVO>() {
        @Override
        public UserDetailsVO createFromParcel(Parcel in) {
            return new UserDetailsVO(in);
        }

        @Override
        public UserDetailsVO[] newArray(int size) {
            return new UserDetailsVO[size];
        }
    };



    public UserDetailsVO() {

    }

    public String getIsSelected() {
        return IsSelected;
    }

    public void setIsSelected(String isSelected) {
        IsSelected = isSelected;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String cityId) {
        CityId = cityId;
    }

    public String getDefaultAddress() {
        return DefaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        DefaultAddress = defaultAddress;
    }

    public String getStateId() {
        return StateId;
    }

    public void setStateId(String stateId) {
        StateId = stateId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public int getRaw_id() {
        return raw_id;
    }

    public void setRaw_id(int raw_id) {
        this.raw_id = raw_id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAddressId() {
        return AddressId;
    }

    public void setAddressId(String addressId) {
        AddressId = addressId;
    }

    public String getAddressType() {
        return AddressType;
    }

    public void setAddressType(String addressType) {
        AddressType = addressType;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }





    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(AddressId);
        dest.writeString(Address);
        dest.writeString(Pincode);
        dest.writeString(Name);
        dest.writeString(ContactNo);
        dest.writeString(AddressType);
        dest.writeString(DefaultAddress);
        dest.writeString(StateId);
        dest.writeString(CityId);
        dest.writeString(StateName);
        dest.writeString(CityName);
        dest.writeString(IsSelected);

    }
}
