package com.redhat.cajun.navy.datagenerate;

import io.vertx.core.json.Json;

public class Victim {

    private double lat = 0.0f;
    private double lon = 0.0f;
    private int numberOfPeople = 0;
    private boolean isMedicalNeeded = false;
    private String victimName = null;
    private String victimPhoneNumber = null;



    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLatLon(double lat, double lon){
        setLat(lat);
        setLon(lon);
    }


    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public boolean isMedicalNeeded() {
        return isMedicalNeeded;
    }

    public void setMedicalNeeded(boolean medicalNeeded) {
        isMedicalNeeded = medicalNeeded;
    }

    public String getVictimName() {
        return victimName;
    }

    public void setVictimName(String victimName) {
        this.victimName = victimName;
    }

    public String getVictimPhoneNumber() {
        return victimPhoneNumber;
    }

    public void setVictimPhoneNumber(String victimPhoneNumber) {
        this.victimPhoneNumber = victimPhoneNumber;
    }


    @Override
    public String toString() {
        return Json.encode(this);
    }
}
