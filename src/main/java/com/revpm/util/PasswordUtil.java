package com.revpm.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    // ================= MASTER PASSWORD (HASHING) =================

    public static String hashMasterPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ✅ THIS METHOD WAS MISSING — VERY IMPORTANT
    public static boolean verifyMasterPassword(String enteredPassword, String storedHash) {
        String enteredHash = hashMasterPassword(enteredPassword);
        return enteredHash.equals(storedHash);
    }

    // ================= ACCOUNT PASSWORDS (AES ENCRYPT / DECRYPT) =================

    private static final String ALGO = "AES/CBC/PKCS5Padding";

    // NOTE: For learning project this is OK. In real apps, never hardcode keys.
    private static final byte[] KEY_BYTES =
            "1234567890123456".getBytes(StandardCharsets.UTF_8); // 16 bytes key

    public static String encrypt(String plainText) {
        try {
            SecretKey key = new SecretKeySpec(KEY_BYTES, "AES");
            Cipher cipher = Cipher.getInstance(ALGO);

            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String cipherText) {
        try {
            byte[] combined = Base64.getDecoder().decode(cipherText);

            byte[] iv = new byte[16];
            byte[] encrypted = new byte[combined.length - 16];

            System.arraycopy(combined, 0, iv, 0, 16);
            System.arraycopy(combined, 16, encrypted, 0, encrypted.length);

            SecretKey key = new SecretKeySpec(KEY_BYTES, "AES");
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

            byte[] original = cipher.doFinal(encrypted);
            return new String(original, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
