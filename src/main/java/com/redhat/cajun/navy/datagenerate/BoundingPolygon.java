package com.redhat.cajun.navy.datagenerate;

import java.awt.geom.PathIterator;

public class BoundingPolygon extends java.awt.geom.Path2D.Double {
	
	//Go to Google Maps and find lat longs and put them in here.
	//Note that lat are Ys and longs are Xs, ie you probably have to transpose 
	//the order that is given in google maps
	
	/*
	 * Wilmington, NC bounding box with just a touch in the water.
	 * Please note if you want to add another coordinate system, 
	 * lets please externalize these points to a configuration file 
	 * and make other changes in the code base
	 * 
	 * Please note that if adding an area below, it needs to be 
	 * large enough in size to show up on the map
	 */
	
	private java.awt.geom.Point2D.Double points[] = {
			new java.awt.geom.Point2D.Double(-77.95, 34.26),
			new java.awt.geom.Point2D.Double(-77.82, 34.26),
			new java.awt.geom.Point2D.Double(-77.77, 34.24),
			new java.awt.geom.Point2D.Double(-77.812, 34.185),
			new java.awt.geom.Point2D.Double(-77.830, 34.195),
			new java.awt.geom.Point2D.Double(-77.868, 34.134),
			new java.awt.geom.Point2D.Double(-77.885, 34.081),
			new java.awt.geom.Point2D.Double(-77.89, 34.04),
			new java.awt.geom.Point2D.Double(-77.93, 33.96),
			new java.awt.geom.Point2D.Double(-77.919, 34.00),
			new java.awt.geom.Point2D.Double(-77.920, 34.05),
			new java.awt.geom.Point2D.Double(-77.927, 34.12),
			new java.awt.geom.Point2D.Double(-77.914, 34.126),
			new java.awt.geom.Point2D.Double(-77.937, 34.151),
			new java.awt.geom.Point2D.Double(-77.954, 34.190),
			new java.awt.geom.Point2D.Double(-77.955, 34.20),
			null,
			new java.awt.geom.Point2D.Double(-77.981, 34.232),
			new java.awt.geom.Point2D.Double(-77.954, 34.227),
			new java.awt.geom.Point2D.Double(-77.965, 34.207),
			new java.awt.geom.Point2D.Double(-77.964, 34.183),
			new java.awt.geom.Point2D.Double(-77.967, 34.184),
			new java.awt.geom.Point2D.Double(-77.971, 34.204),
			new java.awt.geom.Point2D.Double(-77.975, 34.221),
			null,
			new java.awt.geom.Point2D.Double(-77.943, 34.130),
			new java.awt.geom.Point2D.Double(-77.940, 34.128),
			new java.awt.geom.Point2D.Double(-77.939, 34.120),
			new java.awt.geom.Point2D.Double(-77.938, 34.117),
			new java.awt.geom.Point2D.Double(-77.941, 34.112),
			new java.awt.geom.Point2D.Double(-77.944, 34.116),
			new java.awt.geom.Point2D.Double(-77.947, 34.117),
			new java.awt.geom.Point2D.Double(-77.948, 34.122),
			new java.awt.geom.Point2D.Double(-77.946, 34.124),
			new java.awt.geom.Point2D.Double(-77.942, 34.123),
			new java.awt.geom.Point2D.Double(-77.943, 34.128),
			
	};
			
		BoundingPolygon()
	{
		init();
		
	}
	
	/*
	 * I need to be called any time I want to reload the bounding polygon 
	 */
	public void init()
	{
		//Clear the bounding polygon when using another coordinate system
		reset();
		
		if(points.length <= 2)
		{
			throw new RuntimeException("You must set at least 3 points that are not in a line to make an enclosed area");
		}
		
		boolean startOfPolygon = true;
		for(int c = 0; c < points.length; c++)
		{
			if(points[c] == null)
			{
				closePath();
				startOfPolygon = true;
			}else{
				if(startOfPolygon)
				{
					moveTo(points[c].getX(), points[c].getY());
					startOfPolygon = false;
				}else {
					lineTo(points[c].getX(), points[c].getY());
				}
			}
		}
		closePath();
	}
	
	//Creates a bounding box of an X
	public void setXCross()
	{
		points = new java.awt.geom.Point2D.Double[]{
				new java.awt.geom.Point2D.Double(-77.95, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.26),
				new java.awt.geom.Point2D.Double(-77.95, 34.20),
				new java.awt.geom.Point2D.Double(-77.82, 34.20)
		};
		init();
	}
	
	//Creates a bounding box of a square.
	public void setSquare()
	{
		points = new java.awt.geom.Point2D.Double[]{
				new java.awt.geom.Point2D.Double(-77.95, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.20),
				new java.awt.geom.Point2D.Double(-77.95, 34.20)
		};
		init();
	}
	
