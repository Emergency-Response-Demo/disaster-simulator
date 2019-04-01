package com.redhat.cajun.navy.datagenerate;

import io.vertx.core.json.Json;

import java.util.UUID;

public class Victim {

    private String id = null;
    private double lat = 0.0f;
    private double lon = 0.0f;
    private int numberOfPeople = 0;
    private boolean isMedicalNeeded = false;
    private String victimName = null;
    private String victimPhoneNumber = null;
    private String status = "REQUESTED";
    private String timestamp = null;


    public Victim() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return Json.encode(this);
    }
}
