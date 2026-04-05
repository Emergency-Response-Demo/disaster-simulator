package com.redhat.cajun.navy.datagenerate;

import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataScraper {

    private static Logger log = LoggerFactory.getLogger(DataScraper.class);

    public ScrapedData scrape(String filePath) {
        if (filePath == null) {
            return null;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JsonObject json = new JsonObject(content);

            ScrapedData data = new ScrapedData();
            data.setAvgPeoplePerHousehold(json.getDouble("avgPeoplePerHousehold", 2.5));
            data.setMedicalNeedRate(json.getDouble("medicalNeedRate", 0.1));
            data.setResponderDensity(json.getDouble("responderDensity", 0.05));
            data.setRegionalRiskFactor(json.getDouble("regionalRiskFactor", 1.0));

            JsonObject improbability = json.getJsonObject("improbabilityFactor");
            if (improbability != null) {
                ImprobabilityFactor factor = new ImprobabilityFactor();
                factor.setExternalExploitation(improbability.getDouble("externalExploitation", 0.0));
                factor.setInternalUnrest(improbability.getDouble("internalUnrest", 0.0));
                factor.setIncreasedCrime(improbability.getDouble("increasedCrime", 0.0));
                factor.setDecreasedResponseRatio(improbability.getDouble("decreasedResponseRatio", 0.0));
                data.setImprobabilityFactor(factor);
            }

            return data;
        } catch (IOException e) {
            log.error("Error reading scraped data file: " + e.getMessage());
            return null;
        }
    }
}
