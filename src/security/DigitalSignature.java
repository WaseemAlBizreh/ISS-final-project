package security;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DigitalSignature {


    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // يمكنك تعيين حجم المفتاح حسب الحاجة
        return keyPairGenerator.generateKeyPair();
    }


    public byte[] signData(byte[] data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA"); // استخدام خوارزمية التوقيع المناسبة
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA"); // استخدام نفس الخوارزمية المستخدمة للتوقيع
        sig.initVerify(publicKey);
        sig.update(data);
        return sig.verify(signature);
    }


    public String convertKeyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public boolean areKeysEqual(Key key1, Key key2) {
        byte[] keyBytes1 = key1.getEncoded();
        byte[] keyBytes2 = key2.getEncoded();

        return MessageDigest.isEqual(keyBytes1, keyBytes2);
    }


    public PrivateKey getPrivateKeyFromString(String privateKeyString) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(keySpec);
    }

    public PublicKey getPublicKeyFromString(String publicKeyString) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }




}

