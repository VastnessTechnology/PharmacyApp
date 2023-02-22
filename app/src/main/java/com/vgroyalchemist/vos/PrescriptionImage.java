package com.vgroyalchemist.vos;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class PrescriptionImage implements Parcelable {

    String mImageView;

    protected PrescriptionImage(Parcel in) {
        mImageView = in.readString();
    }

    public static final Creator<PrescriptionImage> CREATOR = new Creator<PrescriptionImage>() {
        @Override
        public PrescriptionImage createFromParcel(Parcel in) {
            return new PrescriptionImage(in);
        }

        @Override
        public PrescriptionImage[] newArray(int size) {
            return new PrescriptionImage[size];
        }
    };

    public String getmImageView() {
        return mImageView;
    }

    public void setmImageView(String mImageView) {
        this.mImageView = mImageView;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mImageView);
    }
}

