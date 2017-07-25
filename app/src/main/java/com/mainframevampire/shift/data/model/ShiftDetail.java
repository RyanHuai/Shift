package com.mainframevampire.shift.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShiftDetail implements Parcelable{
    private int id;
    private String start;   //"2017-01-22T06:35:57+00:00"  (string, ISO 8601)
    private String end;
    private String startLatitude;
    private String startLongitude;
    private String startAddress;
    private String startCity;
    private String startState;
    private String startPostcode;
    private String startCountry;    
    private String startLocationStatus;    //0 valid, 1 not valid, 2 not network, 3 waiting to be downloaded
    private String endLatitude;
    private String endLongitude;
    private String endAddress;
    private String endCity;
    private String endState;
    private String endPostcode;
    private String endCountry;
    private String endLocationStatus;  //0 valid, 1 not valid, 2 not network, 3 waiting to be downloaded
    private String image;

    public ShiftDetail(int id, String start, String end, String startLatitude, String startLongitude,
                       String startLocationStatus, String endLatitude, String endLongitude, String endLocationStatus, String image) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.startLocationStatus = startLocationStatus;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.endLocationStatus = endLocationStatus;
        this.image = image;
    }

    public ShiftDetail(int id, String start, String end,
                       String startAddress, String startCity, String startState,
                       String startPostcode, String startCountry, String startLocationStatus,
                       String endAddress, String endCity, String endState,
                       String endPostcode, String endCountry, String endLocationStatus, String image) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.startAddress = startAddress;
        this.startCity = startCity;
        this.startState = startState;
        this.startPostcode = startPostcode;
        this.startCountry = startCountry;
        this.startLocationStatus = startLocationStatus;
        this.endAddress = endAddress;
        this.endCity = endCity;
        this.endState = endState;
        this.endPostcode = endPostcode;
        this.endCountry = endCountry;
        this.endLocationStatus = endLocationStatus;
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

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getStartState() {
        return startState;
    }

    public void setStartState(String startState) {
        this.startState = startState;
    }

    public String getStartPostcode() {
        return startPostcode;
    }

    public void setStartPostcode(String startPostcode) {
        this.startPostcode = startPostcode;
    }

    public String getStartCountry() {
        return startCountry;
    }

    public void setStartCountry(String startCountry) {
        this.startCountry = startCountry;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public String getEndState() {
        return endState;
    }

    public void setEndState(String endState) {
        this.endState = endState;
    }

    public String getEndPostcode() {
        return endPostcode;
    }

    public void setEndPostcode(String endPostcode) {
        this.endPostcode = endPostcode;
    }

    public String getEndCountry() {
        return endCountry;
    }

    public void setEndCountry(String endCountry) {
        this.endCountry = endCountry;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStartLocationStatus() {
        return startLocationStatus;
    }

    public void setStartLocationStatus(String startLocationStatus) {
        this.startLocationStatus = startLocationStatus;
    }

    public String getEndLocationStatus() {
        return endLocationStatus;
    }

    public void setEndLocationStatus(String endLocationStatus) {
        this.endLocationStatus = endLocationStatus;
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
        dest.writeString(startAddress);
        dest.writeString(startCity);
        dest.writeString(startState);
        dest.writeString(startPostcode);
        dest.writeString(startCountry);
        dest.writeString(startLocationStatus);
        dest.writeString(endLatitude);
        dest.writeString(endLongitude);
        dest.writeString(endAddress);
        dest.writeString(endCity);
        dest.writeString(endState);
        dest.writeString(endPostcode);
        dest.writeString(endCountry);
        dest.writeString(endLocationStatus);
        dest.writeString(image);

    }

    private ShiftDetail(Parcel in) {
        id = in.readInt();
        start = in.readString();
        end = in.readString();
        startLatitude = in.readString();
        startLongitude = in.readString();
        startAddress = in.readString();
        startCity = in.readString();
        startState = in.readString();
        startPostcode = in.readString();
        startCountry = in.readString();
        startLocationStatus = in.readString();
        endLatitude = in.readString();
        endLongitude = in.readString();
        endAddress = in.readString();
        endCity = in.readString();
        endState = in.readString();
        endPostcode = in.readString();
        endCountry = in.readString();
        endLocationStatus = in.readString();
        image = in.readString();
    }

    public static final Creator<ShiftDetail> CREATOR = new Creator<ShiftDetail>() {
        @Override
        public ShiftDetail createFromParcel(Parcel source) {
            return new ShiftDetail(source);
        }

        @Override
        public ShiftDetail[] newArray(int size) {
            return new ShiftDetail[size];
        }
    };
}
