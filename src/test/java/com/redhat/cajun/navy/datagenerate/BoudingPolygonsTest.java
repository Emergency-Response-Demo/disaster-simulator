package com.redhat.cajun.navy.datagenerate;

import org.junit.Test;
import com.redhat.cajun.navy.datagenerate.Waypoint;;

public class BoudingPolygonsTest {
	private int count = 1;
	
	@Test
    public void testGenerateSquare()
    {
		double waypoints[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				{34.20, -77.82},
				{34.20, -77.95}
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypoints, true));
		
		printPolygonOutput("Simple Square Test", boundingPolygons);
		System.err.println("");
		System.err.println("");
    }
	
	@Test
    public void testXCross()
    {
		double waypoints[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				{34.20, -77.95},
				{34.20, -77.82}
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypoints, true));
		
		printPolygonOutput("Simple X Cross", boundingPolygons);
		System.err.println("");
		System.err.println("");
    }
	
	@Test
    public void testXCross1000Points()
    {
		double waypoints1[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				{34.20, -77.95},
				{34.20, -77.82}
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypoints1, true));
		
		printPolygonOutput("Simple X Cross with 1000 Points", boundingPolygons);

		Waypoint[] waypoints = boundingPolygons.getInternalWaypoints(1000);
		
		for(Waypoint waypoint: waypoints)
		{
			printWaypoint(waypoint);
						
		}
		
		System.err.println("");
		System.err.println("");
    }
	
	@Test
    public void testReverseSquare()
    {
		double waypoints[][] = {
				{34.26, -77.82},
				{34.26, -77.95},
				{34.20, -77.95},
				{34.20, -77.82}
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypoints, true));
		
		printPolygonOutput("Test Reverse Square", boundingPolygons);
		System.err.println("");
		System.err.println("");
    }
	
	@Test
    public void testTwoSquares()
    {
		double waypointsSquare1[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				{34.20, -77.82},
				{34.20, -77.95}
				};
		
		double waypointsSquare2[][] = {
				{34.36, -77.95},
				{34.36, -77.82},
				{34.46, -77.82},
				{34.46, -77.95}
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1, true));
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare2, true));
		
		printPolygonOutput("Test Two Squares", boundingPolygons);
		System.err.println("");
		System.err.println("");
    }
	
	@Test
    public void testTwoSquares100Points()
    {
		double waypointsSquare1[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				{34.20, -77.82},
				{34.20, -77.95}
				};
		
		double waypointsSquare2[][] = {
				{34.36, -77.95},
				{34.36, -77.82},
				{34.46, -77.82},
				{34.46, -77.95}
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1, true));
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare2, true));
		
		printPolygonOutput("Test Two Squares 100 Points", boundingPolygons);
		
		for(int c = 0; c < 100; c++)
		{
		   printWaypoint(boundingPolygons.getInternalWaypoint());
		}
		
		System.err.println();
    }

	@Test
    public void testTwoSquaresWithOneInternalSquare()
    {
		double waypointsSquare1[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				{34.20, -77.82},
				{34.20, -77.95}
				};
		
		double waypointsSquare2[][] = {
				{34.36, -77.95},
				{34.36, -77.82},
				{34.46, -77.82},
				{34.46, -77.95}
				};
		
		double waypointsSquare3Internal[][] = {
				{34.38, -77.90},
				{34.38, -77.85},
				{34.44, -77.85},
				{34.44, -77.90}
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1, true));
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare2, true));
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare3Internal, true));
		
		printPolygonOutput("Test Two Squares with One Internal Square", boundingPolygons);
		System.err.println("");
		System.err.println("");
    }
	
	@Test(expected = RuntimeException.class)
    public void testTwoPointsInclusion()
    {
		System.err.println("Test Two Points Inclusion Zone, printPolygonOutput below won't run becasuse of the RuntimeException");
		System.err.println("");
		System.err.println("");
		System.err.println("");

		double waypointsSquare1[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1, true));
		
		printPolygonOutput("Test Two Points Inclusion Zone Throws Runtime Exception", boundingPolygons);
		System.err.println("");
		System.err.println("");
    }
	
	@Test(expected = RuntimeException.class)
    public void testTwoPointsExclusion()
    {
		System.err.println("Test Two Points Exclusion Zone, printPolygonOutput below won't run becasuse of the RuntimeException");
		System.err.println("");
		System.err.println("");
		System.err.println("");

		double waypointsSquare1[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		
		boundingPolygons.setExclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1, true));
		
		printPolygonOutput("Test Two Points Exclusion Zone Throws Runtime Exception", boundingPolygons);
		System.err.println("");
		System.err.println("");
    }
	
	@Test
	public void testThreePointsInALine()
    {
		double waypointsSquare1[][] = {
				{34.26, 77.95},
				{34.26, 77.82},
				{34.26, 77.60},
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1, true));
		
		printPolygonOutput("Test Three Points In a Line", boundingPolygons);
		System.err.println("");
		System.err.println("");
    }
	
	@Test
	public void testThreePointsInALineTwice()
    {
		System.err.println("Test Three Points In a Line Twice");
    
		System.err.println("");
		System.err.println("");
		System.err.println("");
		
		double waypointsSquare1[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				{34.26, -77.60},
				};
		
		
		double waypointsSquare2[][] = {
				{34.5, -77.95},
				{34.5, -77.82},
				{34.5, -77.60},
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1, true));
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare2, true));
		
		printPolygonOutput("Test Three Points In a Line twice", boundingPolygons);
		System.err.println("");
		System.err.println("");
    }
	
	@Test(expected = RuntimeException.class)
	public void testThreePointsInALineGeneratePoints()
    {
		System.err.println("Test Three Points In a Line Genereate Points, below won't print becasuse of the IllegalArgumentException");
		System.err.println("");
		System.err.println("");
		System.err.println("");
		double waypointsSquare1[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				{34.26, -77.60},
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		boundingPolygons.clearCurrentPolygons();
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1, true));
		
		printPolygonOutput("Test Three Points In a Line Genereate Points", boundingPolygons);

		for(int c = 0; c < 10; c++)
		{
		   printWaypoint(boundingPolygons.getInternalWaypoint());
		}
    }
	
	@Test
    public void testTwoSquaresWithOneInternalSquareWithInternalPoints()
    {
		double waypointsSquare1[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				{34.20, -77.82},
				{34.20, -77.95}
				};
		
		double waypointsSquare2[][] = {
				{34.36, -77.95},
				{34.36, -77.82},
				{34.46, -77.82},
				{34.46, -77.95}
				};
		
		double waypointsSquare3Internal[][] = {
				{34.38, -77.90},
				{34.38, -77.85},
				{34.44, -77.85},
				{34.44, -77.90}
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1, true));
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare2, true));
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare3Internal, true));
		
		printPolygonOutput("Test Two Squares with One Internal Square with Internal Points", boundingPolygons);
		
		for(int c = 0; c < 100; c++)
		{
		   printWaypoint(boundingPolygons.getInternalWaypoint());
		}
		
		System.err.println();
    }
	
	@Test
    public void testTwoSquaresWithOneInternalSquareWithForWhatEverReasonThe100RandomPointsDontWork()
    {
		double waypointsSquare1[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				{34.20, -77.82},
				{34.20, -77.95}
				};
		
		double waypointsSquare2[][] = {
				{34.36, -77.95},
				{34.36, -77.82},
				{34.46, -77.82},
				{34.46, -77.95}
				};
		
		double waypointsSquare3Internal[][] = {
				{34.38, -77.90},
				{34.38, -77.85},
				{34.44, -77.85},
				{34.44, -77.90}
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1, true));
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare2, true));
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare3Internal, true));
		
		printPolygonOutput("Test Two Squares with One Internal Square With For What Ever Reason The 100 Random Points Don't Work", boundingPolygons);
		
		printWaypoint(boundingPolygons.getAveragedWaypoint());
		
		System.err.println();
    }
	
	@Test
    public void testTwoSquaresWithOneInternalExclusionSquare()
    {
		double waypointsSquare1[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				{34.20, -77.82},
				{34.20, -77.95}
				};
		
		double waypointsSquare2[][] = {
				{34.36, -77.95},
				{34.36, -77.82},
				{34.46, -77.82},
				{34.46, -77.95}
				};
		
		double waypointsSquare1Exclusion[][] = {
				{34.38, -77.90},
				{34.38, -77.85},
				{34.44, -77.85},
				{34.44, -77.90}
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1, true));
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare2, true));
		boundingPolygons.setExclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1Exclusion, true));
		
		printPolygonOutput("Test Two Squares with One Internal Exclusion Square", boundingPolygons);
		
		Waypoint[] waypoints = boundingPolygons.getInternalWaypoints(100);
		
		for(Waypoint waypoint: waypoints)
		{
			printWaypoint(waypoint);
						
		}
		
		System.err.println();
    }
	
	@Test
    public void testTwoSquaresWithOneExclusionSquareOffset()
    {
		double waypointsSquare1[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				{34.20, -77.82},
				{34.20, -77.95}
				};
		
		double waypointsSquare2[][] = {
				{34.36, -77.95},
				{34.36, -77.82},
				{34.46, -77.82},
				{34.46, -77.95}
				};
		
		double waypointsSquare1Exclusion[][] = {
				{34.40, -77.85},
				{34.40, -77.98},
				{34.52, -77.98},
				{34.52, -77.85}
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1, true));
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare2, true));
		boundingPolygons.setExclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1Exclusion, true));
		
		printPolygonOutput("Test Two Squares with One Exclusion Square Offset", boundingPolygons);
		
		Waypoint[] waypoints = boundingPolygons.getInternalWaypoints(100);
		
		for(Waypoint waypoint: waypoints)
		{
			printWaypoint(waypoint);
						
		}
		
		System.err.println();
    }
	
	@Test
    public void testTwoSquaresWithTwoExclusionSquareOffset()
    {
		double waypointsSquare1[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				{34.20, -77.82},
				{34.20, -77.95}
				};
		
		double waypointsSquare2[][] = {
				{34.36, -77.95},
				{34.36, -77.82},
				{34.46, -77.82},
				{34.46, -77.95}
				};
		
		double waypointsSquare1Exclusion[][] = {
				{34.40, -77.85},
				{34.40, -77.98},
				{34.52, -77.98},
				{34.52, -77.85}
				};
		
		double waypointsSquare2Exclusion[][] = {
				{34.18, -77.85},
				{34.18, -77.98},
				{34.23, -77.98},
				{34.23, -77.85}
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1, true));
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare2, true));
		boundingPolygons.setExclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1Exclusion, true));
		boundingPolygons.setExclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare2Exclusion, true));
		
		printPolygonOutput("Test Two Squares with Two Exclusion Squares Offset", boundingPolygons);
		
		Waypoint[] waypoints = boundingPolygons.getInternalWaypoints(100);
		
		for(Waypoint waypoint: waypoints)
		{
			printWaypoint(waypoint);
						
		}
		
		
		
		System.err.println();
    }
	
	@Test
    public void testTwoSquaresWithLargeExclusion()
    {
		double waypointsSquare1[][] = {
				{34.26, -77.95},
				{34.26, -77.82},
				{34.20, -77.82},
				{34.20, -77.95}
				};
		
		double waypointsSquare3Exclusion[][] = {
				{90,    -180},
				{90,     180},
				{-90 ,   180},
				{-90 ,   -180}
				};
		
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare1, true));
		
		boundingPolygons.setExclusionPolygon(BoundingPolygons.setWaypoints(waypointsSquare3Exclusion, true));
		
		printPolygonOutput("Test Two Squares with Large Exclusion, all random point generation will fail and the center of the bounding boxes will be used to return points.", boundingPolygons);
		
		Waypoint[] waypoints = boundingPolygons.getInternalWaypoints(100);
		
		for(Waypoint waypoint: waypoints)
		{
			printWaypoint(waypoint);
						
		}
		
		
		
		System.err.println();
    }
	
	@Test
    public void testDefaultPolygon()
    {
		BoundingPolygons boundingPolygons = new BoundingPolygons();
		
		printPolygonOutput("Test Default Polygon", boundingPolygons);
		
		printWaypoint(boundingPolygons.getInternalWaypoint());
		
		System.err.println();
    }
	
	@Test
	public void testWillingtonWith100Waypoints()
	 {
		long startTime = System.currentTimeMillis();
		
			double waypointsWilmington1[][] = {
					{34.260, -77.950},
					{34.260, -77.820},
					{34.240, -77.770},
					{34.185, -77.812},
					{34.195, -77.830},
					{34.134, -77.868},
					{34.081, -77.885},
					{34.040, -77.890},
					{33.960, -77.930},
					{34.000, -77.919},
					{34.050, -77.920},
					{34.120, -77.927},
					{34.126, -77.914},
					{34.151, -77.937},
					{34.190, -77.954},
					{34.200, -77.955}
					};
			
			double waypointsWilmington2[][] = {
					{34.232, -77.981}, 
					{34.227, -77.954},
					{34.207, -77.965},
					{34.183, -77.964},
					{34.184, -77.967},
					{34.204, -77.971},
					{34.221, -77.975} 
					};
			
			double waypointsWilmington3[][] = {
					{34.130,-77.943}, 
					{34.128,-77.940}, 
					{34.120,-77.939},
					{34.117,-77.938}, 
					{34.112,-77.941}, 
					{34.116,-77.944}, 
					{34.117,-77.947}, 
					{34.122,-77.948}, 
					{34.124,-77.946}, 
					{34.123,-77.942}, 
					{34.128,-77.943}, 
					};
			
			BoundingPolygons boundingPolygons = new BoundingPolygons();
			boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsWilmington1, true));
			boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsWilmington2, true));
			boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsWilmington3, true));
			
			printPolygonOutput("test Willington With 100 Waypoints", boundingPolygons);
			
			Waypoint[] waypoints = boundingPolygons.getInternalWaypoints(100);
			
			for(Waypoint waypoint: waypoints)
			{
				printWaypoint(waypoint);
							
			}
			
			System.err.println("Elasped time: " + (System.currentTimeMillis() - startTime) + "ms");
			System.err.println();
	    }
	
	@Test
	public void testWillingtonWith1Waypoints()
	 {
		long startTime = System.currentTimeMillis();
		
			double waypointsWilmington1[][] = {
					{34.260, -77.950},
					{34.260, -77.820},
					{34.240, -77.770},
					{34.185, -77.812},
					{34.195, -77.830},
					{34.134, -77.868},
					{34.081, -77.885},
					{34.040, -77.890},
					{33.960, -77.930},
					{34.000, -77.919},
					{34.050, -77.920},
					{34.120, -77.927},
					{34.126, -77.914},
					{34.151, -77.937},
					{34.190, -77.954},
					{34.200, -77.955}
					};
			
			double waypointsWilmington2[][] = {
					{34.232, -77.981}, 
					{34.227, -77.954},
					{34.207, -77.965},
					{34.183, -77.964},
					{34.184, -77.967},
					{34.204, -77.971},
					{34.221, -77.975} 
					};
			
			double waypointsWilmington3[][] = {
					{34.130,-77.943}, 
					{34.128,-77.940}, 
					{34.120,-77.939},
					{34.117,-77.938}, 
					{34.112,-77.941}, 
					{34.116,-77.944}, 
					{34.117,-77.947}, 
					{34.122,-77.948}, 
					{34.124,-77.946}, 
					{34.123,-77.942}, 
					{34.128,-77.943}, 
					};
			
			BoundingPolygons boundingPolygons = new BoundingPolygons();
			boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsWilmington1, true));
			boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsWilmington2, true));
			boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsWilmington3, true));
			
			printPolygonOutput("test Willington With 1 Waypoints", boundingPolygons);
			
			Waypoint[] waypoints = boundingPolygons.getInternalWaypoints(1);
			
			for(Waypoint waypoint: waypoints)
			{
				printWaypoint(waypoint);
							
			}
			
			System.err.println("Elasped time: " + (System.currentTimeMillis() - startTime) + "ms");
			System.err.println();
	    }
	
	@Test
	public void testWillingtonWith1000Waypoints()
	 {
		long startTime = System.currentTimeMillis();
		
			double waypointsWilmington1[][] = {
					{34.260, -77.950},
					{34.260, -77.820},
					{34.240, -77.770},
					{34.185, -77.812},
					{34.195, -77.830},
					{34.134, -77.868},
					{34.081, -77.885},
					{34.040, -77.890},
					{33.960, -77.930},
					{34.000, -77.919},
					{34.050, -77.920},
					{34.120, -77.927},
					{34.126, -77.914},
					{34.151, -77.937},
					{34.190, -77.954},
					{34.200, -77.955}
					};
			
			double waypointsWilmington2[][] = {
					{34.232, -77.981}, 
					{34.227, -77.954},
					{34.207, -77.965},
					{34.183, -77.964},
					{34.184, -77.967},
					{34.204, -77.971},
					{34.221, -77.975} 
					};
			
			double waypointsWilmington3[][] = {
					{34.130,-77.943}, 
					{34.128,-77.940}, 
					{34.120,-77.939},
					{34.117,-77.938}, 
					{34.112,-77.941}, 
					{34.116,-77.944}, 
					{34.117,-77.947}, 
					{34.122,-77.948}, 
					{34.124,-77.946}, 
					{34.123,-77.942}, 
					{34.128,-77.943}, 
					};
			
			BoundingPolygons boundingPolygons = new BoundingPolygons();
			boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsWilmington1, true));
			boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsWilmington2, true));
			boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsWilmington3, true));
			
			printPolygonOutput("test Willington With 1000 Waypoints", boundingPolygons);
			
			Waypoint[] waypoints = boundingPolygons.getInternalWaypoints(1000);
			
			for(Waypoint waypoint: waypoints)
			{
				printWaypoint(waypoint);
							
			}
			
			System.err.println("Elasped time: " + (System.currentTimeMillis() - startTime) + "ms");
			System.err.println();
	    }
	
	@Test
	public void testWillingtonWith1000WaypointsLongitudeFirst()
	 {
		long startTime = System.currentTimeMillis();
		
			double waypointsWilmington1[][] = {
					{-77.950, 34.260},
					{-77.820, 34.260},
					{-77.770, 34.240},
					{-77.812, 34.185},
					{-77.830, 34.195},
					{-77.868, 34.134},
					{-77.885, 34.081},
					{-77.890, 34.040},
					{-77.930, 33.960},
					{-77.919, 34.000},
					{-77.920, 34.050},
					{-77.927, 34.120},
					{-77.914, 34.126},
					{-77.937, 34.151},
					{-77.954, 34.190},
					{-77.955, 34.200}
					};
			
			double waypointsWilmington2[][] = {
					{-77.981, 34.232, }, 
					{-77.954, 34.227, },
					{-77.965, 34.207, },
					{-77.964, 34.183, },
					{-77.967, 34.184, },
					{-77.971, 34.204, },
					{-77.975, 34.221, } 
					};
			
			double waypointsWilmington3[][] = {
					{-77.943, 34.130,}, 
					{-77.940, 34.128,}, 
					{-77.939, 34.120,},
					{-77.938, 34.117,}, 
					{-77.941, 34.112,}, 
					{-77.944, 34.116,}, 
					{-77.947, 34.117,}, 
					{-77.948, 34.122,}, 
					{-77.946, 34.124,}, 
					{-77.942, 34.123,}, 
					{-77.943, 34.128,}, 
					};
			
			BoundingPolygons boundingPolygons = new BoundingPolygons();
			boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsWilmington1, false));
			boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsWilmington2, false));
			boundingPolygons.setInclusionPolygon(BoundingPolygons.setWaypoints(waypointsWilmington3, false));
			
			printPolygonOutput("test Willington With 1000 Waypoints Longitude First", boundingPolygons);
			
			Waypoint[] waypoints = boundingPolygons.getInternalWaypoints(1000);
			
			for(Waypoint waypoint: waypoints)
			{
				printWaypoint(waypoint);
							
			}
			
			System.err.println("Elasped time: " + (System.currentTimeMillis() - startTime) + "ms");
			System.err.println();
	    }


	/**
	 * Helper method to print out some output so we can cut and paste things into the map
	 * @param message
	 * @param boundingPolygons
	 */
	private void printPolygonOutput(String message, BoundingPolygons boundingPolygons)
	{
		System.err.println(message);
		System.err.print(boundingPolygons);
	}
	
	private void printWaypoint(Waypoint waypoint)
	{
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(waypoint.getLatitude());
        stringBuilder.append("\t");
        stringBuilder.append(waypoint.getLongitude());
        stringBuilder.append("\t");
        stringBuilder.append("circle1");
        stringBuilder.append("\t");
        stringBuilder.append("yellow");
        stringBuilder.append("\t");
        stringBuilder.append(count);
        stringBuilder.append("\t");
        stringBuilder.append(count++);
        
        System.err.println(stringBuilder.toString());
	}

	
}
