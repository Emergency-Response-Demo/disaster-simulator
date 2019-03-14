package com.redhat.cajun.navy.datagenerate;


public class BoundingPolygon extends java.awt.geom.Path2D.Double {
	
	//Go to Google Maps and find lat longs and put them in here.
	//Note that lat are Ys and longs are Xs, ie you probably have to transpose 
	//the order that is given in google maps
	
	//Wilmington, NC bounding box with just a touch in the water.
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
			
	public void setPoints(java.awt.geom.Point2D.Double[] points) {
		this.points = points;
	}

	BoundingPolygon()
	{
		init();
	}
	
	public void init()
	{
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
	
	public void setTwoPoints()
	{
		points = new java.awt.geom.Point2D.Double[]{
				new java.awt.geom.Point2D.Double(-77.95, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.26)
		};
		init();
	}
	
	public void setThreePointsLine()
	{
		points = new java.awt.geom.Point2D.Double[]{
				new java.awt.geom.Point2D.Double(-77.95, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.26),
				new java.awt.geom.Point2D.Double(-77.60, 34.26),
		};
		init();
	}
	
	public void setThreePointsArea()
	{
		points = new java.awt.geom.Point2D.Double[]{
				new java.awt.geom.Point2D.Double(-77.95, 34.26),
				new java.awt.geom.Point2D.Double(-77.82, 34.26),
				new java.awt.geom.Point2D.Double(-77.60, 34.20),
		};
		init();
	}
	
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
	
	public static void main(String args[])
	{
		BoundingPolygon boundingPolygon = new BoundingPolygon();
		//boundingPolygon.setThreePointsArea();
		//boundingPolygon.setThreePointsLine();
		//boundingPolygon.setxcross();
		//boundingPolygon.setsquare();
		
		System.err.println(boundingPolygon);
	}

}
