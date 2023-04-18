package com.example.votingapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class EncryptionTest {
     @Test
    public void testEncryptAndDecrypt() {
        String input = "test-input";
        String groupEncryptionKey = "test-group-key";

        String encrypted = EncryptionUtils.encrypt(input, groupEncryptionKey);
        String decrypted = EncryptionUtils.decrypt(encrypted, groupEncryptionKey);

        assertEquals(input, decrypted);
    }
}

