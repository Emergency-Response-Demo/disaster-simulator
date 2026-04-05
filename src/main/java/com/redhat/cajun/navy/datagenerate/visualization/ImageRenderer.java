package com.redhat.cajun.navy.datagenerate.visualization;

import com.redhat.cajun.navy.datagenerate.BoundingPolygons;
import com.redhat.cajun.navy.datagenerate.Zone;
import com.redhat.cajun.navy.datagenerate.physics.CoordinateConverter;
import com.redhat.cajun.navy.datagenerate.physics.PhysicsSimulation;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import com.redhat.cajun.navy.datagenerate.Responder;
import com.redhat.cajun.navy.datagenerate.Victim;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageRenderer {

    private static Logger log = LoggerFactory.getLogger(ImageRenderer.class);
    private static final Logger log = LoggerFactory.getLogger(ImageRenderer.class);

    private int width = 1024;
    private int height = 1024;

    public void render(PhysicsSimulation sim, BoundingPolygons map, String filepath) {
        if (map.getInclusionZones().isEmpty()) {
            log.warn("No zones to render.");
            return;
        }

        CoordinateConverter converter = sim.getConverter();

        // Determine bounds of the map (in world coords)
        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for (Zone zone : map.getInclusionZones()) {
             PathIterator pi = zone.getPath().getPathIterator(null);
             double[] coords = new double[6];
             while (!pi.isDone()) {
                 int type = pi.currentSegment(coords);
                 if (type == PathIterator.SEG_MOVETO || type == PathIterator.SEG_LINETO) {
                     // coords[0] is Lon, coords[1] is Lat
                     Vector2 world = converter.toWorld(coords[1], coords[0]);
                     if (world.x < minX) minX = world.x;
                     if (world.x > maxX) maxX = world.x;
                     if (world.y < minY) minY = world.y;
                     if (world.y > maxY) maxY = world.y;
                 }
                 pi.next();
             }
        }

        // Add padding
        double rangeX = maxX - minX;
        double rangeY = maxY - minY;
        if (rangeX == 0) rangeX = 1000;
        if (rangeY == 0) rangeY = 1000;

        minX -= rangeX * 0.1;
        maxX += rangeX * 0.1;
        minY -= rangeY * 0.1;
        maxY += rangeY * 0.1;

        rangeX = maxX - minX;
        rangeY = maxY - minY;

        double scaleX = width / rangeX;
        double scaleY = height / rangeY;
        double scale = Math.min(scaleX, scaleY); // Uniform scale

        // Center the view
        double offsetX = (width - rangeX * scale) / 2.0;
        double offsetY = (height - rangeY * scale) / 2.0;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Draw Zones
        g2d.setColor(new Color(200, 255, 200)); // Light Green
        for (Zone zone : map.getInclusionZones()) {
             PathIterator pi = zone.getPath().getPathIterator(null);
             Path2D.Double pixelPath = new Path2D.Double();
             double[] coords = new double[6];
             while (!pi.isDone()) {
                 int type = pi.currentSegment(coords);
                 if (type == PathIterator.SEG_MOVETO || type == PathIterator.SEG_LINETO) {
                     Vector2 world = converter.toWorld(coords[1], coords[0]);
                     double px = (world.x - minX) * scale + offsetX;
                     double py = height - ((world.y - minY) * scale) - offsetY; // Flip Y
                     if (type == PathIterator.SEG_MOVETO) pixelPath.moveTo(px, py);
                     else pixelPath.lineTo(px, py);
                 } else if (type == PathIterator.SEG_CLOSE) {
                     pixelPath.closePath();
                 }
                 pi.next();
             }
             g2d.fill(pixelPath);
             g2d.setColor(Color.GRAY);
             g2d.draw(pixelPath);
        }

        // Draw Bodies
        for (Object obj : sim.getWorld().getBodies()) {
            Body body = (Body) obj;
            Vector2 pos = body.getTransform().getTranslation();
            double px = (pos.x - minX) * scale + offsetX;
            double py = height - ((pos.y - minY) * scale) - offsetY;

            Object userData = body.getUserData();
            if (userData instanceof Victim) {
                g2d.setColor(Color.RED);
                g2d.fillOval((int)(px - 3), (int)(py - 3), 6, 6);
            } else if (userData instanceof Responder) {
                g2d.setColor(Color.BLUE);
                g2d.fillOval((int)(px - 4), (int)(py - 4), 8, 8);
            }
        }

        g2d.dispose();
        try {
            ImageIO.write(image, "PNG", new File(filepath));
        } catch (IOException e) {
            log.error("Failed to write image", e);
            log.error("Failed to write image to " + filepath, e);
        }
    }
}
