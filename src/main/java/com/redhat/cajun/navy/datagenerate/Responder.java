package com.redhat.cajun.navy.datagenerate;

public class Responder {

    private String id;

    private Double latitude;

    private Double longitude;

    private Boolean enrolled;

    private String name;

    private String phoneNumber;

    private Integer boatCapacity;

    private Boolean medicalKit;

    private Boolean available = true;

    private Boolean person = false;

    private String type = "BOAT";
    private Double speed = 10.0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getBoatCapacity() {
        return boatCapacity;
    }

    public void setBoatCapacity(Integer boatCapacity) {
        this.boatCapacity = boatCapacity;
    }

    public Boolean getMedicalKit() {
        return medicalKit;
    }

    public void setMedicalKit(Boolean medicalKit) {
        this.medicalKit = medicalKit;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getPerson() {
        return person;
    }

    public void setPerson(Boolean person) {
        this.person = person;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(Boolean enrolled) {
        this.enrolled = enrolled;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }
}
