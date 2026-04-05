package com.redhat.cajun.navy.datagenerate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.regex.Pattern;

public class GeneratePhoneNumbersTest {

    @Test
    public void testGetNextPhoneNumber() {
        String phoneNumber = GeneratePhoneNumbers.getNextPhoneNumber();
        assertNotNull(phoneNumber);
        // Format is "(%s) %s-%s" where %s are areaCode, prefixCode (555), and 4 digits
        // Example: (704) 555-1234
        assertTrue(Pattern.compile("\\(\\d{3}\\) 555-\\d{4}").matcher(phoneNumber).matches());
    }

    @Test
    public void testGenerateMultiplePhoneNumbers() {
        for (int i = 0; i < 100; i++) {
            String phoneNumber = GeneratePhoneNumbers.getNextPhoneNumber();
            assertTrue(Pattern.compile("\\(\\d{3}\\) 555-\\d{4}").matcher(phoneNumber).matches());
        }
    }
}
