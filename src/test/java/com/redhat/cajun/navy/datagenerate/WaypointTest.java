package com.redhat.cajun.navy.datagenerate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class WaypointTest {

    @Test
    public void testSetLongitudeInvalidValue() {
        Waypoint waypoint = new Waypoint(0, 0);
        try {
            waypoint.setLongitude(181);
            fail("Should have thrown a RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Longitude values must be between -180 and 180 inclusive", e.getMessage());
        }
    }

    @Test
    public void testSetLatitudeInvalidValue() {
        Waypoint waypoint = new Waypoint(0, 0);
        try {
            waypoint.setLatitude(91);
            fail("Should have thrown a RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Latitude values must be between -90 and 90 inclusive", e.getMessage());
        }
    }
}
