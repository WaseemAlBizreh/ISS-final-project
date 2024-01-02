package security;

import model.Message;
import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class SessionKey {


    private final SecretKey sessionKey;

    public SessionKey() throws NoSuchAlgorithmException {
        String algorithm = "DES";
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(56);
        this.sessionKey = keyGenerator.generateKey();
    }

    public SecretKey getSessionKey() {
        return sessionKey;
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
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | SignatureException | BadPaddingException | InvalidKeySpecException exception) {
            exception.printStackTrace();
            System.out.println("Error" + exception.getMessage());
            return null;
        }
    }

    public void writeToFile(String path, byte[] key) throws IOException {

        File file = new File(path);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(key);
        fos.flush();
        fos.close();

    }
}
