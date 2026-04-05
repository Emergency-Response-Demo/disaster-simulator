package com.redhat.cajun.navy.datagenerate;

import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Path2D;

public class BenchmarkSelectWeightedZone {

    public static void main(String[] args) {
        int numZones = 1000;
        int numCalls = 100000;
        int warmupIterations = 10000;

        BoundingPolygons bp = new BoundingPolygons();
        bp.clearCurrentPolygons();

        List<Zone> zones = new ArrayList<>();
        for (int i = 0; i < numZones; i++) {
            Path2D.Double path = new Path2D.Double();
            path.moveTo(0, 0);
            path.lineTo(1, 0);
            path.lineTo(1, 1);
            path.closePath();
            zones.add(new Zone(path, 1.0));
        }
        bp.setInclusionZones(zones);

        // We need to measure selectWeightedZone. Since it's private,
        // for baseline we will just call a method that uses it,
        // but we want to avoid the random point generation overhead if possible.
        // Actually, let's just make it package-private in the real code temporarily
        // or use reflection.

        System.out.println("Warmup...");
        for (int i = 0; i < warmupIterations; i++) {
            bp.getInternalWaypoint();
        }

        System.out.println("Benchmarking...");
        long start = System.nanoTime();
        for (int i = 0; i < numCalls; i++) {
            bp.getInternalWaypoint();
        }
        long end = System.nanoTime();

        long duration = end - start;
        System.out.println("Total time for " + numCalls + " getInternalWaypoint calls with " + numZones + " zones: " + duration / 1000000 + " ms");
        System.out.println("Average time per call: " + (duration / numCalls) + " ns");
    }
}
