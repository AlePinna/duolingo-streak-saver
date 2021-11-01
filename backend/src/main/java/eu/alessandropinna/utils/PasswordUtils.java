package eu.alessandropinna.utils;

import org.springframework.data.util.Pair;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;
import java.util.Set;

public class PasswordUtils {

    private final SecretKey secretKey;

    public final String key64;

    public final Pair<String, String> hashPair;

    private final Set<Integer> acceptedKeySizes = Set.of(16, 24, 32);

    public PasswordUtils(String key64) throws NoSuchAlgorithmException {
        byte[] decodedKey;
        if (key64 == null || !acceptedKeySizes.contains((decodedKey = Base64.getDecoder().decode(key64)).length)) {
            throw new IllegalStateException("Invalid encryption key");
        }
        this.key64 = key64;
        hashPair = newHash(key64);
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public Pair<String, String> newHash(String clearInput) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        byte[] hashedPassword = hash(clearInput, salt);
        return Pair.of(Base64.getEncoder().encodeToString(salt), (Base64.getEncoder().encodeToString(hashedPassword)));
    }

    public boolean isHashMatching(String clearInput, String hash, String saltBase64) throws NoSuchAlgorithmException {
        byte[] salt = Base64.getDecoder().decode(saltBase64);
        String hashedInput = Base64.getEncoder().encodeToString(hash(clearInput, salt));
        return Objects.equals(hashedInput, hash);
    }

    private byte[] hash(String clearInput, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        return md.digest(clearInput.getBytes(StandardCharsets.UTF_8));
    }

    public String encrypt(String plainText, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedByte);
    }

    public String encrypt(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return encrypt(plainText, secretKey);
    }

    public String decrypt(String encryptedText, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedTextByte = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        return new String(decryptedByte);
    }

    public String decrypt(String encryptedText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return decrypt(encryptedText, secretKey);
    }

    public boolean isPasswordMatching(String input, String encrypted) throws NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return Objects.equals(input, decrypt(encrypted));
    }
}
