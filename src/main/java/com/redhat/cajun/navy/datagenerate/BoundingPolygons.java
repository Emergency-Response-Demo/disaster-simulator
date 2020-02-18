package com.redhat.cajun.navy.datagenerate;

import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.geom.Path2D.Double;


public class BoundingPolygons {

	private Double exclusionPolygons = null;
	private Double inclusionPolygons = null;
	

	public void setInclusionPolygon(Waypoint waypoints[])
	{
		if(inclusionPolygons == null)
		{
			inclusionPolygons = new Double();	
		}
		
		if(waypoints.length <= 2)
		{
			throw new RuntimeException("You must set at least 3 points that are not in a line to make an inclusion zone");
		}
		
		for(int c = 0; c < waypoints.length; c++)
		{
			
			if(c == 0)
			{
				inclusionPolygons.moveTo(waypoints[c].getX(), waypoints[c].getY());
			}else {
				inclusionPolygons.lineTo(waypoints[c].getX(), waypoints[c].getY());
			}
		}
		inclusionPolygons.closePath();
	}
	
	public void setExclusionPolygon(Waypoint waypoints[])
	{
		if(exclusionPolygons == null)
		{
			exclusionPolygons = new Double();	
		}
		
		if(waypoints.length <= 2)
		{
			throw new RuntimeException("You must set at least 3 points that are not in a line to make an exclusion zone");
		}
		
		for(int c = 0; c < waypoints.length; c++)
		{
			
			if(c == 0)
			{
				exclusionPolygons.moveTo(waypoints[c].getX(), waypoints[c].getY());
			}else {
				exclusionPolygons.lineTo(waypoints[c].getX(), waypoints[c].getY());
			}
		}
		exclusionPolygons.closePath();
	}
	
	private Waypoint getWaypoint()
	{
		Rectangle2D boundingRectangle = inclusionPolygons.getBounds2D();
		
		double longitude = 0;
		double latitude = 0;
		try {
			longitude = ThreadLocalRandom.current().nextDouble(boundingRectangle.getMinX(), boundingRectangle.getMaxX());
			latitude = ThreadLocalRandom.current().nextDouble(boundingRectangle.getMinY(), boundingRectangle.getMaxY());
			return new Waypoint(latitude, longitude);
			//return new Waypoint(round(x,5), round(y, 5));
		}catch(java.lang.IllegalArgumentException e) {
			System.err.println("You must ensure that the three or more points you pass in are not in a line or that you have initilzied a proper polygon");
			throw new RuntimeException("You must ensure that the three or more points you pass in are not in a line or that you have initilzied a proper polygon");
		}
	}
	
	public Waypoint[] getInternalWaypoints(int number)
	{
		Waypoint waypoints[] = new Waypoint[number];
		
		for(int c = 0;c < number ;c++)
		{
			waypoints[c] = getInternalWaypoint();
		}
		
		return waypoints;
	}
	
	public Waypoint getInternalWaypoint()
	{
		boolean running = true;
		int c = 0;
		while(running)
		{
			Waypoint waypoint = getWaypoint();
			//System.err.println("Waypoint 1: " + waypoint + " " å+ exclusionPolygons);
			if((inclusionPolygons.contains(waypoint.getX(), waypoint.getY()) && exclusionPolygons == null) ||
			   (inclusionPolygons.contains(waypoint.getX(), waypoint.getY()) && exclusionPolygons != null && !exclusionPolygons.contains(waypoint.getX(), waypoint.getY())))
			{
				//System.err.println("Waypoint 2: " + waypoint);
				return waypoint;
			}else {
				c++;
				//100 because 10 didn't work
				//This ensure that we will always return if something goes sideways so we do not lock up this thread.
				//Think of this as a circuit breaker after 100 iterations.
			   if(c > 100)
			   {
				   running = false;
				   //System.err.println("Stopping at 100");
			   }
			}
		}
		/*
		System.err.println(boundingRectangle.getMinX());
		System.err.println(boundingRectangle.getMaxX());
		System.err.println(boundingRectangle.getMinY());
		System.err.println(boundingRectangle.getMaxY());
		System.err.println((boundingRectangle.getMaxX()+boundingRectangle.getMinX())/2);
		System.err.println((boundingRectangle.getMaxY()+boundingRectangle.getMinY())/2);
		*/

		//System.err.println("Waypoint 3: " + getAveragedWaypoint());
		return getAveragedWaypoint();
	}
	
	/**
	 * 
	 * Helper method in case we get all the way through 100 iterations of a random point to plop a Waypoint in the middle of all 
	 * of the bounding boxes.  This should probably never happen, but who knows.
	 * @return
	 */
	public Waypoint getAveragedWaypoint()
	{
		System.err.println("Something has gone wrong with generating the random point, either we maxed out on the 100 iterations or our exclusion zone is too large");
		
		Rectangle2D boundingRectangle = inclusionPolygons.getBounds2D();
		return new Waypoint(Waypoint.round((boundingRectangle.getMaxY()+boundingRectangle.getMinY())/2, 5),
				Waypoint.round((boundingRectangle.getMaxX()+boundingRectangle.getMinX())/2,5));
	}
	
	public void clearCurrentPolygons()
	{
		exclusionPolygons = null;
		inclusionPolygons = null;
	}
	
	public String toString()
	{
        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append("These points below can be viewed with this URL below");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("http://www.copypastemap.com/index.html");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("These are the points from the Path2d Objext");
        stringBuilder.append(System.lineSeparator());
        
        int c = 0;
        int base = 1000;
        for(PathIterator pathIterator = inclusionPolygons.getPathIterator(null); !pathIterator.isDone(); c++ )
        {
        	double points[] = new double[6];
        	int value = pathIterator.currentSegment(points);
        	if(value == PathIterator.SEG_MOVETO || value == PathIterator.SEG_LINETO)
        	{
		        stringBuilder.append(points[1]);
		        stringBuilder.append("\t");
		        stringBuilder.append(points[0]);
		        stringBuilder.append("\t");
		        stringBuilder.append("square1");
		        stringBuilder.append("\t");
		        stringBuilder.append("green");
		        stringBuilder.append("\t");
		        stringBuilder.append(base+c);
		        stringBuilder.append("\t");
		        stringBuilder.append(base+c);
		        stringBuilder.append(System.lineSeparator());
        	}else {
        		base+=1000;
        		c=0;
        	}
        	
	        pathIterator.next();
        }
        
        if(exclusionPolygons != null)
        {
	        c = 0;
	        for(PathIterator pathIterator = exclusionPolygons.getPathIterator(null); !pathIterator.isDone(); c++ )
	        {
	        	double points[] = new double[6];
	        	int value = pathIterator.currentSegment(points);
	        	if(value == PathIterator.SEG_MOVETO || value == PathIterator.SEG_LINETO)
	        	{
			        stringBuilder.append(points[1]);
			        stringBuilder.append("\t");
			        stringBuilder.append(points[0]);
			        stringBuilder.append("\t");
			        stringBuilder.append("triangle1");
			        stringBuilder.append("\t");
			        stringBuilder.append("red");
			        stringBuilder.append("\t");
			        stringBuilder.append(base+c);
			        stringBuilder.append("\t");
			        stringBuilder.append(base+c);
			        stringBuilder.append(System.lineSeparator());
	        	}else {
	        		base+=1000;
	        		c=0;
	        	}
	        	
		        pathIterator.next();
	        }
        }
		return stringBuilder.toString();
	}
}
