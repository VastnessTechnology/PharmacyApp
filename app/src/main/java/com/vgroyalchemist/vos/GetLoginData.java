package com.vgroyalchemist.vos;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

public class GetLoginData implements Parcelable {

    public UserDetails user;

    public GetLoginData(Parcel in) {
    }

    public GetLoginData() {

    }

    public UserDetails getUser() {
        return user;
    }

    public void setUser(UserDetails user) {
        this.user = user;
    }

    public static final Creator<GetLoginData> CREATOR = new Creator<GetLoginData>() {
        @Override
        public GetLoginData createFromParcel(Parcel in) {
            return new GetLoginData(in);
        }

        @Override
        public GetLoginData[] newArray(int size) {
            return new GetLoginData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }



    public static class UserDetails implements Parcelable {


        public String emailid;
        public String firstname;
        public String Lastname;
        public String userid;
        public String isactive;
        public String ContactNo;


        public UserDetails(Parcel in) {
            emailid = in.readString();
            firstname = in.readString();
            Lastname = in.readString();
            userid = in.readString();
            isactive = in.readString();
            ContactNo= in.readString();
        }

        public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
            @Override
            public UserDetails createFromParcel(Parcel in) {
                return new UserDetails(in);
            }

            @Override
            public UserDetails[] newArray(int size) {
                return new UserDetails[size];
            }
        };

        public UserDetails() {

        }

        public String getContactNo() {
            return ContactNo;
        }

        public void setContactNo(String contactNo) {
            ContactNo = contactNo;
        }

        public String getEmailid() {
            return emailid;
        }

        public void setEmailid(String emailid) {
            this.emailid = emailid;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return Lastname;
        }

        public void setLastname(String lastname) {
            Lastname = lastname;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getIsactive() {
            return isactive;
        }

        public void setIsactive(String isactive) {
            this.isactive = isactive;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(emailid);
            dest.writeString(firstname);
            dest.writeString(Lastname);
            dest.writeString(userid);
            dest.writeString(isactive);
            dest.writeString(ContactNo);
        }
    }
}
