package com.redhat.cajun.navy.datagenerate;

import io.vertx.core.json.Json;

public class Victim {

    private double lat = 0.0f;
    private double lon = 0.0f;
    private int numberOfPeople = 0;
    private boolean isMedicalNeeded = false;
    private String victimName = null;
    private String victimPhoneNumber = null;
    private int age = 0;
    private String gender = "U";
    private boolean pregnant = false;
    private boolean conscious = true;
    private String mobility = "AMBULATORY";
    private int priority = 0;



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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isPregnant() {
        return pregnant;
    }

    public void setPregnant(boolean pregnant) {
        this.pregnant = pregnant;
    }

    public boolean isConscious() {
        return conscious;
    }

    public void setConscious(boolean conscious) {
        this.conscious = conscious;
    }

    public String getMobility() {
        return mobility;
    }

    public void setMobility(String mobility) {
        this.mobility = mobility;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return Json.encode(this);
    }
}
