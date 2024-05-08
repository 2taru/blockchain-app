package com.taru.cryptocurrency;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class CryptographyHelper {

    /**
     * Method that generate SHA-256 hash from given data
     *
     * @param data input data to generate hash
     * @return 64 hexadecimal characters as {@link java.lang.String}
     */
    public static String generateHash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexadecimalString = new StringBuilder();

            for (var h : hash) {
                String hexadecimal = Integer.toHexString(h & 0xff);

                if (hexadecimal.length() == 1) {
                    hexadecimalString.append('0');
                }

                hexadecimalString.append(hexadecimal);
            }

            return hexadecimalString.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method generate public and private keys using ECDSA.
     *
     * @return {@link java.security.KeyPair}, where
     * <p>private key: 256 bits long random integer;</p>
     * <p>public key: point on the elliptic curve ((x,y) - both of these values are 256 bits long)</p>
     */
    public static KeyPair generateKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
            ECGenParameterSpec params = new ECGenParameterSpec("prime256v1");
            keyPairGenerator.initialize(params);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ECDSA (elliptic curve digital signature algorithm).
     * This method sign given {@link java.lang.String} message with given private key {@link java.security.PrivateKey}
     *
     * @param privateKey key to sign with
     * @param input      data to be signed
     * @return byte array with signed data
     */
    public static byte[] sign(PrivateKey privateKey, String input) {
        byte[] output;

        try {
            Signature signature = Signature.getInstance("ECDSA", "BC");
            signature.initSign(privateKey);
            signature.update(input.getBytes());
            output = signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return output;
    }

    /**
     * This method checks whether the given transaction belongs to the sender based on the signature.
     *
     * @param publicKey sender public key
     * @param data      data to verify
     * @param signature signed data of sender
     * @return true if the transaction belongs to the sender, or false otherwise
     */
    public static boolean verify(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaSignature = Signature.getInstance("ECDSA", "BC");
            ecdsaSignature.initVerify(publicKey);
            ecdsaSignature.update(data.getBytes());
            return ecdsaSignature.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
