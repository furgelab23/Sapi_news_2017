package com.example.a2017_09_13.sapi_news_2017.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 2017-09-13 on 11/10/2017.
 */

public class Coordinate implements Parcelable {

    String latitude;
    String longitude;

    public Coordinate() {
    }

    public Coordinate(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Coordinate(Parcel in) {
        latitude = in.readString();
        longitude = in.readString();
    }

    public static final Creator<Coordinate> CREATOR = new Creator<Coordinate>() {
        @Override
        public Coordinate createFromParcel(Parcel in) {
            return new Coordinate(in);
        }

        @Override
        public Coordinate[] newArray(int size) {
            return new Coordinate[size];
        }
    };

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(latitude);
        parcel.writeString(longitude);
    }
}
