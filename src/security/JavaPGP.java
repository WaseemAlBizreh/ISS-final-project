package security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;


public  class JavaPGP {



public static byte[] encrypt(byte[] message, PublicKey key) {
    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] encryptedBytes = cipher.doFinal(message);
    System.out.println("Encrypted Text: " + new String(encryptedBytes));
        return encryptedBytes;
    } catch (NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
        throw new RuntimeException(e);
    }


}

public static byte[] decrypt(byte[] message, PrivateKey key) {
    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] decryptedBytes = cipher.doFinal(message);
    System.out.println("Decrypted Text: " + new String(decryptedBytes));
return decryptedBytes;
    } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
        throw new RuntimeException(e);
    }


}}