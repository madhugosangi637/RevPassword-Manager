package com.revpm.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.revpm.util.PasswordUtil;

public class PasswordUtilTest {

    @Test
    void testHashMasterPassword() {
        String password = "Test@123";
        String hash1 = PasswordUtil.hashMasterPassword(password);
        String hash2 = PasswordUtil.hashMasterPassword(password);

        assertNotNull(hash1);
        assertEquals(hash1, hash2); // same input → same hash
    }

    @Test
    void testEncryptDecrypt() {
        String original = "gmailPassword@123";

        String encrypted = PasswordUtil.encrypt(original);
        String decrypted = PasswordUtil.decrypt(encrypted);

        assertNotEquals(original, encrypted); // encrypted ≠ original
        assertEquals(original, decrypted);    // decrypted = original
    }
}

