package com.redhat.cajun.navy.datagenerate;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class DisasterTest {

    private Disaster disaster;

    @Before
    public void setUp() {
        // Resources are expected to be in src/test/resources/
        String fNameFile = "src/test/resources/FNames.txt";
        String lNameFile = "src/test/resources/LNames.txt";

        // Fallback to absolute path if relative doesn't work (for some IDEs)
        if (!new File(fNameFile).exists()) {
            fNameFile = new File(fNameFile).getAbsolutePath();
            lNameFile = new File(lNameFile).getAbsolutePath();
        }

        disaster = new Disaster(fNameFile, lNameFile);
    }

    @Test
    public void testGenerateVictim() {
        Victim victim = disaster.generateVictim();
        assertThat(victim).isNotNull();
        assertThat(victim.getVictimName()).isNotNull().isNotEmpty();
        assertThat(victim.getVictimPhoneNumber()).isNotNull().isNotEmpty();
        assertThat(victim.getPriority()).isGreaterThanOrEqualTo(0);
        assertThat(victim.getLatitude()).isNotZero();
        assertThat(victim.getLongitude()).isNotZero();
    }

    @Test
    public void testGenerateResponder() {
        Responder responder = disaster.generateResponder();
        assertThat(responder).isNotNull();
        assertThat(responder.getName()).isNotNull().isNotEmpty();
        assertThat(responder.getType()).isIn("BOAT", "HELICOPTER", "GROUND");
        assertThat(responder.isAvailable()).isTrue();
        assertThat(responder.isEnrolled()).isTrue();
        assertThat(responder.getLatitude()).isNotZero();
        assertThat(responder.getLongitude()).isNotZero();
    }

    @Test
    public void testApplyScrapedData() {
        ScrapedData data = new ScrapedData();
        data.setAvgPeoplePerHousehold(5.0);
        data.setMedicalNeedRate(0.8);
        data.setResponderDensity(0.2);
        data.setRegionalRiskFactor(1.0);

        disaster.applyScrapedData(data);

        Victim victim = disaster.generateVictim();
        // minPeople = 4, maxPeople = 7
        assertThat(victim.getNumberOfPeople()).isBetween(4, 7);
    }

    @Test
    public void testGenerateVictimsAndResponders() {
        List<Victim> victims = disaster.generateVictims(10);
        assertThat(victims).hasSize(10);
        assertThat(victims).allSatisfy(v -> assertThat(v).isNotNull());

        List<Responder> responders = disaster.generateResponders(5);
        assertThat(responders).hasSize(5);
        assertThat(responders).allSatisfy(r -> assertThat(r).isNotNull());
    }

    @Test
    public void testBiasedRandom() {
        for (int i = 0; i < 100; i++) {
            int val = disaster.biasedRandom(1, 10, 1.3);
            assertThat(val).isBetween(1, 10);
        }
    }

    @Test
    public void testPriorityCalculationLogic() {
        boolean foundUnconscious = false;
        boolean foundPregnant = false;

        for (int i = 0; i < 1000; i++) {
            Victim v = disaster.generateVictim();
            int expectedPriority = 0;
            if (!v.isConscious()) expectedPriority += 50;
            if (v.isMedicalNeeded()) expectedPriority += 30;
            if (v.isPregnant()) expectedPriority += 20;
            if (v.getAge() < 5 || v.getAge() > 75) expectedPriority += 10;
            if ("NON_AMBULATORY".equals(v.getMobility())) expectedPriority += 15;
            if ("ASSISTED".equals(v.getMobility())) expectedPriority += 5;

            assertThat(v.getPriority()).isEqualTo(expectedPriority);

            if (!v.isConscious()) foundUnconscious = true;
            if (v.isPregnant()) foundPregnant = true;
        }
        assertThat(foundUnconscious).describedAs("Should have generated at least one unconscious victim in 1000 trials").isTrue();
        assertThat(foundPregnant).describedAs("Should have generated at least one pregnant victim in 1000 trials").isTrue();
    }

}
