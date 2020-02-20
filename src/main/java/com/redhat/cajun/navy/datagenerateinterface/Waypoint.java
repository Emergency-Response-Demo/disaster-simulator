package com.redhat.cajun.navy.datagenerateinterface;

/**
 * 
 * @author jimtyrrell
 * The Definition of what is a waypoint and the methods we should have
 */
public interface Waypoint {
   
	
   public double getX();
   public double getY();
   public double getLatitude();
   public double getLongitude();
   
   
   public void setLatitude(double latitude);
   public void setLongitude(double longitude);
   
	
}
