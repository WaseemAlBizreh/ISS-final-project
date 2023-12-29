package security;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class GenerateKeys {

    private final KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public GenerateKeys(int keysize)
            throws NoSuchAlgorithmException {

        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keysize);

    }

    public void createKeys() {

        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public KeyPair getPair() {
        return pair;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void writeToFile(String path, byte[] key) throws IOException {

        File f = new File(path);
        f.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();

    }
    public byte[] readFromFile(String path) throws IOException{
        File f=new File(path);
        InputStream inputStream = new FileInputStream(f);
        byte[] bytes = inputStream.readAllBytes();
        inputStream.close();
        return bytes;
    }
}