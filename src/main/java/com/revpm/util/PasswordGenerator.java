package com.revpm.util;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class PasswordGenerator {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*";

    public static String generateStrongPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder pwd = new StringBuilder();

        // 1 char from each category
        pwd.append(UPPER.charAt(random.nextInt(UPPER.length())));
        pwd.append(LOWER.charAt(random.nextInt(LOWER.length())));
        pwd.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        pwd.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        String all = UPPER + LOWER + DIGITS + SPECIAL;

        // Fill remaining chars to reach length 8
        while (pwd.length() < 8) {
            pwd.append(all.charAt(random.nextInt(all.length())));
        }

        // Shuffle to randomize positions
        List<Character> pwdChars = new ArrayList<>();
        for (char c : pwd.toString().toCharArray()) {
            pwdChars.add(c);
        }
        Collections.shuffle(pwdChars, random);

        // Convert back to string
        StringBuilder finalPwd = new StringBuilder();
        for (char c : pwdChars) {
            finalPwd.append(c);
        }

        return finalPwd.toString();
    }

}
