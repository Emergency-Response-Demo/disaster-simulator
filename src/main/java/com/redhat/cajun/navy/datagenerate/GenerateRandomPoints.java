package com.redhat.cajun.navy.datagenerate;

import java.awt.geom.Rectangle2D;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.geom.Point2D.Double;

/*
 * Please note that x and y, which we are all pretty comfortable with in Latitude and Longitude is reversed.
 * Longitude is an x and Latitude is a y to just be confusing.
 * This simple class uses the BoundingPolygon and then finds points that are within the bounding polygon.
 */

public class GenerateRandomPoints {
	
	//This is marked as protected so extends of this call could easily update the bounding box points 
	//from within this class or a class that extends this class
	//Leaving this here could in some cases in threading could create some bad behaviors.
	//For the purposes of the demo it probably doesn't matter.
	protected BoundingPolygon boundingPolygon = new BoundingPolygon();
	private int maxNumberOfPointsToReturn = 500;
	/*
	 * You can view the generated points on a map here
	 * http://www.copypastemap.com/map.php
	 */
	
	GenerateRandomPoints()
	{
		//A bunch of boxes to ensure the APIs work well.
		//Commented them in one at a time.  Last one wins if they are all uncommented
		//boundingPolygon.setThreePointsArea();
		//boundingPolygon.setThreePointsLine();
		//boundingPolygon.setxcross();
		//boundingPolygon.setsquare();
		
		//Debug statement so you can cut and paste the generated points into the copypastemap.com url noted above.
		//System.err.println(boundingPolygon);
	}

	/*
	 * I return a point that is the bounding rectangle of the min/max of x and y.
	 * It needs to be checked against the boundingPolygon to ensure it fits
	 */
	private Double getPoint()
	{
		Rectangle2D boundingRectangle = boundingPolygon.getBounds2D();
				
		double x = 0;
		double y = 0;
		try {
			x = ThreadLocalRandom.current().nextDouble(boundingRectangle.getMinX(), boundingRectangle.getMaxX());
			y = ThreadLocalRandom.current().nextDouble(boundingRectangle.getMinY(), boundingRectangle.getMaxY());
			return new Double(x, y);
		}catch(java.lang.IllegalArgumentException e) {
			System.err.println("You must ensure that the three or more points you pass in are not in a line");
			throw e;
		}
	}
	
	/*
	 * This is the method to use to get a point that is within the bounding polygon
	 */
	public Double getInternalPoint()
	{
		boolean running = true;
		int c = 0;
		while(running)
		{
			Double point = getPoint();
			if(boundingPolygon.contains(point.getX(), point.getY()))
			{
				return point;
			}else {
				c++;
				//100 because 10 didn't work
				//This ensure that we will always return if something goes sideways so we do not lock up this thread.
				//Think of this as a circuit breaker after 100 iterations.
			   if(c > 100)
			   {
				   running = false;
			   }
			}
		}
		return null;
	}
	
	/*
	 * Simple toString method that returns the values for pasting into:
	 * http://www.copypastemap.com/map.php
	 */
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		int countPointsFound = 0;
		boolean running = true;
		
		while(running)
		{
			Double point = getInternalPoint();
			
		    stringBuilder.append(point.getY());
		    stringBuilder.append("\t");
		    stringBuilder.append(point.getX());
		    stringBuilder.append("\t");
		    stringBuilder.append("circle1");
		    stringBuilder.append("\t");
		    stringBuilder.append("red");
		    stringBuilder.append("\t");
		    stringBuilder.append(countPointsFound+1);
		    stringBuilder.append("\t");
		    stringBuilder.append(countPointsFound+1);
		    stringBuilder.append(System.lineSeparator());
            countPointsFound++;
            
            if(countPointsFound >= maxNumberOfPointsToReturn)
            {
            	running = false;
            }
		}
		
		return stringBuilder.toString();
	}
	
	public int getMaxNumberOfPointsToReturn() {
		return this.maxNumberOfPointsToReturn;
	}

	public void setMaxNumberOfPoints(int maxNumberOfPointsToReturn) {
		this.maxNumberOfPointsToReturn = maxNumberOfPointsToReturn;
	}

	public static void main(String[] args) {
		//System.err.println("Starting generation");
		
		GenerateRandomPoints generateRandomPoints = new GenerateRandomPoints();
		
		//System.err.println(generateRandomPoints.getInternalPoint());
		
		generateRandomPoints.setMaxNumberOfPoints(1000);
		
		System.err.println(generateRandomPoints);
	}
}
