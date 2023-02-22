package com.vgroyalchemist.vos;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryList implements Parcelable {

    String MedicineCategoryId;
    String MedicineCategoryName;
    String ImagePath;

    public CategoryList(Parcel in) {
        MedicineCategoryId = in.readString();
        MedicineCategoryName = in.readString();
        ImagePath = in.readString();
    }

    public CategoryList() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(MedicineCategoryId);
        dest.writeString(MedicineCategoryName);
        dest.writeString(ImagePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryList> CREATOR = new Creator<CategoryList>() {
        @Override
        public CategoryList createFromParcel(Parcel in) {
            return new CategoryList(in);
        }

        @Override
        public CategoryList[] newArray(int size) {
            return new CategoryList[size];
        }
    };

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

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


}
