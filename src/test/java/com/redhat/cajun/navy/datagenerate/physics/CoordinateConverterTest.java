package com.redhat.cajun.navy.datagenerate.physics;

import com.redhat.cajun.navy.datagenerate.BoundingPolygons;
import com.redhat.cajun.navy.datagenerate.Waypoint;
import org.dyn4j.geometry.Vector2;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoordinateConverterTest {

    private static final double DELTA = 1e-6;

    @Test
    public void testToWorldCenter() {
        BoundingPolygons map = new BoundingPolygons();
        CoordinateConverter converter = new CoordinateConverter(map);

        // Default center for Wilmington map is (34.225, -77.94)
        Vector2 world = converter.toWorld(34.225, -77.94);
        assertEquals(0.0, world.x, DELTA);
        assertEquals(0.0, world.y, DELTA);
    }

    @Test
    public void testToGeoCenter() {
        BoundingPolygons map = new BoundingPolygons();
        CoordinateConverter converter = new CoordinateConverter(map);

        Waypoint geo = converter.toGeo(new Vector2(0, 0));
        assertEquals(34.225, geo.getLatitude(), DELTA);
        assertEquals(-77.94, geo.getLongitude(), DELTA);
    }

    @Test
    public void testRoundTrip() {
        BoundingPolygons map = new BoundingPolygons();
        CoordinateConverter converter = new CoordinateConverter(map);

        double lat = 34.21;
        double lon = -77.95;

        Vector2 world = converter.toWorld(lat, lon);
        Waypoint geo = converter.toGeo(world);

        assertEquals(lat, geo.getLatitude(), DELTA);
        assertEquals(lon, geo.getLongitude(), DELTA);
    }

    @Test
    public void testToWorldNorth() {
        BoundingPolygons map = new BoundingPolygons();
        CoordinateConverter converter = new CoordinateConverter(map);

        double centerLat = 34.225;
        double centerLon = -77.94;
        double R = 6371000.0;

        // 0.01 degrees north
        Vector2 world = converter.toWorld(centerLat + 0.01, centerLon);

        assertEquals(0.0, world.x, DELTA);
        assertEquals(R * Math.toRadians(0.01), world.y, DELTA);
    }

    @Test
    public void testToWorldEast() {
        BoundingPolygons map = new BoundingPolygons();
        CoordinateConverter converter = new CoordinateConverter(map);

        double centerLat = 34.225;
        double centerLon = -77.94;
        double R = 6371000.0;

        // 0.01 degrees east
        Vector2 world = converter.toWorld(centerLat, centerLon + 0.01);

        double expectedX = R * Math.toRadians(0.01) * Math.cos(Math.toRadians(centerLat));
        assertEquals(expectedX, world.x, DELTA);
        assertEquals(0.0, world.y, DELTA);
    }

    @Test
    public void testNoZones() {
        BoundingPolygons map = new BoundingPolygons();
        map.clearCurrentPolygons();
        CoordinateConverter converter = new CoordinateConverter(map);

        // Should default to center (0,0)
        Vector2 world = converter.toWorld(0, 0);
        assertEquals(0.0, world.x, DELTA);
        assertEquals(0.0, world.y, DELTA);

        Waypoint geo = converter.toGeo(new Vector2(100, 100));
        // world.y / R + 0 = 100 / 6371000
        double expectedLat = Math.toDegrees(100.0 / 6371000.0);
        assertEquals(expectedLat, geo.getLatitude(), DELTA);
    }
}
