package api;

import app.Utils;
import controller.ServerAddProjectOrMarks;
import controller.ServerRegistration;
import controller.Server_login_registerController;
import exception.CustomException;
import model.*;
import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;
import security.AES;
import security.JavaPGP;
import security.SessionKey;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

public class ServerClientHandler implements Runnable {
    private static final List<ServerClientHandler> clients = new ArrayList<>();
    private final Socket clientSocket;
    private final ObjectOutputStream sender;
    private final ObjectInputStream receiver;
    private static SecretKey symmetricKey;
    public PublicKey clientKey;
    public static SecretKey sessionKey;
    private KeyPair keyPair;
    Server_login_registerController loginSignUpController = new Server_login_registerController();
    ServerAddProjectOrMarks pm = new ServerAddProjectOrMarks();
    ServerRegistration register = new ServerRegistration();


    public ServerClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        clients.add(this);
        System.out.println("New client is connected: " + clientSocket.getInetAddress());
        try {
            // Establish Sender
            sender = new ObjectOutputStream(clientSocket.getOutputStream());

            // Establish Receiver
            receiver = new ObjectInputStream(clientSocket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing client handler.");
        }
    }

    @Override
    public void run() {
        //hand shaking
        if (handShaking() != null)
            receiveSessionKey();


        try {
            //handshaking

            // Handle client requests
            while (true) {
                //TODO: receive Header { 0: Encryption.None , 1:Encryption.AES }
                byte[] typeEncryption = new byte[1];
                int bytesRead;

                try {
                    bytesRead = receiver.read(typeEncryption);
                } catch (SocketException e) {
                    System.out.println("Client disconnected: " + clientSocket.getInetAddress());
                    break;
                }

                // Check if the client has disconnected
                if (bytesRead == -1) {
                    // Client disconnected
                    System.out.println("Client disconnected: " + clientSocket.getInetAddress());
                    break;
                }

                // Receive a response from the Client
                Object receivedData = receiver.readObject();
                Object receiveData2=null;
                if (typeEncryption[0]==4){
                     receiveData2=receiver.readObject();
                }

                //TODO: add case if there new Encryption Type
                switch (typeEncryption[0]) {
                    case 0:
                        receiveNormalMessage(receivedData);
                        break;
                    case 1:
                        receiveSymmetricEncryptionMessage(receivedData);
                        break;
                    case 2:
                        receivePGPEncryptionMessage(receivedData);
                        break;
                    case 3:
                        receiveRequestAndDigitalCertificate(receivedData,receiveData2);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Remove this client handler from the list
            clients.remove(this);

            // Close the streams and socket properly when done
            try {
                clientSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private void receiveSessionKey() {
        SecretKey key2 = null;
        try {
            byte[] session = (byte[]) receiver.readObject();
            session = JavaPGP.decrypt(session, keyPair.getPrivate());
            key2 = new SecretKeySpec(session, 0, session.length, "DES");
            sessionKey = key2;
            String message = "sessionKey confirmed";
            sender.writeObject(message);
            sender.flush();
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
        }
    }

    private PublicKey handShaking() {
        Utils utils = new Utils();
        keyPair = utils.serverCheckPgp();
        try {
            clientKey = (PublicKey) receiver.readObject();
            sender.writeObject(keyPair.getPublic());
            sender.flush();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return clientKey;
    }

    private void receiveNormalMessage(Object receivedData) throws Exception {
        // Receive request message
        Message request = (Message) receivedData;

        // Process the received message
        Message response = handleClientRequests(request, clientKey);

        // Send the response message
        sender.writeObject(response);
        sender.flush();
    }

    private void receiveSymmetricEncryptionMessage(Object receivedData) throws Exception {
        String request = (String) receivedData;

        // Check Secret Key
        if (symmetricKey == null) {
            symmetricKey = AES.generateSecretKey("data");
        }

        // decrypt request
        Message decryptRequest = AES.decryptMessage(request, symmetricKey);

        // handle Response Message
        Message responseMessage = handleClientRequests(decryptRequest, clientKey);

        // encrypt response
        String response = AES.encryptMessage(responseMessage, symmetricKey);

        System.out.println("message after encryption: " + response);
        // Send the response byte array
        sender.writeObject(response);
        sender.flush();
    }

    private void receivePGPEncryptionMessage(Object receivedData) throws Exception , CustomException{
        String request = (String) receivedData;

        // Check Secret Key
        if (sessionKey == null) {
            throw new CustomException("session Key not receive");
        }

        // decrypt request

        Message decryptRequest = SessionKey.decrypt(request, sessionKey);
        // handle Response Message
        assert decryptRequest != null;

        Message message = handleClientRequests(decryptRequest,clientKey);

        // encrypt response
        String response = SessionKey.encrypt(message, sessionKey);

        System.out.println("message after encryption: " + response);
        // Send the response byte array
        sender.writeObject(response);
        sender.flush();
    }

    private void receiveRequestAndDigitalCertificate(Object receivedData,Object receiveData2) throws Exception , CustomException{
        String digitalCertificate = (String) receivedData;
        String request= (String) receiveData2;

        // Check Secret Key
        if (sessionKey == null) {
            throw new CustomException("session Key not receive");
        }

        // decrypt request

        Message decryptRequest = SessionKey.decrypt(request, sessionKey);
        Message ddd=SessionKey.decrypt(digitalCertificate,sessionKey);
        // handle Response Message
        assert decryptRequest != null;

        Message message = handleClientRequests(decryptRequest,clientKey);

        // encrypt response
        String response = SessionKey.encrypt(message, sessionKey);

        System.out.println("message after encryption: " + response);
        // Send the response byte array
        sender.writeObject(response);
        sender.flush();
    }
    public void receiveDigitalCertificate(){
        try {
            String encryptedDigitalCertificate = (String) receiver.readObject();
            Message decryptedDigitalCertificate=SessionKey.decrypt(encryptedDigitalCertificate,sessionKey);
            DigitalCertificate digitalCertificate=new DigitalCertificate() ;
            digitalCertificate.parseToModel(decryptedDigitalCertificate.getMessage());
            if (digitalCertificate==null){
                System.out.println("no Digital Certificate");
                throw new RuntimeException();
            }
            if (clientKey.equals(digitalCertificate.getSenderPublicKey())){
                String sign=digitalCertificate.getSignature();
                byte[] signBytes= sign.getBytes();
                if (digitalCertificate.getSubject().equals(new String(JavaPGP.reversedecrypt(signBytes,digitalCertificate.getSenderPublicKey()))))
                System.out.println("authorized");
            }
            else{
                System.out.println("Certification failed");
                throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
    private Message handleClientRequests(Message request, PublicKey key) throws Exception {
        switch (request.getOperation()) {
            case None:
                return new Message("None", Operation.None);

            case Login:
                // Extract Model from Message
                LoginRegisterModel log = (LoginRegisterModel) request.getBody();
                // Do Login Operation
                RegistrationModel response = loginSignUpController.login(log.username, log.password);
                //Create Response Message
                Message responseMessage = new Message(response, Operation.Login);
                if (response != null) {
                    symmetricKey = AES.generateSecretKey(log.password);
                    responseMessage.setMessage("Login Successfully");
                }
                return responseMessage;

            case SignUp:
                // Extract Model from Message
                LoginRegisterModel signUp = (LoginRegisterModel) request.getBody();
                // Do signUp Operation
                int userId = loginSignUpController.register(signUp.username, signUp.password);
                //Check response Success
                if (userId != 0) {
                    symmetricKey = AES.generateSecretKey(signUp.password);
                }
                //Create Response Message
                String id = Integer.toString(userId);
                return new Message(id, Operation.SignUp);

            case Register:
                //Get RegistrationModel data
                RegistrationModel registrationForm = (RegistrationModel) request.getBody();
                // Do Registration From
                RegistrationModel responseData = register.Registration(registrationForm);
                //Create Response Message
                return new Message(responseData, Operation.Register);

            case Project:
                //TODO: write Project function Here
                AddData d = (AddData) request.getBody();
                int c = pm.addProject(d);
                String idd = Integer.toString(c);
                return new Message(idd, Operation.Project);

            case Marks:
                //TODO: write Marks function Here
                AddData da = (AddData) request.getBody();
                int s = pm.addMaterialMarks(da,key);
                String v = Integer.toString(s);
                return new Message(v, Operation.Marks);

            case SessionKey:
                //return controller.SessionKey()
            default:
                return new Message("Determine Operation Type Please", Operation.None);
        }
    }
}
