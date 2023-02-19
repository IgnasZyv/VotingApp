package com.example.votingapp;

import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

public class EncryptionUtils {

    public static String generateEncryptionKey() {
        try {
            // Generate a 128-bit AES encryption key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128, new SecureRandom());
            SecretKey secretKey = keyGenerator.generateKey();

            // Encode the key in Base64 format
            byte[] encodedKey = secretKey.getEncoded();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(encodedKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public static String encrypt(String input, String groupEncryptionKey) {
        try {
            // Create a new encryption key by hashing the group encryption key
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = groupEncryptionKey.getBytes(StandardCharsets.UTF_8);
            byte[] hashBytes = digest.digest(keyBytes);
            SecretKeySpec secretKeySpec = new SecretKeySpec(hashBytes, "AES");

            // Generate a new IV for the encryption process
            SecureRandom random = new SecureRandom();
            byte[] iv = new byte[16];
            random.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Perform the encryption
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(input.getBytes());

            // Combine the IV and encrypted data into a single Base64-encoded string
            ByteBuffer buffer = ByteBuffer.allocate(iv.length + encrypted.length);
            buffer.put(iv);
            buffer.put(encrypted);
            byte[] combined = buffer.array();

            return android.util.Base64.encodeToString(combined, android.util.Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String input, String groupEncryptionKey) {
        try {
            // Create a new encryption key by hashing the group encryption key
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = groupEncryptionKey.getBytes(StandardCharsets.UTF_8);
            byte[] hashBytes = digest.digest(keyBytes);
            SecretKeySpec secretKeySpec = new SecretKeySpec(hashBytes, "AES");

            // Split the input string into its IV and encrypted data components
            byte[] combined = android.util.Base64.decode(input, android.util.Base64.DEFAULT);
            ByteBuffer buffer = ByteBuffer.wrap(combined);
            byte[] iv = new byte[16];
            buffer.get(iv);
            byte[] encrypted = new byte[buffer.remaining()];
            buffer.get(encrypted);

            // Perform the decryption
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
            byte[] decrypted = cipher.doFinal(encrypted);

            // Convert the decrypted data back to a string
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getOrCreateEncryptionKey(String groupId) {
        String url = "https://pmjbsctasrzmcwleuotqtje6my0ocngl.lambda-url.eu-central-1.on.aws/";
        String requestBody = "{ \"groupId\": \"" + groupId + "\" }";

        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(requestBody.getBytes());
            os.flush();
            os.close();

            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String responseBody = response.toString();

            if (responseCode == 200) {
                // Key generated and stored successfully
                return responseBody;
            } else {
                // Handle error
                return null;
            }
        } catch (Exception e) {
            // Handle error
            Log.d("EncryptionUtils", "getOrCreateEncryptionKey: " + Arrays.toString(e.getStackTrace()));
        }
        return null;

    }
}
