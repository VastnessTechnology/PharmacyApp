package com.vgroyalchemist.vos;

import android.os.Parcel;
import android.os.Parcelable;

public class GetAllReviewListVO implements Parcelable {

    String Review;
    String ReviewText;
    String UserName;
    String ReviewDate;

    protected GetAllReviewListVO(Parcel in) {
        Review = in.readString();
        ReviewText = in.readString();
        UserName = in.readString();
        ReviewDate = in.readString();
    }

    public static final Creator<GetAllReviewListVO> CREATOR = new Creator<GetAllReviewListVO>() {
        @Override
        public GetAllReviewListVO createFromParcel(Parcel in) {
            return new GetAllReviewListVO(in);
        }

        @Override
        public GetAllReviewListVO[] newArray(int size) {
            return new GetAllReviewListVO[size];
        }
    };

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public String getReviewDate() {
        return ReviewDate;
    }

    public void setReviewDate(String reviewDate) {
        ReviewDate = reviewDate;
    }

    public String getReviewText() {
        return ReviewText;
    }

    public void setReviewText(String reviewText) {
        ReviewText = reviewText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Review);
        dest.writeString(ReviewText);
        dest.writeString(UserName);
        dest.writeString(ReviewDate);
    }
}
