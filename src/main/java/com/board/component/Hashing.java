package com.board.component;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
@Deprecated
public class Hashing {

    private static final int SALT_LENGTH = 32;

    public static String createSaltKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);

        return encodeBase64(salt);
    }

    public static String hash(String text, String salt) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(text.getBytes());
            sha256.update(decodeBase64(salt));
            sha256.update(text.getBytes());

            byte[] digest = sha256.digest();
            return encodeBase64(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String encodeBase64(byte[] text) {
        return Base64.getEncoder()
                .encodeToString(text);
    }

    private static byte[] decodeBase64(String text) {
        return Base64.getDecoder()
                .decode(text);
    }

}
