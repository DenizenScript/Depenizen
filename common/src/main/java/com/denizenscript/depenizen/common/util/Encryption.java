package com.denizenscript.depenizen.common.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;

public class Encryption {

    private final SecretKey secret;
    private final Cipher cipher;
    private final byte[] iv;

    public Encryption(char[] password, byte[] salt) throws GeneralSecurityException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        this.secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        this.iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    }

    public byte[] encrypt(byte[] message) throws GeneralSecurityException {
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        return cipher.doFinal(message);
    }

    public byte[] decrypt(byte[] message) throws GeneralSecurityException {
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        return cipher.doFinal(message);
    }

    public byte[] getIV() {
        return iv;
    }
}
