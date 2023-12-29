package app;

import security.GenerateKeys;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Utils {


    public KeyPair checkpgp() throws NoSuchAlgorithmException, NoSuchProviderException, IOException, InvalidKeySpecException {
        File file =new File("C:\\Users\\Saria Alzoubi\\Desktop\\NetworkProject\\group1\\filePublic.txt");
        if (file.exists()){
            GenerateKeys generateKeys=new GenerateKeys();
            byte[] publicKeyBytes= generateKeys.readFromFile("C:\\Users\\Saria Alzoubi\\Desktop\\NetworkProject\\group1\\filePublic.txt");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = kf.generatePublic(spec);
            byte[] privateKeyBytes= generateKeys.readFromFile("C:\\Users\\Saria Alzoubi\\Desktop\\NetworkProject\\group1\\filePrivate.txt");
            KeyFactory kf1 = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec spec1 = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey privatekey = kf1.generatePrivate(spec1);
            return new KeyPair(publicKey,privatekey);
        }else {
            GenerateKeys generateKeys = new GenerateKeys();
            generateKeys.createKeys();
            PublicKey publicKey = generateKeys.getPublicKey();
            PrivateKey privateKey = generateKeys.getPrivateKey();
            generateKeys.writeToFile("C:\\Users\\Saria Alzoubi\\Desktop\\NetworkProject\\group1\\filePublic.txt", publicKey.getEncoded());
            generateKeys.writeToFile("C:\\Users\\Saria Alzoubi\\Desktop\\NetworkProject\\group1\\filePrivate.txt", privateKey.getEncoded());
            return generateKeys.getPair();
        }
    }
}
