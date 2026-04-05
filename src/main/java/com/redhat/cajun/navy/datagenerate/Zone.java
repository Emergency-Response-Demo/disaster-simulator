package com.redhat.cajun.navy.datagenerate;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class Zone {
    private Path2D.Double path;
    private Rectangle2D bounds;
    private double weight; // Population density or risk factor

    public Zone(Path2D.Double path, double weight) {
        this.path = path;
        this.weight = weight;
        this.bounds = path.getBounds2D();
    }

    public Path2D.Double getPath() {
        return path;
    }

    public Rectangle2D getBounds() {
        return bounds;
    }

    public double getWeight() {
        return weight;
    }

    public boolean contains(double x, double y) {
        return path.contains(x, y);
    }
}
