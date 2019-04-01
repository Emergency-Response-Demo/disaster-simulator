package com.redhat.cajun.navy.datagenerate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class GenerateFullNames {

    private static Map<Integer, String> fNames = null;

    private static Map<Integer, String> lNames = null;


    public GenerateFullNames(String fNameFile, String lNameFile) {
        fNames = Collections.unmodifiableMap(getMapFromFile(fNameFile));
        lNames = Collections.unmodifiableMap(getMapFromFile(lNameFile));
    }

    public Map getMapFromFile(String fileName) {
        Map<Integer, String> temp = new HashMap<Integer, String>();
        int count = 1;
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                for (String line; (line = br.readLine()) != null; ) {
                    temp.put(count, line);
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public String getNextLastName() {
        return fNames.get(new Random().nextInt(lNames.size()));
    }

    public String getNextFirstName() {
        return fNames.get(new Random().nextInt(fNames.size()));
    }


    public String getNextFullName() {
        return getNextFirstName() + " " + getNextLastName();
    }

}
