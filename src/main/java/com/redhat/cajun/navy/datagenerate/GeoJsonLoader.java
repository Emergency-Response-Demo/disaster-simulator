package com.redhat.cajun.navy.datagenerate;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.awt.geom.Path2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GeoJsonLoader {

    private static Logger log = LoggerFactory.getLogger(GeoJsonLoader.class);

    public List<Zone> load(String filePath) {
        List<Zone> zones = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonObject json = new JsonObject(content);
            JsonArray features = json.getJsonArray("features");

            if (features != null) {
                for (int i = 0; i < features.size(); i++) {
                    JsonObject feature = features.getJsonObject(i);
                    JsonObject geometry = feature.getJsonObject("geometry");
                    JsonObject properties = feature.getJsonObject("properties");
                    double weight = 1.0;

                    if (properties != null && properties.containsKey("pop_density")) {
                        weight = properties.getDouble("pop_density");
                    } else if (properties != null && properties.containsKey("weight")) {
                        weight = properties.getDouble("weight");
                    }

                    if (geometry != null) {
                        String type = geometry.getString("type");
                        JsonArray coordinates = geometry.getJsonArray("coordinates");

                        if ("Polygon".equalsIgnoreCase(type)) {
                            zones.add(createZoneFromPolygon(coordinates, weight));
                        } else if ("MultiPolygon".equalsIgnoreCase(type)) {
                            for (int j = 0; j < coordinates.size(); j++) {
                                zones.add(createZoneFromPolygon(coordinates.getJsonArray(j), weight));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error reading GeoJSON file: " + e.getMessage());
        }
        return zones;
    }

    private Zone createZoneFromPolygon(JsonArray rings, double weight) {
        Path2D.Double path = new Path2D.Double();
        // GeoJSON Polygons are array of linear rings. First is exterior, others are holes.
        // We will just assume the first ring for the inclusion zone path for simplicity,
        // or treat holes as exclusions later if needed. For now, Path2D handles winding rules.

        if (rings.size() > 0) {
            JsonArray exteriorRing = rings.getJsonArray(0);
            for (int k = 0; k < exteriorRing.size(); k++) {
                JsonArray coord = exteriorRing.getJsonArray(k);
                double lon = coord.getDouble(0);
                double lat = coord.getDouble(1);
                if (k == 0) {
                    path.moveTo(lon, lat); // GeoJSON is [lon, lat], we use X=lon, Y=lat
                } else {
                    path.lineTo(lon, lat);
                }
            }
            path.closePath();

            // Handle holes? Path2D supports them if we append them.
            for (int h = 1; h < rings.size(); h++) {
                JsonArray holeRing = rings.getJsonArray(h);
                if (holeRing.size() > 0) {
                    JsonArray start = holeRing.getJsonArray(0);
                    path.moveTo(start.getDouble(0), start.getDouble(1));
                    for (int k = 1; k < holeRing.size(); k++) {
                        JsonArray coord = holeRing.getJsonArray(k);
                        path.lineTo(coord.getDouble(0), coord.getDouble(1));
                    }
                    path.closePath();
                }
            }
        }
        return new Zone(path, weight);
    }
}
