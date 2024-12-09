package com.xuange.ai.aaa.codec;
//xuange
public class codeFactory {
    public RSAEncryption getRSAEncryption() throws Exception {
        return new RSAEncryption();
    }

    public DSAExample getDSAExample() throws Exception {
        return new DSAExample();
    }
    public ElGamal getElGamal(){
        return new ElGamal(16);
    }
}
