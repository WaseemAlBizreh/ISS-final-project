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
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ClientAddProjectOrMarks {

    private final ClientSocket clientSocket;

    public ClientAddProjectOrMarks(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public int addProject(AddData model) throws CustomException {
        Message request = new Message(model,Operation.Project);
        Message response = clientSocket.sendMessageToServer(request, Encryption.DES);
        int id = 1;
        return id;


    }
    public int addMaterialMarks(AddData model , KeyPair Keys) throws Exception {
////////////////////////
        PrivateKey prk = Keys.getPrivate();
        DigitalSignature de = new DigitalSignature();

     //   KeyPair keyPair = de.generateKeyPair();
       // PrivateKey privateKey = keyPair.getPrivate();
       // PublicKey publicKey = keyPair.getPublic();


        byte[] signatureBytes = de.signData(model.content.getBytes(),prk);
       // Base64.decodeBase64()
        //return the encrypted text to String
       //  Base64.encodeBase64String(signatureBytes);
        model.signatureBytes = Base64.encodeBase64String(signatureBytes);
       // model.name= de.convertKeyToString(publicKey);


        ////////////////////////


        Message request = new Message( model , Operation.Marks);
        Message response = clientSocket.sendMessageToServer(request, Encryption.None);
        int id = 2;
        return id;
    }


}