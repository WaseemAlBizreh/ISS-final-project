package app;

import security.GenerateKeys;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Utils {


    public KeyPair checkpgp(){
        try{
        File file =new File("C:\\path\\client\\keys\\publickey.txt");
        if (file.exists()){
            //get public
            GenerateKeys generateKeys=new GenerateKeys(4096);
            byte[] publicKeyBytes= generateKeys.readFromFile("C:\\path\\client\\keys\\publickey.txt");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = kf.generatePublic(spec);
            //get private
            byte[] privateKeyBytes= generateKeys.readFromFile("C:\\path\\client\\keys\\privatekey.txt");
            KeyFactory kf1 = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec spec1 = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privatekey = kf1.generatePrivate(spec1);
            return new KeyPair(publicKey,privatekey);
        }else {
            GenerateKeys generateKeys = new GenerateKeys(4096);
            generateKeys.createKeys();
            PublicKey publicKey = generateKeys.getPublicKey();
            PrivateKey privateKey = generateKeys.getPrivateKey();
            generateKeys.writeToFile("C:\\path\\client\\keys\\publickey.txt", publicKey.getEncoded());
            generateKeys.writeToFile("C:\\path\\client\\keys\\privatekey.txt", privateKey.getEncoded());
            return generateKeys.getPair();
        }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public KeyPair servercheckpgp() {
        try {
        File file =new File("C:\\path\\server\\to\\keys\\publickey.txt");
        if (file.exists()){
            //get public
            GenerateKeys generateKeys= null;

                generateKeys = new GenerateKeys(4096);

            byte[] publicKeyBytes= generateKeys.readFromFile("C:\\path\\server\\keys\\publickey.txt");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = kf.generatePublic(spec);
            //get private
            byte[] privateKeyBytes= generateKeys.readFromFile("C:\\path\\server\\keys\\privatekey.txt");
            KeyFactory kf1 = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec spec1 = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privatekey = kf1.generatePrivate(spec1);
            return new KeyPair(publicKey,privatekey);

        }else {
            GenerateKeys generateKeys = new GenerateKeys(4096);
            generateKeys.createKeys();
            PublicKey publicKey = generateKeys.getPublicKey();
            PrivateKey privateKey = generateKeys.getPrivateKey();
            generateKeys.writeToFile("C:\\path\\server\\keys\\publickey.txt", publicKey.getEncoded());
            generateKeys.writeToFile("C:\\path\\server\\keys\\privatekey.txt", privateKey.getEncoded());
            return generateKeys.getPair();
        }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

}
