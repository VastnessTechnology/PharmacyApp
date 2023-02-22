package com.vgroyalchemist.vos;

import android.net.Uri;

public class MYPrescriptionVO {

    String ImageName;

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    Uri ImagePath;

    public Uri getImagePath() {
        return ImagePath;
    }

    public void setImagePath(Uri imagePath) {
        ImagePath = imagePath;
    }
}
