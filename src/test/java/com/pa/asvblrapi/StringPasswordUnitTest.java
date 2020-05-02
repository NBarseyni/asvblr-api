package com.pa.asvblrapi;

import com.pa.asvblrapi.spring.RandomPasswordGenerator;
import org.junit.jupiter.api.Test;

import static org.springframework.test.util.AssertionErrors.assertTrue;

public class StringPasswordUnitTest {

    RandomPasswordGenerator randomPasswordGenerator = new RandomPasswordGenerator();

    @Test
    public void whenPasswordGenerated_thenSuccessful() {
        String password = randomPasswordGenerator.generatePassword();
        int specialCharCount = 0;
        for (char c : password.toCharArray()) {
            if (c >= 33 || c <= 47) {
                specialCharCount++;
            }
        }
        assertTrue("Password validation failed in Passay", specialCharCount >= 2);
    }
}
