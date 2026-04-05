package com.redhat.cajun.navy.datagenerate.physics;

import com.redhat.cajun.navy.datagenerate.BoundingPolygons;
import com.redhat.cajun.navy.datagenerate.Responder;
import com.redhat.cajun.navy.datagenerate.Victim;
import com.redhat.cajun.navy.datagenerate.Waypoint;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.world.World;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.security.SecureRandom;

public class PhysicsSimulation {

    private static final SecureRandom RANDOM = new SecureRandom();
    private World world;
    private CoordinateConverter converter;

    public void init(BoundingPolygons map) {
        this.world = new World();
        this.world.setGravity(World.ZERO_GRAVITY);
        this.converter = new CoordinateConverter(map);
    }

    public void addVictim(Victim v) {
        Body body = new Body();
        body.addFixture(new BodyFixture(new Circle(5.0))); // 5m radius
        body.setMass(MassType.INFINITE); // Static

        Vector2 pos = converter.toWorld(v.getLat(), v.getLon());
        body.translate(pos);
        body.setUserData(v);

        this.world.addBody(body);
    }

    public void addResponder(Responder r) {
        Body body = new Body();
        body.addFixture(new BodyFixture(new Circle(10.0))); // 10m radius
        body.setMass(MassType.NORMAL);

        Vector2 pos = converter.toWorld(r.getLatitude(), r.getLongitude());
        body.translate(pos);
        body.setUserData(r);

        // Velocity
        double speedKmH = r.getSpeed();
        double speedMS = speedKmH / 3.6;
        double angle = RANDOM.nextDouble() * 2 * Math.PI;

        Vector2 velocity = new Vector2(speedMS * Math.cos(angle), speedMS * Math.sin(angle));
        body.setLinearVelocity(velocity);
        body.setLinearDamping(0.0); // Keep moving

        this.world.addBody(body);
    }

    public void step(double dt) {
        this.world.update(dt);
    }

    public void updateEntities() {
        // Cast to list of Body (or iterate object) to handle raw type or generic mismatch
        for (Object obj : this.world.getBodies()) {
            Body b = (Body) obj;
            Object userData = b.getUserData();
            if (userData instanceof Responder) {
                Responder r = (Responder) userData;
                Waypoint w = converter.toGeo(b.getTransform().getTranslation());
                r.setLatitude(w.getLatitude());
                r.setLongitude(w.getLongitude());
            } else if (userData instanceof Victim) {
                // Victims are static
            }
        }
    }

    public World getWorld() {
        return world;
    }

    public CoordinateConverter getConverter() {
        return converter;
    }
}
