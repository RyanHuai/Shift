package com.mainframevampire.shift.data.model;

import android.support.annotation.NonNull;

import java.util.Comparator;

public class Shift implements Comparable<Shift> {
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
}
