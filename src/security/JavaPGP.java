package security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;


public  class JavaPGP {



public static byte[] encrypt(byte[] message, PublicKey key) {
    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] encryptedBytes = cipher.doFinal(message);
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
return decryptedBytes;
    } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
        throw new RuntimeException(e);
    }


}



    public static byte[] reverseencrypt(byte[] message, PrivateKey key) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(message);
            return encryptedBytes;
        } catch (NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }


    }

    public static byte[] reversedecrypt(byte[] message, PublicKey key) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(message);
            return decryptedBytes;
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }


    }





}