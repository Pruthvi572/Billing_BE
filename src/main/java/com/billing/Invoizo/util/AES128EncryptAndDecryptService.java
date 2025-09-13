package com.billing.Invoizo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AES128EncryptAndDecryptService {
    private static final String ALOGRITHM = "AES";
    private static final String CIPHER_ALGORITHAM = "AES/CBC/PKCS5PADDING";

    private static SecretKeySpec getSecretKeySpecFromHexString(String algoCommonName, String hexString) {
        byte[] encodedBytes = hexStrToByteArray(hexString);
        return new SecretKeySpec(encodedBytes, algoCommonName);
    }

    private static byte[] hexStrToByteArray(String hex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(hex.length() / 2);
        for (int i = 0; i < hex.length(); i += 2) {
            String output = hex.substring(i, i + 2);
            int decimal = Integer.parseInt(output, 16);
            baos.write(decimal);
        }
        return baos.toByteArray();
    }

    public static byte[] copyIVAndCipher(byte[] encryptedText, byte[] iv) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(iv);
        os.write(encryptedText);
        return os.toByteArray();
    }

    public String aes128Encrypt(String plainText, String privateKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, IOException {

        byte[] iv = new byte[]{
                (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 0x07, 0x72, 0x6F, 0x5A,
                (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 0x07, 0x72, 0x6F, 0x5A};

        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
        SecretKeySpec secretKeySpec = getSecretKeySpecFromHexString(ALOGRITHM, privateKey);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHAM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, paramSpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] encryptedWithIV = copyIVAndCipher(encrypted, iv);
        return Base64.getEncoder().encodeToString(encryptedWithIV);
    }

    public String aes128Decrypt(String encryptedText, String privateKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

        SecretKeySpec secretKeySpec = getSecretKeySpecFromHexString(ALOGRITHM, privateKey);
        byte[] encryptedIVandTextAsBytes = Base64.getDecoder().decode(encryptedText);
        byte[] iv = Arrays.copyOf(encryptedIVandTextAsBytes, 16);
        byte[] ciphertextByte = Arrays.copyOfRange(encryptedIVandTextAsBytes, 16, encryptedIVandTextAsBytes.length);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHAM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        byte[] decryptedTextBytes = cipher.doFinal(ciphertextByte);
        return new String(decryptedTextBytes, "UTF-8");
    }
}
