package com.redhat.cajun.navy.datagenerate;


import org.apache.commons.cli.*;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import com.redhat.cajun.navy.datagenerate.physics.PhysicsSimulation;
import com.redhat.cajun.navy.datagenerate.visualization.ImageRenderer;
import java.util.List;
import java.io.File;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        try {
            run(args);
        } catch (ParseException e) {
            log.error(e.getMessage());
            formatter.printHelp("Main -m server|cli -g NUMBER", options);

            System.out.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Main -m server|cli -g NUMBER", getOptions());
            System.exit(1);
        }
    }

    public static void run(String[] args) throws Exception {
        Options options = getOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        int number = Integer.parseInt(cmd.getOptionValue("g"));

        Disaster disaster = new Disaster("fnames.txt","lnames.txt");
        if (cmd.hasOption("scrape")) {
            String scrapeFile = cmd.getOptionValue("scrape");
            DataScraper scraper = new DataScraper();
            ScrapedData data = scraper.scrape(scrapeFile);
            if (data != null) {
                disaster.applyScrapedData(data);
            }
        }

        if (cmd.hasOption("geojson")) {
            String geoJsonFile = cmd.getOptionValue("geojson");
            GeoJsonLoader loader = new GeoJsonLoader();
            java.util.List<Zone> zones = loader.load(geoJsonFile);
            if (!zones.isEmpty()) {
                disaster.boundingPolygons.setInclusionZones(zones);
            }
        }

        switch(cmd.getOptionValue("m")) {
            case "server":
                Vertx vertx = Vertx.vertx();
                // Pass scraped file path via system property if needed, or rely on config
                if (cmd.hasOption("scrape")) {
                    System.setProperty("simulation.scraped.data.file", cmd.getOptionValue("scrape"));
                }
                if (cmd.hasOption("geojson")) {
                    System.setProperty("simulation.geojson.file", cmd.getOptionValue("geojson"));
                }
                vertx.rxDeployVerticle(MainVerticle.class.getName())
                        .subscribe();
                break;
            case "cli":
                if (cmd.hasOption("sim")) {
                    int simSteps = 10;
                    if (cmd.hasOption("steps")) {
                        try {
                            simSteps = Integer.parseInt(cmd.getOptionValue("steps"));
                        } catch (NumberFormatException e) {
                            log.warn("Invalid steps number, using default 10");
                        }
                    }
                    String outDir = "output";
                    if (cmd.hasOption("out")) {
                        outDir = cmd.getOptionValue("out");
                    }
                    File dir = new File(outDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    List<Victim> victims = disaster.generateVictims(number);
                    List<Responder> responders = disaster.generateResponders(number);
                    log.info("Generated " + victims.size() + " victims and " + responders.size() + " responders.");

                    PhysicsSimulation physics = new PhysicsSimulation();
                    physics.init(disaster.boundingPolygons);

                    for (Victim v : victims) physics.addVictim(v);
                    for (Responder r : responders) physics.addResponder(r);

                    ImageRenderer renderer = new ImageRenderer();

                    for (int i = 0; i < simSteps; i++) {
                        physics.step(1.0);
                        physics.updateEntities();
                        renderer.render(physics, disaster.boundingPolygons, outDir + "/step_" + String.format("%03d", i) + ".png");
                        log.info("Step " + i + " complete.");
                    }
                    log.info("Simulation complete. Images saved to " + outDir);

                } else {
                    log.info("Generating Victims List in Json");
                    System.out.println(disaster.generateVictims(number));
                }
                break;
            default: log.error("Incorrect mode");
        }
    }

    private static Options getOptions() {
        Options options = new Options();

        Option mode = new Option("m", "mode", true, "server|cli");
        mode.setRequired(true);
        options.addOption(mode);

        Option generate = new Option("g", "generate", true, "number of victims to generate");
        generate.setRequired(true);
        options.addOption(generate);

        Option scrape = new Option("s", "scrape", true, "file path to scraped data json");
        scrape.setRequired(false);
        options.addOption(scrape);

        Option geojson = new Option("map", "geojson", true, "file path to geojson map file");
        geojson.setRequired(false);
        options.addOption(geojson);

        Option sim = new Option("sim", "simulation", false, "run physics simulation");
        sim.setRequired(false);
        options.addOption(sim);

        Option steps = new Option("steps", "steps", true, "simulation steps (default 10)");
        steps.setRequired(false);
        options.addOption(steps);

        Option out = new Option("out", "output", true, "output directory for images (default: output)");
        out.setRequired(false);
        options.addOption(out);
        return options;
    }

}