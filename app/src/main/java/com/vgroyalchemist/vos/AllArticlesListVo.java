package com.vgroyalchemist.vos;

import android.os.Parcel;
import android.os.Parcelable;

public class AllArticlesListVo implements Parcelable {

    String id;
    String ShortDesc;
    String LongDesc;
    String ImageName;
    String BookMark;

    public AllArticlesListVo() {

    }

    public String getBookMark() {
        return BookMark;
    }

    public void setBookMark(String bookMark) {
        BookMark = bookMark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getLongDesc() {
        return LongDesc;
    }

    public void setLongDesc(String longDesc) {
        LongDesc = longDesc;
    }

    public String getShortDesc() {
        return ShortDesc;
    }

    public void setShortDesc(String shortDesc) {
        ShortDesc = shortDesc;
    }

    public AllArticlesListVo(Parcel in) {
        id = in.readString();
        ShortDesc = in.readString();
        LongDesc = in.readString();
        ImageName = in.readString();
        BookMark = in.readString();
    }

    public static final Creator<AllArticlesListVo> CREATOR = new Creator<AllArticlesListVo>() {
        @Override
        public AllArticlesListVo createFromParcel(Parcel in) {
            return new AllArticlesListVo(in);
        }

        @Override
        public AllArticlesListVo[] newArray(int size) {
            return new AllArticlesListVo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(ShortDesc);
        dest.writeString(LongDesc);
        dest.writeString(ImageName);
        dest.writeString(BookMark);
    }
}
