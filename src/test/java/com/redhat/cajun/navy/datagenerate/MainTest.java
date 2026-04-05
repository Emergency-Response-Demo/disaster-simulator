package com.redhat.cajun.navy.datagenerate;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class MainTest {

    @Test(expected = ParseException.class)
    public void testRunInvalidArgs() throws Exception {
        // Missing required options -m and -g
        String[] args = new String[]{"-sim"};
        Main.run(args);
    }

    @Test(expected = ParseException.class)
    public void testRunMissingMode() throws Exception {
        String[] args = new String[]{"-g", "10"};
        Main.run(args);
    }

    @Test(expected = ParseException.class)
    public void testRunMissingGenerate() throws Exception {
        String[] args = new String[]{"-m", "cli"};
        Main.run(args);
    }
}
