package com.example.a2017_09_13.sapi_news_2017.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 2017-09-13 on 11/24/2017.
 */

public class Picture implements Parcelable {

    String pictureId;

    public Picture() {
    }

    protected Picture(Parcel in) {
        pictureId = in.readString();
    }

    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel in) {
            return new Picture(in);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(pictureId);
    }
}
