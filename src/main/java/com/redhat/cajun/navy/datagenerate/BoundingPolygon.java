package com.redhat.cajun.navy.datagenerate;


public class BoundingPolygon extends java.awt.geom.Path2D.Double {
	
	//Go to Google Maps and find lat longs and put them in here.
	//Note that lat are Ys and longs are Xs, ie you probably have to transpose 
	//the order that is given in google maps
	
	/*
	 * Wilmington, NC bounding box with just a touch in the water.
	 * Please note if you want to add another coordinate system, 
	 * lets please externalize these points to a configuration file 
	 * and make other changes in the code base
	 */
	
	private java.awt.geom.Point2D.Double points[] = {
			new java.awt.geom.Point2D.Double(-77.95, 34.26),
			new java.awt.geom.Point2D.Double(-77.82, 34.26),
			new java.awt.geom.Point2D.Double(-77.77, 34.24),
			new java.awt.geom.Point2D.Double(-77.84, 34.15),
			new java.awt.geom.Point2D.Double(-77.89, 34.04),
			new java.awt.geom.Point2D.Double(-77.93, 33.96),
			new java.awt.geom.Point2D.Double(-77.919, 34.00),
			new java.awt.geom.Point2D.Double(-77.925, 34.05),
			new java.awt.geom.Point2D.Double(-77.93, 34.12),
			new java.awt.geom.Point2D.Double(-77.96, 34.20)
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
		
		for(int c = 0; c < points.length; c++)
		{
			if(c == 0)
			{
				moveTo(points[c].getX(), points[c].getY());	
			}else {
				lineTo(points[c].getX(), points[c].getY());
			}
		}
		closePath();
	}
	
	//Creates a bounding box of an X
	public void setxcross()
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
	public void setsquare()
	{
		points = new java.awt.geom.Point2D.Double[]{
				new java.awt.geom.Point2D.Double(-77.95, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.20),
				new java.awt.geom.Point2D.Double(-77.95, 34.20)
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
	
	//Valuidate that a trianlge works for the data.
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
		
		System.err.println(boundingPolygon);
	}

}
