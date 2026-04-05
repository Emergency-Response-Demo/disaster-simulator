package com.redhat.cajun.navy.datagenerate;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.geom.Path2D.Double;


import java.util.ArrayList;
import java.util.List;

public class BoundingPolygons {

	private static Logger log = LoggerFactory.getLogger(BoundingPolygons.class);

	private Double exclusionPolygons = null;
	private List<Zone> inclusionZones = new ArrayList<>();
	private double totalInclusionWeight = 0.0;

	public BoundingPolygons() {
		// Default inclusion polygon (Wilmington, NC area approx)
		Waypoint[] defaultPoints = new Waypoint[] {
				new Waypoint(34.20, -77.98),
				new Waypoint(34.20, -77.90),
				new Waypoint(34.25, -77.90),
				new Waypoint(34.25, -77.98),
				new Waypoint(34.20, -77.98) // Close the loop
		};
		setInclusionPolygon(defaultPoints);
	}

	public void setInclusionZones(List<Zone> zones) {
		this.inclusionZones = zones;
		updateTotalWeight();
	}

    public List<Zone> getInclusionZones() {
        return inclusionZones;
    }

	public void setInclusionPolygon(Waypoint waypoints[])
	{
		if(waypoints.length <= 2)
		{
			throw new RuntimeException("You must set at least 3 points that are not in a line to make an inclusion zone");
		}
		
		Double path = new Double();
		for(int c = 0; c < waypoints.length; c++)
		{
			
			if(c == 0)
			{
				path.moveTo(waypoints[c].getX(), waypoints[c].getY());
			}else {
				path.lineTo(waypoints[c].getX(), waypoints[c].getY());
			}
		}
		path.closePath();
		Zone zone = new Zone(path, 1.0);
		inclusionZones.add(zone);
		totalInclusionWeight += zone.getWeight();
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
	
	private Waypoint getWaypoint(Zone zone)
	{
		Rectangle2D boundingRectangle = zone.getBounds();
		
		double longitude = 0;
		double latitude = 0;
		try {
			longitude = ThreadLocalRandom.current().nextDouble(boundingRectangle.getMinX(), boundingRectangle.getMaxX());
			latitude = ThreadLocalRandom.current().nextDouble(boundingRectangle.getMinY(), boundingRectangle.getMaxY());
			return new Waypoint(latitude, longitude);
		}catch(java.lang.IllegalArgumentException e) {
			log.error("You must ensure that the three or more points you pass in are not in a line or that you have initilzied a proper polygon");
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
		if (inclusionZones.isEmpty()) {
			throw new RuntimeException("No inclusion zones defined");
		}

		boolean running = true;
		int c = 0;
		while(running)
		{
			// Pick a zone based on weight
			Zone zone = selectWeightedZone();
			Waypoint waypoint = getWaypoint(zone);

			if((zone.contains(waypoint.getX(), waypoint.getY()) && exclusionPolygons == null) ||
			   (zone.contains(waypoint.getX(), waypoint.getY()) && exclusionPolygons != null && !exclusionPolygons.contains(waypoint.getX(), waypoint.getY())))
			{
				return waypoint;
			}else {
				c++;
			   if(c > 100)
			   {
				   running = false;
			   }
			}
		}
		return getAveragedWaypoint();
	}

	private Zone selectWeightedZone() {
		double r = ThreadLocalRandom.current().nextDouble() * totalInclusionWeight;
		double count = 0.0;
		for (Zone z : inclusionZones) {
			count += z.getWeight();
			if (count >= r) return z;
		}
		return inclusionZones.get(0);
	}
	
	/**
	 * 
	 * Helper method in case we get all the way through 100 iterations of a random point to plop a Waypoint in the middle of all 
	 * of the bounding boxes.  This should probably never happen, but who knows.
	 * @return
	 */
	public Waypoint getAveragedWaypoint()
	{
		log.error("Something has gone wrong with generating the random point, either we maxed out on the 100 iterations or our exclusion zone is too large");
		
        if (inclusionZones.isEmpty()) return new Waypoint(0,0);

        Zone z = inclusionZones.get(0);
		Rectangle2D boundingRectangle = z.getBounds();
		return new Waypoint(Waypoint.round((boundingRectangle.getMaxY()+boundingRectangle.getMinY())/2, 5),
				Waypoint.round((boundingRectangle.getMaxX()+boundingRectangle.getMinX())/2,5));
	}
	
	public void clearCurrentPolygons()
	{
		exclusionPolygons = null;
		inclusionZones.clear();
		totalInclusionWeight = 0.0;
	}

	private void updateTotalWeight() {
		totalInclusionWeight = 0.0;
		for (Zone z : inclusionZones) {
			totalInclusionWeight += z.getWeight();
		}
	}
	
	/**
	 * Helper method to set a Waypoint Array
	 * @param waypoints
	 * @return
	 */
	public static Waypoint[] setWaypoints(double waypoints[][], boolean latitudeFirst)
	{
		Waypoint waypoint[] = new Waypoint[waypoints.length];
		for(int c = 0; c < waypoints.length; c++)
		{
			if(latitudeFirst)
			{
			   waypoint[c] = new Waypoint(waypoints[c][0], waypoints[c][1]);
			}else {
				waypoint[c] = new Waypoint(waypoints[c][1], waypoints[c][0]);
			}
		}
		
		return waypoint;
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
        for (Zone zone : inclusionZones) {
            for(PathIterator pathIterator = zone.getPath().getPathIterator(null); !pathIterator.isDone(); c++ )
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
