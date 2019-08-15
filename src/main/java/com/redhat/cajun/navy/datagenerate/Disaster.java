package com.redhat.cajun.navy.datagenerate;

import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Disaster {

    private static GenerateFullNames fullNames = null;
    private static GenerateRandomPoints points = null;


    public Disaster(String fNameFile, String lNameFile){
        fullNames = new GenerateFullNames(fNameFile,lNameFile);
        points = new GenerateRandomPoints();
    }


    public Victim generateVictim(){
        Victim v = new Victim();
        v.setVictimName(fullNames.getNextFullName());

        Double point = points.getInternalPoint();
        v.setLatLon(point.getY(),point.getX());

        v.setVictimPhoneNumber(GeneratePhoneNumbers.getNextPhoneNumber());
        v.setNumberOfPeople(biasedRandom(1, 10, 1.3));
        v.setMedicalNeeded(new Random().nextBoolean());

        return v;
    }

    public List<Responder> generateResponders(int number) {
        List<Responder> responders = new ArrayList<>();
        for(int i=0; i<number; i++){
            responders.add(generateResponder());
        }
        return responders;
    }

    public Responder generateResponder() {
        Responder responder = new Responder();
        Double point = points.getInternalPoint();
        responder.setName(fullNames.getNextFullName());
        responder.setPhoneNumber(GeneratePhoneNumbers.getNextPhoneNumber());
        responder.setBoatCapacity(biasedRandom(1, 12, 0.5));
        responder.setMedicalKit(new Random().nextBoolean());
        responder.setLatitude(point.getY());
        responder.setLongitude(point.getX());
        responder.setEnrolled(true);
        responder.setPerson(false);
        responder.setAvailable(true);
        return responder;
    }


    public List<Victim> generateVictims(int number){
        List<Victim> victims = new ArrayList<Victim>();
        for(int i=0; i<number; i++)
            victims.add(generateVictim());
        return victims;
    }

    protected int biasedRandom(int min, int max, double bias) {
        double d = ThreadLocalRandom.current().nextDouble();
        double biased = Math.pow(d, bias);
        return (int) Math.round(min + (max-min)*biased);
    }
}
