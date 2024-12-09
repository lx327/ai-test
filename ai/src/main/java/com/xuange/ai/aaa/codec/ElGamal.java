package com.xuange.ai.aaa.codec;
import java.math.BigInteger;
import java.security.SecureRandom;
//xuange
public class ElGamal {

    private BigInteger p; // a large prime number
    private BigInteger g; // a generator of the multiplicative group of integers modulo p
    private BigInteger x; // private key
    private BigInteger y; // public key

    private SecureRandom random;

    public ElGamal(int bitLength) {
        random = new SecureRandom();
        p = BigInteger.probablePrime(bitLength, random);
        g = new BigInteger("2"); // a simple generator
        x = new BigInteger(bitLength, random).mod(p.subtract(BigInteger.ONE)); // private key
        y = g.modPow(x, p); // public key
    }

    public BigInteger[] encrypt(BigInteger message) {
        BigInteger k;
        do {
            k = new BigInteger(p.bitLength(), random);
        } while (k.compareTo(BigInteger.ZERO) <= 0 || k.compareTo(p.subtract(BigInteger.ONE)) >= 0);

        BigInteger c1 = g.modPow(k, p); // c1 = g^k mod p
        BigInteger c2 = message.multiply(y.modPow(k, p)).mod(p); // c2 = m * y^k mod p
        return new BigInteger[]{c1, c2};
    }

    public BigInteger decrypt(BigInteger[] ciphertext) {
        BigInteger c1 = ciphertext[0];
        BigInteger c2 = ciphertext[1];
        BigInteger s = c1.modPow(x, p); // s = c1^x mod p
        BigInteger sInv = s.modInverse(p); // s^-1 mod p
        return c2.multiply(sInv).mod(p); // m = c2 * s^-1 mod p
    }

    public BigInteger getPublicKey() {
        return y;
    }


}