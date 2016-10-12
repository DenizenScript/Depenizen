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

    private SecretKey secret;
    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private byte[] iv;

    public Encryption(char[] password, byte[] salt, byte[] iv) throws GeneralSecurityException {
        init(password, salt);
        this.iv = iv;
        finishInit();
    }

    public Encryption(char[] password, byte[] salt) throws GeneralSecurityException {
        init(password, salt);
        AlgorithmParameters params = encryptCipher.getParameters();
        this.iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        finishInit();
    }

    private void init(char[] password, byte[] salt) throws GeneralSecurityException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        this.secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        this.encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }

    private void finishInit() throws GeneralSecurityException {
        encryptCipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(iv));
        decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
    }

    public byte[] encrypt(byte[] message) throws GeneralSecurityException {
        return encryptCipher.doFinal(message);
    }

    public byte[] decrypt(byte[] message) throws GeneralSecurityException {
        return decryptCipher.doFinal(message);
    }

    public byte[] getIV() {
        return iv;
    }
}
