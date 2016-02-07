package com.poitot.cryptoexample.utils;

import android.util.Base64;

import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Cryptographic helper methods
 */
public class CryptoUtils {

    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 8;
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA1";                        // Using PKCS #5 v2.0 -- https://tools.ietf.org/html/rfc2898
    private static final String KEY_ALGORITHM = "AES";                                              // https://en.wikipedia.org/wiki/Advanced_Encryption_Standard
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String TEXT_ENCODING = "UTF-8";
    private static final String SEPARATOR = ",";                                                      // Can be any character not [0-9a-zA-Z/=+]

    /**
     * Generates a secret key
     * @param password passphrase for the key
     * @param salt a salt for the password
     */
    public static SecretKey generateSecretKey(char[] password, byte[] salt) throws Exception {
        final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
        final KeySpec keySpec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        byte[] keyBytes = secretKeyFactory.generateSecret(keySpec).getEncoded();
        return new SecretKeySpec(keyBytes, KEY_ALGORITHM);
    }

    /**
     * Encrypts plain text
     * @param password key password
     * @param text text to encrypt
     * @throws Exception
     */
    public static String encryptText(char[] password, String text) throws Exception {
        final SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);

        final SecretKey secretKey = generateSecretKey(password, salt);
        final Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        byte[] initVector = new byte[cipher.getBlockSize()];
        secureRandom.nextBytes(initVector);

        final IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] data = cipher.doFinal(text.getBytes(TEXT_ENCODING));

        // Everything encrypted is also Base64-encoded, and concatenated by a separator (,) to ensure
        // that all of the information necessaryto decrypt resides in one single string.
        return Base64.encodeToString(data, Base64.NO_WRAP | Base64.NO_PADDING)
                + SEPARATOR + Base64.encodeToString(initVector, Base64.NO_WRAP | Base64.NO_PADDING)
                + SEPARATOR + Base64.encodeToString(salt, Base64.NO_WRAP | Base64.NO_PADDING);
    }

    /**
     * Decrypts into plain text
     * @param password key password
     * @param encryptedData text to decrypt
     * @throws Exception
     */
    public static String decryptData(char[] password, String encryptedData) throws Exception {

        // Split the string into its requisite parts
        final String[] pieces = encryptedData.split(SEPARATOR);
        byte[] data = Base64.decode(pieces[0], Base64.DEFAULT);
        byte[] initVector = Base64.decode(pieces[1], Base64.DEFAULT);
        byte[] salt = Base64.decode(pieces[2], Base64.DEFAULT);

        final Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        final IvParameterSpec ivParameterSpec = new IvParameterSpec(initVector);
        final SecretKey secretKey = generateSecretKey(password, salt);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        return new String(cipher.doFinal(data), TEXT_ENCODING);
    }
}
