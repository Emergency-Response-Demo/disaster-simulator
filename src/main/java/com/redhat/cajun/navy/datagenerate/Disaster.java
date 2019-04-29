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
        v.setNumberOfPeople(new Random().nextInt(10));
        v.setMedicalNeeded(new Random().nextBoolean());

        return v;
    }


    public List<Victim> generateVictims(int number){
        List<Victim> victims = new ArrayList<Victim>();
        for(int i=0; i<number; i++)
            victims.add(generateVictim());
        return victims;
    }

}
