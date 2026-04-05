package com.redhat.cajun.navy.datagenerate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BenchmarkPolygonProcessing {

    public static void main(String[] args) {
        int numPoints = 10000;
        int numIterations = 1000;
        int warmupIterations = 500;

        List<double[]> points = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            points.add(new double[]{ 0.0, 0.0 });
        }

        System.out.println("Warmup...");
        for (int i = 0; i < warmupIterations; i++) {
            runCurrent(points);
            runOptimized(points);
        }

        for (int run = 0; run < 5; run++) {
            // Benchmark current
            long startCurrent = System.nanoTime();
            for (int i = 0; i < numIterations; i++) {
                runCurrent(points);
            }
            long endCurrent = System.nanoTime();
            long durationCurrent = endCurrent - startCurrent;

            // Benchmark optimized
            long startOptimized = System.nanoTime();
            for (int i = 0; i < numIterations; i++) {
                runOptimized(points);
            }
            long endOptimized = System.nanoTime();
            long durationOptimized = endOptimized - startOptimized;

            System.out.println("Run " + (run + 1) + ":");
            System.out.println("  Current implementation average time: " + (durationCurrent / numIterations) + " ns");
            System.out.println("  Optimized implementation average time: " + (durationOptimized / numIterations) + " ns");
            System.out.println("  Improvement: " + String.format("%.2f", (double) (durationCurrent - durationOptimized) / durationCurrent * 100) + "%");
        }
    }

    private static Waypoint[] runCurrent(List<double[]> points) {
        Waypoint waypoints[] = new Waypoint[points.size()];
        points.stream()
            .map(point -> new Waypoint(point[1], point[0]))
            .collect(Collectors.toList())
            .toArray(waypoints);
        return waypoints;
    }

    private static Waypoint[] runOptimized(List<double[]> points) {
        return points.stream()
            .map(point -> new Waypoint(point[1], point[0]))
            .toArray(Waypoint[]::new);
    }
}
