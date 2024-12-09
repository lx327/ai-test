package com.xuange.ai.aaa.codec;

import java.security.*;
import java.util.Base64;
//xuange
public class DSAExample {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public DSAExample() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(2048); // Key size
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    public String sign(String data) throws Exception {
        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] signedData = signature.sign();
        return Base64.getEncoder().encodeToString(signedData);
    }

    public boolean verify(String data, String signedData) throws Exception {
        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        byte[] byteSignedData = Base64.getDecoder().decode(signedData);
        return signature.verify(byteSignedData);
    }
}