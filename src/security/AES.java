package security;

import model.Message;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;

public class AES {

    private static final String ALGORITHM = "AES";

    public static String encryptMessage(Message message, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // Convert Message object to byte array
        byte[] messageBytes = message.toString().getBytes();

        //encrypt and return the encrypted text in bytes
        byte[] encrypted = cipher.doFinal(messageBytes);

        //return the encrypted text to String
        return Base64.encodeBase64String(encrypted);
    }

    public static Message decryptMessage(String encryptedMessage, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);

            // Decrypt the message
            // Decode text back to byte array to decrypt
            byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encryptedMessage));

            // Convert the Decrypt text back to String
            String decryptedMessage = new String(decryptedBytes);

            // Convert the Decrypt text back to Message Object By ParseToModel
            Message message = new Message();
            message.parseToModel(decryptedMessage);
            return message;
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | SignatureException | BadPaddingException | InvalidKeySpecException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            System.out.println("Error during decryption: " + e.getMessage());
            return null;
        }
    }

    public static SecretKey generateSecretKey(String data) {
        try {
            byte[] dataBytes = data.getBytes();

            // Use HKDF for key derivation
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            // Expand data using HKDF
            KeySpec keySpec = new PBEKeySpec(data.toCharArray(), dataBytes, 1, 256);
            SecretKey secretKey = keyFactory.generateSecret(keySpec);
            byte[] keyData = secretKey.getEncoded();

            // Create a SecretKeySpec to wrap the key data
            return new SecretKeySpec(keyData, ALGORITHM);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to convert bytes to hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }
}
