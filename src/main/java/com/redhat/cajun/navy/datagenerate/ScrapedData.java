package com.redhat.cajun.navy.datagenerate;

public class ScrapedData {
    private double avgPeoplePerHousehold;
    private double medicalNeedRate;
    private double responderDensity;
    private double regionalRiskFactor;
    private ImprobabilityFactor improbabilityFactor;

    public double getAvgPeoplePerHousehold() {
        return avgPeoplePerHousehold;
    }

    public void setAvgPeoplePerHousehold(double avgPeoplePerHousehold) {
        this.avgPeoplePerHousehold = avgPeoplePerHousehold;
    }

    public double getMedicalNeedRate() {
        return medicalNeedRate;
    }

    public void setMedicalNeedRate(double medicalNeedRate) {
        this.medicalNeedRate = medicalNeedRate;
    }

    public double getResponderDensity() {
        return responderDensity;
    }

    public void setResponderDensity(double responderDensity) {
        this.responderDensity = responderDensity;
    }

    public double getRegionalRiskFactor() {
        return regionalRiskFactor;
    }

    public void setRegionalRiskFactor(double regionalRiskFactor) {
        this.regionalRiskFactor = regionalRiskFactor;
    }

    public ImprobabilityFactor getImprobabilityFactor() {
        return improbabilityFactor;
    }

    public void setImprobabilityFactor(ImprobabilityFactor improbabilityFactor) {
        this.improbabilityFactor = improbabilityFactor;
    }
}
