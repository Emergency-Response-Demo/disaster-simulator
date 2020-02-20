package com.redhat.cajun.navy.datagenerate;

import java.util.List;

public class ServicePolygon {
    private String id;
    private List<double[]> points;

    public ServicePolygon() {
    }
    
    public ServicePolygon(String id, List<double[]> points) {
        this.id = id;
        this.points = points;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<double[]> getPoints() {
        return this.points;
    }

    public void setPoints(List<double[]> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", points='" + getPoints() + "'" +
            "}";
    }

}