package security;

import model.Message;
import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import java.security.*;

public class SessionKey {


    private final String algorithm = "DES";
    private SecretKey sessionKey;
    private KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);

    public SessionKey() throws NoSuchAlgorithmException {
        this.keyGenerator.init(2048);
        this.sessionKey = keyGenerator.generateKey();
    }



    public static String encrypt(Message message, SecretKey key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] messageBytes = message.toString().getBytes();
        byte[] encrypted = cipher.doFinal(messageBytes);
        return Base64.encodeBase64String(encrypted);
    }

    public static Message decrypt(String message, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(message));
            String decryptedMessage = new String(decryptedBytes);
            Message mes = new Message();
            mes.parseToModel(decryptedMessage);
            return mes;
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException |
                IllegalBlockSizeException | BadPaddingException exception) {
            exception.printStackTrace();
            System.out.println("Error" + exception.getMessage());
            return null;
        }
    }
}
