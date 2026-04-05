package com.redhat.cajun.navy.datagenerate.physics;

import com.redhat.cajun.navy.datagenerate.BoundingPolygons;
import com.redhat.cajun.navy.datagenerate.Waypoint;
import com.redhat.cajun.navy.datagenerate.Zone;
import org.dyn4j.geometry.Vector2;

import java.awt.geom.Rectangle2D;
import java.util.List;

public class CoordinateConverter {

    private double centerLat;
    private double centerLon;
    private static final double R = 6371000.0; // Earth radius in meters

    public CoordinateConverter(BoundingPolygons map) {
        List<Zone> zones = map.getInclusionZones();
        if (zones == null || zones.isEmpty()) {
            this.centerLat = 0;
            this.centerLon = 0;
        } else {
            Rectangle2D bounds = zones.get(0).getBounds();
            this.centerLat = bounds.getCenterY();
            this.centerLon = bounds.getCenterX();
        }
    }

    // lat/lon in degrees
    public Vector2 toWorld(double lat, double lon) {
        double x = R * Math.toRadians(lon - centerLon) * Math.cos(Math.toRadians(centerLat));
        double y = R * Math.toRadians(lat - centerLat);
        return new Vector2(x, y);
    }

    public Waypoint toGeo(Vector2 world) {
        double latRad = world.y / R + Math.toRadians(centerLat);
        double lonRad = world.x / (R * Math.cos(Math.toRadians(centerLat))) + Math.toRadians(centerLon);

        return new Waypoint(Math.toDegrees(latRad), Math.toDegrees(lonRad));
    }
}
