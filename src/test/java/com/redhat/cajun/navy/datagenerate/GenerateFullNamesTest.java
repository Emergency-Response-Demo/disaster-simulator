package com.redhat.cajun.navy.datagenerate;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;

import org.junit.Test;

public class GenerateFullNamesTest {

    @Test
    public void testGenerateFullNames() {

        String fNameFile = new File("src/test/resources/FNames.txt").getAbsolutePath();
        String lNameFile = new File("src/test/resources/LNames.txt").getAbsolutePath();

        GenerateFullNames gfn = new GenerateFullNames(fNameFile, lNameFile);

        for (int i = 0; i < 10; i++) {
            String nextFullName = gfn.getNextFullName();
            assertThat(nextFullName, notNullValue());
            assertThat(nextFullName, anyOf(containsString("John"), containsString("Fred")));
            assertThat(nextFullName, anyOf(containsString("Doe"), containsString("Foo")));
        }

    }

}
