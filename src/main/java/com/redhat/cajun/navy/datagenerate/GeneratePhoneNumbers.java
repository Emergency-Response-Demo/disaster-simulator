package com.redhat.cajun.navy.datagenerate;

import java.text.DecimalFormat;
import java.util.Random;

public class GeneratePhoneNumbers {


    private final static int[] nationalAreaCode = {720, 303, 910, 414, 859, 202};
    private final static int[] ncAreaCode = {704, 828, 651, 336, 252, 984};
    private final static int prefixCode = 555;

    private final static DecimalFormat fourDigits = new DecimalFormat("0000");
    private final static Random random = new Random();

    private final static String format = "(%s) %s-%s";

    public static String getNextPhoneNumber() {
        int areaCode = 0;
        int percentage = random.nextInt(100);

        //Use the NC area codes for 90% of the time
        if (percentage < 90) {
            areaCode = ncAreaCode[random.nextInt(ncAreaCode.length)];
        } else {
            areaCode = nationalAreaCode[random.nextInt(nationalAreaCode.length)];
        }

        return String.format(format, areaCode, prefixCode, fourDigits.format(random.nextInt(9999)));
    }

}
