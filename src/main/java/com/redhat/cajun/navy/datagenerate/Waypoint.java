package com.redhat.cajun.navy.datagenerate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Waypoint implements com.redhat.cajun.navy.datagenerateinterface.Waypoint {

	private double latitude = 0.0;
	private double longitude = 0.0;
	
	public Waypoint(double latitude, double longitude)
	{
		this.setLatitude(latitude);
		this.setLongitude(longitude);
	}
	
	//Note that x is latitude
	public double getX() {
		
		return longitude;
	}

	
	//Note that Y is latitude
	public double getY() {
		return latitude;
	}

	
	public double getLatitude() {
		return round(latitude, 3);
	}

	
	public double getLongitude() {
		return round(longitude, 3);
	}

	
	public void setLatitude(double latitude) {
       if(latitude < -90 || latitude >  90)
       {
    	   throw new RuntimeException("Latitude values must be between -90 and 90 inclusive");
       }
       
       this.latitude = latitude;
		
	}
	
	public void setLongitude(double longitude) {
		if(longitude < -180 || longitude >  180)
	       {
	    	   throw new RuntimeException("Latitude values must be between -180 and 180 inclusive");
	       }
	       
	       this.longitude = longitude;
	}
	
	public String toString()
	{
	   StringBuilder stringBuilder = new StringBuilder();
	   
	   stringBuilder.append("Latitude: ");
	   stringBuilder.append(latitude);
	   stringBuilder.append(", Longitude: ");
	   stringBuilder.append(longitude);
	   
	   return stringBuilder.toString();
	}
	
    protected static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	public static void main(String args[])
	{
		System.err.println(new Waypoint(10, 10));
		System.err.println(new Waypoint(30, 40));
		System.err.println(new Waypoint(-90, -280));
		
	}
}
