package com.mainframevampire.shift.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Comparator;

public class Shift implements Parcelable, Comparable<Shift> {
    private int id;
    private String start;   //"2017-01-22T06:35:57+00:00"  (string, ISO 8601)
    private String end;
    private String startLatitude;
    private String startLongitude;
    private String endLatitude;
    private String endLongitude;
    private String image;

    public Shift(int id, String start, String end, String startLatitude, String startLongitude, String endLatitude, String endLongitude, String image) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(String startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int compareTo(@NonNull Shift other) {
        return other.id - this.id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeString(startLatitude);
        dest.writeString(startLongitude);
        dest.writeString(endLatitude);
        dest.writeString(endLongitude);
        dest.writeString(image);
    }

    private Shift(Parcel in) {
        id = in.readInt();
        start = in.readString();
        end = in.readString();
        startLatitude = in.readString();
        startLongitude = in.readString();
        endLatitude = in.readString();
        endLongitude = in.readString();
        image = in.readString();
    }

    public static final Creator<Shift> CREATOR = new Creator<Shift>() {
        @Override
        public Shift createFromParcel(Parcel source) {
            return new Shift(source);
        }

        @Override
        public Shift[] newArray(int size) {
            return new Shift[size];
        }
    };
}
