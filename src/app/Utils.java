package app;

import constants.Path;
import security.GenerateKeys;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Utils {


    public KeyPair checkPgp(){
        try{
        File file =new File(Path.clientPublicKey);
        if (file.exists()){
            //get public
            GenerateKeys generateKeys=new GenerateKeys(4096);
            byte[] publicKeyBytes= generateKeys.readFromFile(Path.clientPublicKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = kf.generatePublic(spec);
            //get private
            byte[] privateKeyBytes= generateKeys.readFromFile(Path.clientPrivateKey);
            KeyFactory kf1 = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec spec1 = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privatekey = kf1.generatePrivate(spec1);
            return new KeyPair(publicKey,privatekey);
        }else {
            GenerateKeys generateKeys = new GenerateKeys(4096);
            generateKeys.createKeys();
            PublicKey publicKey = generateKeys.getPublicKey();
            PrivateKey privateKey = generateKeys.getPrivateKey();
            generateKeys.writeToFile(Path.clientPublicKey, publicKey.getEncoded());
            generateKeys.writeToFile(Path.clientPrivateKey, privateKey.getEncoded());
            return generateKeys.getPair();
        }
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public KeyPair serverCheckPgp() {
        try {
        File file =new File(Path.serverPublicKey);
        if (file.exists()){
            //get public
            GenerateKeys generateKeys= null;

                generateKeys = new GenerateKeys(4096);

            byte[] publicKeyBytes= generateKeys.readFromFile(Path.serverPublicKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = kf.generatePublic(spec);
            //get private
            byte[] privateKeyBytes= generateKeys.readFromFile(Path.serverPrivateKey);
            KeyFactory kf1 = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec spec1 = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privatekey = kf1.generatePrivate(spec1);
            return new KeyPair(publicKey,privatekey);

        }else {
            GenerateKeys generateKeys = new GenerateKeys(4096);
            generateKeys.createKeys();
            PublicKey publicKey = generateKeys.getPublicKey();
            PrivateKey privateKey = generateKeys.getPrivateKey();
            generateKeys.writeToFile(Path.serverPublicKey, publicKey.getEncoded());
            generateKeys.writeToFile(Path.serverPrivateKey, privateKey.getEncoded());
            return generateKeys.getPair();
        }
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

}
