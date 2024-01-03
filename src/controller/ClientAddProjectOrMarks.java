package controller;

import api.ClientSocket;
import api.Encryption;
import api.Operation;
import exception.CustomException;
import model.Message;
import model.AddData;
import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;
import security.DigitalSignature;

import java.nio.charset.StandardCharsets;
import java.security.*;

public class ClientAddProjectOrMarks {

    private final ClientSocket clientSocket;

    public ClientAddProjectOrMarks(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public int addProject(AddData model) throws CustomException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Message request = new Message(model,Operation.Project);
        Message response = clientSocket.sendMessageToServer(request, Encryption.DES);
        int id = Integer.parseInt(response.getMessage());
        return id;


    }
    public int addMaterialMarks(AddData model , KeyPair Keys) throws Exception {

        PrivateKey prk = Keys.getPrivate();
        DigitalSignature de = new DigitalSignature();
        byte[] signatureBytes = de.signData(model.content.getBytes(),prk);
        model.setSignatureBytes(Base64.encodeBase64String(signatureBytes));
        Message request = new Message( model , Operation.Marks);
        Message response = clientSocket.sendMessageToServer(request, Encryption.DES);
        int id = Integer.parseInt(response.getMessage());
        return id;
    }


}