package model;

import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class DigitalCertificate extends Model{
    private String subject;
    private String role;
    private String senderName;
    private String signature;
    private PublicKey senderPublicKey;
    private PublicKey receiverPublicKey;

    public DigitalCertificate(){}

    public DigitalCertificate(
            String subject,
            String receiverName,
            String role,
            PublicKey senderPublicKey,
            PublicKey receiverPublicKey
    ) {
        this.subject = subject;
        this.senderName = receiverName;
        this.role = role;
        this.senderPublicKey = senderPublicKey;
        this.receiverPublicKey = receiverPublicKey;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public PublicKey getSenderPublicKey() {
        return senderPublicKey;
    }

    public void setSenderPublicKey(PublicKey senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public PublicKey getReceiverPublicKey() {
        return receiverPublicKey;
    }

    public void setReceiverPublicKey(PublicKey receiverPublicKey) {
        this.receiverPublicKey = receiverPublicKey;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("subject:DigitalCertificate: ").append(this.subject).append(" .DigitalCertificate. ");
        builder.append("receiverName:DigitalCertificate: ").append(this.senderName).append(" .DigitalCertificate. ");
        builder.append("signature:DigitalCertificate: ").append(this.signature).append(" .DigitalCertificate. ");
        builder.append("senderPublicKey:DigitalCertificate: ").append(this.senderPublicKey).append(" .DigitalCertificate. ");
        builder.append("receiverPublicKey:DigitalCertificate: ").append(this.receiverPublicKey);
        return builder.toString();
    }

    @Override
    public void parseToModel(String message) throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        String[] parts = message.split(" .DigitalCertificate. ");
        for (String part : parts) {
            String[] keyValue = part.split(":DigitalCertificate: ");
            if (keyValue.length == 2) {

                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                switch (key) {
                    case "subject":
                        this.setSubject(value);
                        break;
                    case "receiverName":
                        this.setSenderName(value);
                        break;
                    case "signature":
                        this.setSignature(value);
                        break;
                    case "senderPublicKey":
                        this.setSenderPublicKey(convertStringToPublicKey(value));
                        break;
                    case "receiverPublicKey":
                        this.setReceiverPublicKey(convertStringToPublicKey(value));
                        break;
                }
            }
        }
    }

    private Signature convertStringToSignature (String value) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withDSA");
        byte[] signatureBytes = value.getBytes();
        PublicKey publicKey = this.senderPublicKey;
        signature.initVerify(publicKey);
        signature.update(signatureBytes);
        return signature;
    }

    private PublicKey convertStringToPublicKey (String value) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String[] publicKeyLines = value.split("\n");
        String modulusString = publicKeyLines[2].split(": ")[1].replaceAll("\\s", "");
        String exponentString = publicKeyLines[3].split(": ")[1].replaceAll("\\s", "");
        byte[] modulusBytes = new BigInteger(modulusString, 10).toByteArray();
        byte[] exponentBytes = new BigInteger(exponentString, 10).toByteArray();
        RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(1, modulusBytes), new BigInteger(1, exponentBytes));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }
}
