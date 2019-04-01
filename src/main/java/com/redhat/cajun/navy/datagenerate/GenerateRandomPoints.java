package com.redhat.cajun.navy.datagenerate;

import java.awt.geom.Rectangle2D;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.geom.Point2D.Double;

/*
 * Please note that x and y, which we are all pretty comfortable with in Latitude and Longitude is reversed.
 * Longitude is an x and Latitude is a y to just be confusing.
 * This simple class uses the BoundingPolygon and then finds points that are within the bounding polygon.
 * 
 */

public class GenerateRandomPoints {
	
	//This is marked as protected so extends of this call could easily update the bounding box points
	//from within this class or a class that extends this class
	//Leaving this here could in some cases in threading could create some bad behaviors.
	//For the purposes of the demo it probably doesn't matter.
	protected BoundingPolygon boundingPolygon = new BoundingPolygon();


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

}
