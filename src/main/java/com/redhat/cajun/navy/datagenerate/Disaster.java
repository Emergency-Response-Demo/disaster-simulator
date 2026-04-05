package com.redhat.cajun.navy.datagenerate;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class Disaster {
    private static final Logger log = LoggerFactory.getLogger(Disaster.class);

    private static GenerateFullNames fullNames = null;
    public BoundingPolygons boundingPolygons = new BoundingPolygons();
    private Random random = new SecureRandom();

    // Simulation Parameters with defaults
    private int minPeople = 1;
    private int maxPeople = 10;
    private double peopleBias = 1.3;
    private double medicalNeededProb = 0.5;
    private int minBoatCapacity = 1;
    private int maxBoatCapacity = 12;
    private double boatCapacityBias = 0.5;
    private double medicalKitProb = 0.5;

    // Elaborate Simulation Parameters
    private double pregnantProb = 0.1;
    private double consciousProb = 0.9;
    private double mobilityAmbulatoryProb = 0.7;
    private double mobilityAssistedProb = 0.2;
    // (Implied mobilityNonAmbulatoryProb = 1 - 0.7 - 0.2 = 0.1)

    private double responderBoatProb = 0.8;
    private double responderHelicopterProb = 0.05;
    // (Implied responderGroundProb = 1 - 0.8 - 0.05 = 0.15)


    public Disaster(String fNameFile, String lNameFile){
        fullNames = new GenerateFullNames(fNameFile,lNameFile);
    }

    public void setSimulationParameters(int minPeople, int maxPeople, double peopleBias, double medicalNeededProb,
                                        int minBoatCapacity, int maxBoatCapacity, double boatCapacityBias, double medicalKitProb) {
        this.minPeople = minPeople;
        this.maxPeople = maxPeople;
        this.peopleBias = peopleBias;
        this.medicalNeededProb = medicalNeededProb;
        this.minBoatCapacity = minBoatCapacity;
        this.maxBoatCapacity = maxBoatCapacity;
        this.boatCapacityBias = boatCapacityBias;
        this.medicalKitProb = medicalKitProb;
    }

    public void setElaborateSimulationParameters(double pregnantProb, double consciousProb,
                                                 double mobilityAmbulatoryProb, double mobilityAssistedProb,
                                                 double responderBoatProb, double responderHelicopterProb) {
        this.pregnantProb = pregnantProb;
        this.consciousProb = consciousProb;
        this.mobilityAmbulatoryProb = mobilityAmbulatoryProb;
        this.mobilityAssistedProb = mobilityAssistedProb;
        this.responderBoatProb = responderBoatProb;
        this.responderHelicopterProb = responderHelicopterProb;
    }

    public void applyScrapedData(ScrapedData data) {
        if (data == null) return;

        // Adjust people count based on average household size
        int avg = (int) Math.round(data.getAvgPeoplePerHousehold());
        this.minPeople = Math.max(1, avg - 1);
        this.maxPeople = avg + 2;

        // Adjust medical need probability
        this.medicalNeededProb = data.getMedicalNeedRate();

        // Adjust responder availability/capacity based on density
        if (data.getResponderDensity() < 0.05) {
            // Low density, maybe fewer boats or smaller capacity
            this.maxBoatCapacity = Math.max(1, this.maxBoatCapacity - 2);
        } else if (data.getResponderDensity() > 0.1) {
            // High density, more resources
            this.maxBoatCapacity += 2;
        }

        // Adjust risk factors based on regional risk
        if (data.getRegionalRiskFactor() > 1.5) {
            this.consciousProb *= 0.8; // More likely to be unconscious
            this.mobilityAmbulatoryProb *= 0.8; // More mobility issues
        }

        if (data.getImprobabilityFactor() != null) {
            ImprobabilityFactor factor = data.getImprobabilityFactor();
            if (factor.getExternalExploitation() > 0.5) {
                this.responderBoatProb *= 0.5; // Reduced local resources due to exploitation
                this.maxBoatCapacity = Math.max(1, (int)(this.maxBoatCapacity * 0.7));
            }
            if (factor.getInternalUnrest() > 0.5) {
                this.medicalNeededProb = Math.min(1.0, this.medicalNeededProb * 1.5);
                this.consciousProb *= 0.7; // Violence leads to more trauma
            }
            if (factor.getIncreasedCrime() > 0.5) {
                // Responders are slower due to caution
                // Note: Speed is dynamic in generateResponder, but we can reflect this by reducing density or capacity
                this.boatCapacityBias = Math.max(0.1, this.boatCapacityBias - 0.2);
            }
            if (factor.getDecreasedResponseRatio() > 0.0) {
                // Direct impact on availability logic if we had it, for now we can simulate by making responders less effective
                this.maxBoatCapacity = Math.max(1, (int)(this.maxBoatCapacity * (1.0 - factor.getDecreasedResponseRatio())));
            }
        }

        log.info("Applied scraped data: AvgPeople=" + avg + ", MedProb=" + medicalNeededProb +
                 ", MaxBoat=" + maxBoatCapacity + ", ConsciousProb=" + consciousProb);
    }


    public Victim generateVictim(){
        Victim v = new Victim();
        v.setVictimName(fullNames.getNextFullName());

        Waypoint point = boundingPolygons.getInternalWaypoint();
        v.setLatLon(point.getY(),point.getX());

        v.setVictimPhoneNumber(GeneratePhoneNumbers.getNextPhoneNumber());
        v.setNumberOfPeople(biasedRandom(minPeople, maxPeople, peopleBias));

        // Elaborate generation logic
        int age = random.nextInt(100);
        v.setAge(age);
        v.setGender(random.nextBoolean() ? "M" : "F");

        boolean isPregnant = false;
        if(v.getGender().equals("F") && age >= 18 && age <= 45) {
             isPregnant = random.nextDouble() < pregnantProb;
        }
        v.setPregnant(isPregnant);

        boolean isConscious = random.nextDouble() < consciousProb;
        v.setConscious(isConscious);

        double mobilityRoll = random.nextDouble();
        if (mobilityRoll < mobilityAmbulatoryProb) {
            v.setMobility("AMBULATORY");
        } else if (mobilityRoll < mobilityAmbulatoryProb + mobilityAssistedProb) {
            v.setMobility("ASSISTED");
        } else {
            v.setMobility("NON_AMBULATORY");
        }

        // Correlate medical needed with other factors
        boolean baseMedical = random.nextDouble() < medicalNeededProb;
        if (!isConscious || v.getMobility().equals("NON_AMBULATORY") || age > 80 || isPregnant) {
            baseMedical = true; // High risk factors force medical needed
        }
        v.setMedicalNeeded(baseMedical);

        v.setPriority(calculatePriority(v));

        return v;
    }

    private int calculatePriority(Victim v) {
        int score = 0;
        if (!v.isConscious()) score += 50;
        if (v.isMedicalNeeded()) score += 30;
        if (v.isPregnant()) score += 20;
        if (v.getAge() < 5 || v.getAge() > 75) score += 10;
        if (v.getMobility().equals("NON_AMBULATORY")) score += 15;
        if (v.getMobility().equals("ASSISTED")) score += 5;
        return score;
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
        Waypoint point = boundingPolygons.getInternalWaypoint();
        responder.setName(fullNames.getNextFullName());
        responder.setPhoneNumber(GeneratePhoneNumbers.getNextPhoneNumber());
        responder.setMedicalKit(random.nextDouble() < medicalKitProb);
        responder.setLatitude(point.getY());
        responder.setLongitude(point.getX());
        responder.setEnrolled(true);
        responder.setPerson(false);
        responder.setAvailable(true);

        double typeRoll = random.nextDouble();
        if (typeRoll < responderBoatProb) {
            responder.setType("BOAT");
            responder.setSpeed(10.0 + random.nextDouble() * 20.0); // 10-30 km/h
            responder.setBoatCapacity(biasedRandom(minBoatCapacity, maxBoatCapacity, boatCapacityBias));
        } else if (typeRoll < responderBoatProb + responderHelicopterProb) {
            responder.setType("HELICOPTER");
            responder.setSpeed(150.0 + random.nextDouble() * 100.0); // 150-250 km/h
            responder.setBoatCapacity(biasedRandom(1, 5, 0.5)); // Smaller capacity usually
        } else {
            responder.setType("GROUND");
            responder.setSpeed(30.0 + random.nextDouble() * 50.0); // 30-80 km/h
            responder.setBoatCapacity(biasedRandom(2, 6, 0.5)); // Car/Ambulance capacity
        }

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
