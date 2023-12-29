package security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;


public final class JavaPGP {


//    public static byte[] encrypt(byte[] message, PublicKey key) {
//        Cipher cipher = null;
//        try {
//            cipher = Cipher.getInstance("RSA");
//
//        cipher.init(Cipher.ENCRYPT_MODE, key);
//
//        byte[] encryptedMessage = cipher.doFinal(message);
//        ByteBuffer buffer = ByteBuffer.allocate((encryptedMessage.length));
//        buffer.put(encryptedMessage);
//        return buffer.array();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchPaddingException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalBlockSizeException e) {
//            throw new RuntimeException(e);
//        } catch (BadPaddingException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidKeyException e) {
//            throw new RuntimeException(e);
//        }
//    }
public static byte[] encrypt(byte[] message, PublicKey key) {
    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] encryptedBytes = cipher.doFinal(message);
    System.out.println("Encrypted Text: " + new String(encryptedBytes));
        return encryptedBytes;
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    } catch (NoSuchPaddingException e) {
        throw new RuntimeException(e);
    } catch (IllegalBlockSizeException e) {
        throw new RuntimeException(e);
    } catch (BadPaddingException e) {
        throw new RuntimeException(e);
    } catch (InvalidKeyException e) {
        throw new RuntimeException(e);
    }



}
//    public static byte[] decrypt(byte[] message, PrivateKey key) {
//        ByteBuffer buffer = ByteBuffer.wrap(message);
//        int keyLength = buffer.getInt();
//        Cipher cipher = null;
//        try {
//            cipher = Cipher.getInstance("RSA");
//
//        cipher.init(Cipher.DECRYPT_MODE, key);
//        byte[] encryptedMessage = new byte[buffer.get()];
//        buffer.get(encryptedMessage);
//
//        return cipher.doFinal(encryptedMessage);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchPaddingException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalBlockSizeException e) {
//            throw new RuntimeException(e);
//        } catch (BadPaddingException e) {
//            throw new RuntimeException(e);
//        } catch (InvalidKeyException e) {
//            throw new RuntimeException(e);
//        }
//    }
public static byte[] decrypt(byte[] message, PrivateKey key) {
    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] decryptedBytes = cipher.doFinal(message);
    System.out.println("Decrypted Text: " + new String(decryptedBytes));
return decryptedBytes;
    } catch (NoSuchPaddingException e) {
        throw new RuntimeException(e);
    } catch (IllegalBlockSizeException e) {
        throw new RuntimeException(e);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    } catch (BadPaddingException e) {
        throw new RuntimeException(e);
    } catch (InvalidKeyException e) {
        throw new RuntimeException(e);
    }


}}