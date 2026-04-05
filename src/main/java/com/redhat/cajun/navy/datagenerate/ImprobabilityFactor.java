package com.redhat.cajun.navy.datagenerate;

public class ImprobabilityFactor {
    private double externalExploitation = 0.0;
    private double internalUnrest = 0.0;
    private double increasedCrime = 0.0;
    private double decreasedResponseRatio = 0.0;

    public double getExternalExploitation() {
        return externalExploitation;
    }

    public void setExternalExploitation(double externalExploitation) {
        this.externalExploitation = externalExploitation;
    }

    public double getInternalUnrest() {
        return internalUnrest;
    }

    public void setInternalUnrest(double internalUnrest) {
        this.internalUnrest = internalUnrest;
    }

    public double getIncreasedCrime() {
        return increasedCrime;
    }

    public void setIncreasedCrime(double increasedCrime) {
        this.increasedCrime = increasedCrime;
    }

    public double getDecreasedResponseRatio() {
        return decreasedResponseRatio;
    }

    public void setDecreasedResponseRatio(double decreasedResponseRatio) {
        this.decreasedResponseRatio = decreasedResponseRatio;
    }
}
