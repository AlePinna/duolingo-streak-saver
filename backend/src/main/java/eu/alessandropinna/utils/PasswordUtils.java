package eu.alessandropinna.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

@Component
public class PasswordUtils {

    private final SecretKey secretKey;

    public PasswordUtils(@Value("${encryption.key}") String key64) {

        byte[] decodedKey = Base64.getDecoder().decode(key64);
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public Pair<String, String> hashNewPassword(String clearPassword) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        byte[] hashedPassword = hash(clearPassword, salt);
        return Pair.of(Base64.getEncoder().encodeToString(salt), (Base64.getEncoder().encodeToString(hashedPassword)));
    }

    public boolean isHashMatching(String input, String hashedPassword, String saltBase64) throws NoSuchAlgorithmException {
        byte[] salt = Base64.getDecoder().decode(saltBase64);
        String hashedInput = Base64.getEncoder().encodeToString(hash(input, salt));
        return Objects.equals(hashedInput, hashedPassword);
    }

    private byte[] hash(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        return md.digest(password.getBytes(StandardCharsets.UTF_8));
    }

    public String encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedByte);
    }

    public String decrypt(String encryptedText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedTextByte = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        return new String(decryptedByte);
    }

    public boolean isPasswordMatching(String input, String encrypted) throws NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return Objects.equals(input, decrypt(encrypted));
    }
}
