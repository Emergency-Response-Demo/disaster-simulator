package com.redhat.cajun.navy.datagenerate;

import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Disaster {

    private static GenerateFullNames fullNames = new GenerateFullNames("/opt/github.com/NAPS/disaster-simulator/src/main/resources/FNames.txt", "/opt/github.com/NAPS/disaster-simulator/src/main/resources/LNames.txt");
    private static GenerateRandomPoints points = new GenerateRandomPoints();


    public Victim generateVictim(){
        Victim v = new Victim();
        v.setVictimName(fullNames.getNextFullName());

        Double point = points.getInternalPoint();
        v.setLatLon(point.getX(),point.getY());

        v.setVictimPhoneNumber(GeneratePhoneNumbers.getNextPhoneNumber());
        v.setNumberOfPeople(new Random().nextInt(10));
        v.setStatus("REQUESTED");
        v.setTimestamp(System.currentTimeMillis()+"");

        return v;
    }


    public List<Victim> generateVictims(int number){
        List<Victim> victims = new ArrayList<Victim>();
        for(int i=0; i<number; i++)
            victims.add(generateVictim());
        return victims;
    }

}
