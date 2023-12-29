package security;
import javax.crypto.Cipher;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;


public final class JavaPGP {


    public static byte[] encrypt(byte[] message, PublicKey key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedMessage = cipher.doFinal(message);
        ByteBuffer buffer = ByteBuffer.allocate((encryptedMessage.length) + 4);
        buffer.put(encryptedMessage);
        return buffer.array();
    }

    public static byte[] decrypt(byte[] message, PrivateKey key) throws GeneralSecurityException {
        ByteBuffer buffer = ByteBuffer.wrap(message);
        int keyLength = buffer.getInt();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedMessage = new byte[buffer.remaining()];
        buffer.get(encryptedMessage);
        return cipher.doFinal(encryptedMessage);
    }





}