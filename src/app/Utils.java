package app;

import constants.FilePath;
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

        File file =new File(FilePath.desktopPath+"\\client");
        if (file.exists()){
            //get public
            GenerateKeys generateKeys=new GenerateKeys(4096);
            byte[] publicKeyBytes= generateKeys.readFromFile(FilePath.createFile("clientPublicKey.txt","client"));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = kf.generatePublic(spec);
            //get private
            byte[] privateKeyBytes= generateKeys.readFromFile(FilePath.createFile("clientPrivateKey.txt","client"));
            KeyFactory kf1 = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec spec1 = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privatekey = kf1.generatePrivate(spec1);
            return new KeyPair(publicKey,privatekey);
        }else {
            GenerateKeys generateKeys = new GenerateKeys(4096);
            generateKeys.createKeys();
            PublicKey publicKey = generateKeys.getPublicKey();
            PrivateKey privateKey = generateKeys.getPrivateKey();
            generateKeys.writeToFile(FilePath.createFile("clientPublicKey.txt","client"), publicKey.getEncoded());
            generateKeys.writeToFile(FilePath.createFile("clientPrivateKey.txt","client"), privateKey.getEncoded());
            return generateKeys.getPair();
        }
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public KeyPair serverCheckPgp() {
        try {
            File file =new File(FilePath.desktopPath+"\\server");
        if (file.exists()){
            //get public
            GenerateKeys generateKeys= null;

                generateKeys = new GenerateKeys(4096);

            byte[] publicKeyBytes= generateKeys.readFromFile(FilePath.createFile("serverPublicKey.txt","server"));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = kf.generatePublic(spec);
            //get private
            byte[] privateKeyBytes= generateKeys.readFromFile(FilePath.createFile("serverPrivateKey.txt","server"));
            KeyFactory kf1 = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec spec1 = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privatekey = kf1.generatePrivate(spec1);
            return new KeyPair(publicKey,privatekey);

        }else {
            GenerateKeys generateKeys = new GenerateKeys(4096);
            generateKeys.createKeys();
            PublicKey publicKey = generateKeys.getPublicKey();
            PrivateKey privateKey = generateKeys.getPrivateKey();
            generateKeys.writeToFile(FilePath.createFile("serverPublicKey.txt","server"), publicKey.getEncoded());
            generateKeys.writeToFile(FilePath.createFile("serverPrivateKey.txt","server"), privateKey.getEncoded());
            return generateKeys.getPair();
        }
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

}
