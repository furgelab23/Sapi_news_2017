package com.example.a2017_09_13.sapi_news_2017.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;

/**
 * Created by 2017-09-13 on 11/10/2017.
 */

public class Event implements Parcelable {

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
    String title;
    String description;
    String uId;
    Coordinate coordinate;
    ArrayList<Picture> pictures;

    public Event(String title, String description, String uId, Coordinate coordinate, ArrayList<Picture> pictures) {
        this.title = title;
        this.description = description;
        this.uId = uId;
        this.coordinate = coordinate;
        this.pictures = pictures;
    }

    public Event() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected Event(Parcel in) {
        title = in.readString();
        description = in.readString();
        uId = in.readString();
        coordinate = in.readTypedObject(Coordinate.CREATOR);
        pictures = in.createTypedArrayList(Picture.CREATOR);//infok atrakasa // osszesurites
    }

    public ArrayList<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<Picture> pictures) {
        this.pictures = pictures;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(uId);
        parcel.writeTypedList(pictures);
        parcel.writeParcelable(coordinate, 0);
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", uId='" + uId + '\'' +
                ", coordinate=" + coordinate +
                ", pictures=" + pictures +
                '}';
    }
}
