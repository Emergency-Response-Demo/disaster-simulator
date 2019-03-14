package com.redhat.cajun.navy.datagenerate;

import java.awt.geom.Rectangle2D;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.geom.Point2D.Double;

/*
 * Please note that x and y, which we are all pretty comfortable with in Latitude and Longitude is reversed.
 * This simple class takes in the boundingPolygon and then finds points that are within the bounding polygon.
 */

public class GenerateRandomPoints {
	
	private BoundingPolygon boundingPolygon = new BoundingPolygon();
	private int maxNumberOfPoints = 500;
	private boolean running = true;
	private int countPointsFound = 0;
	private Rectangle2D boundingRectangle = null;
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
		boundingRectangle = boundingPolygon.getBounds2D();
		
		//System.err.println(boundingPolygon);
	}

	/*
	 * I return a point that is the bounding rectangle of the min/max of x and y.
	 */
	private Double getPoint()
	{
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
			   if(c > 100)
			   {
				   running = false;
			   }
			}
		}
		return null;
	}
	
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		
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
            
            if(countPointsFound >= maxNumberOfPoints)
            {
            	running = false;
            }
		}
		
		return stringBuilder.toString();
	}
	
	public int getMaxNumberOfPoints() {
		return this.maxNumberOfPoints;
	}

	public void setMaxNumberOfPoints(int maxNumberOfPoints) {
		this.maxNumberOfPoints = maxNumberOfPoints;
	}

	public static void main(String[] args) {
		//System.err.println("Starting generation");
		
		GenerateRandomPoints generateRandomPoints = new GenerateRandomPoints();
		
		System.err.println(generateRandomPoints.getInternalPoint());
		
		generateRandomPoints.setMaxNumberOfPoints(1000);
		
		System.err.println(generateRandomPoints);
	}
}