	//Does the points in the other direction
	public void setReverseSquare()
	{
		points = new java.awt.geom.Point2D.Double[]{
				new java.awt.geom.Point2D.Double(-77.82, 34.26),
				new java.awt.geom.Point2D.Double(-77.95, 34.26),
				new java.awt.geom.Point2D.Double(-77.95, 34.20),
				new java.awt.geom.Point2D.Double(-77.82, 34.20)
		};
		
		init();
	}
	
	//Creates a bounding box of a square.
	public void setsTwoSquaresWithOneInternalSquare()
	{
		points = new java.awt.geom.Point2D.Double[]{
				new java.awt.geom.Point2D.Double(-77.95, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.20),
				new java.awt.geom.Point2D.Double(-77.95, 34.20),
				null,
				new java.awt.geom.Point2D.Double(-77.95, 34.36),
				new java.awt.geom.Point2D.Double(-77.82, 34.36),
				new java.awt.geom.Point2D.Double(-77.82, 34.46),
				new java.awt.geom.Point2D.Double(-77.95, 34.46),
				null,
				//Internal Square no change
				new java.awt.geom.Point2D.Double(-77.90, 34.38),
				new java.awt.geom.Point2D.Double(-77.85, 34.38),
				new java.awt.geom.Point2D.Double(-77.85, 34.44),
				new java.awt.geom.Point2D.Double(-77.90, 34.44)
		};
		
		init();
	}
		
	//Creates a bounding box of a square.
	public void setsTwoSquares()
	{
		points = new java.awt.geom.Point2D.Double[]{
				new java.awt.geom.Point2D.Double(-77.95, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.20),
				new java.awt.geom.Point2D.Double(-77.95, 34.20),
				null,
				new java.awt.geom.Point2D.Double(-77.95, 34.36),
				new java.awt.geom.Point2D.Double(-77.82, 34.36),
				new java.awt.geom.Point2D.Double(-77.82, 34.46),
				new java.awt.geom.Point2D.Double(-77.95, 34.46),
				null,
		};
		
		init();
	}

	//Creates a bounding box of 2 points to ensure an exception is thrown if you do that.
	public void setTwoPoints()
	{
		points = new java.awt.geom.Point2D.Double[]{
				new java.awt.geom.Point2D.Double(-77.95, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.26)
		};
		init();
	}
	
	//Try to create a bounding box of 3 points in a line, which is not a triangle, and has no area.
	public void setThreePointsLine()
	{
		points = new java.awt.geom.Point2D.Double[]{
				new java.awt.geom.Point2D.Double(-77.95, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.26),
				new java.awt.geom.Point2D.Double(-77.60, 34.26),
		};
		init();
	}
	
	//Evaluate that a triangle works for the data.
	public void setThreePointsArea()
	{
		points = new java.awt.geom.Point2D.Double[]{
				new java.awt.geom.Point2D.Double(-77.95, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.26),
				new java.awt.geom.Point2D.Double(-77.60, 34.20),
		};
		init();
	}
	
	/*
	 * Simple toString method that returns the values for pasting into:
	 * http://www.copypastemap.com/map.php
	 */
	public String toString()
	{
        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append("These points below can be viewed with this URL below");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("http://www.copypastemap.com/index.html");
        stringBuilder.append(System.lineSeparator());
        
        /*
         * Probably should not use the points object as it was a means to an end.  Using the path object seems to be better.
         * 
        stringBuilder.append("These are the points from the internal points object");
        stringBuilder.append(System.lineSeparator());
        
        for(int c = 0; c < points.length;c++)
        {
	        stringBuilder.append(points[c].getY());
	        stringBuilder.append("\t");
	        stringBuilder.append(points[c].getX());
	        stringBuilder.append("\t");
	        stringBuilder.append("square2");
	        stringBuilder.append("\t");
	        stringBuilder.append("green");
	        stringBuilder.append("\t");
	        stringBuilder.append(1000+c+1);
	        stringBuilder.append("\t");
	        stringBuilder.append(1000+c+1);
	        stringBuilder.append(System.lineSeparator());
        }
        */
        
        stringBuilder.append("These are the points from the Path2d Objext");
        stringBuilder.append(System.lineSeparator());
        
        int c = 0;
        int base = 1000;
        for(PathIterator pathIterator = getPathIterator(null); !pathIterator.isDone(); c++ )
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
		return stringBuilder.toString();
	}
	
	//Helper method to create specific points
	public void setPoints(java.awt.geom.Point2D.Double[] points) {
		this.points = points;
		
		init();
	}
	
	public static void main(String args[])
	{
		BoundingPolygon boundingPolygon = new BoundingPolygon();
		
		//Methods to make it easy to see the varioius bounding boxes that can be cut and pasted into
		//http://www.copypastemap.com/map.php
		//boundingPolygon.setThreePointsArea();
		//boundingPolygon.setThreePointsLine();
		//boundingPolygon.setxcross();
		//boundingPolygon.setsquare();
		//boundingPolygon.setsTwoSquares();
		//boundingPolygon.setsTwoSquaresWithOneInternalSquare();
		
		System.err.println(boundingPolygon);
	}

}
