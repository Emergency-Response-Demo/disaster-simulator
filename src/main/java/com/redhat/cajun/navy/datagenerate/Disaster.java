package com.redhat.cajun.navy.datagenerate;

import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        v.setNumberOfPeople(new Random().nextInt(10) + 1);
        v.setMedicalNeeded(new Random().nextBoolean());

        return v;
    }

    public List<Responder> generateResponder(long number){
        List<Responder> responders = new ArrayList<>();
        for(int i=0; i<number; i++){
            responders.add(generateResponder());
        }
        return responders;
    }

    public Responder generateResponder(){
        Responder responder = new Responder();
        Double point = points.getInternalPoint();
        responder.setName(fullNames.getNextFullName());
        responder.setPhoneNumber(GeneratePhoneNumbers.getNextPhoneNumber());
        responder.setBoatCapacity(new Random().nextInt(12) + 1);
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

    public Responder generateResponder(int id) {
        Responder responder = new Responder();
        Double point = points.getInternalPoint();
        responder.setId(Integer.toString(id));
        responder.setLatitude(point.getY());
        responder.setLongitude(point.getX());
        responder.setEnrolled(true);
        return responder;
    }

    public List<Responder> generateResponders(int number) {
        List<Responder> responders = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            responders.add(generateResponder(i));
        }
        return responders;
    }

}
