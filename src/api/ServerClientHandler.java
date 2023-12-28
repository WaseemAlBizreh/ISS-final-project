package api;

import app.Utils;
import model.Message;
import security.AES;
import security.GenerateKeys;
import security.JavaPGP;
import controller.ServerAddProjectOrMarks;
import controller.ServerRegistration;
import controller.Server_login_registerController;
import exception.CustomException;
import model.*;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

public class ServerClientHandler implements Runnable {
    private static final List<ServerClientHandler> clients = new ArrayList<>();
    private final Socket clientSocket;
    private final ObjectOutputStream sender;
    private final ObjectInputStream receiver;
    private static SecretKey symmetricKey;
    public PublicKey clientKey;

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
            Utils utils=new Utils();
            KeyPair keyPair= utils.servercheckpgp();
            try {
                clientKey=(PublicKey) receiver.readObject();
                sender.writeObject(keyPair.getPublic());
                sender.flush();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        try {
            //handshaking

            // Handle client requests
            while (true) {
                //TODO: receive Header { 0: Encryption.None , 1:Encryption.AES }
                byte[] typeEncryption = new byte[1];
                int bytesRead = receiver.read(typeEncryption);

                // Check if the client has disconnected
                if (bytesRead == -1) {
                    // Client disconnected
                    System.out.println("Client disconnected: " + clientSocket.getInetAddress());
                    break;
                }

                // Receive a response from the Client
                Object receivedData = receiver.readObject();

                //TODO: add case if there new Encryption Type
                switch (typeEncryption[0]){
                    case 0:
                        receiveNormalMessage(receivedData);
                        break;
                    case 1:
                        receiveSymmetricEncryptionMessage(receivedData);
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | CustomException e) {
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

    private void receiveNormalMessage(Object receivedData) throws IOException {
        // Receive request message
        Message request = (Message) receivedData;

        // Process the received message
        Message response = handleClientRequests(request);

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
        Message responseMessage = handleClientRequests(decryptRequest);

        // encrypt response
        String response = AES.encryptMessage(responseMessage, symmetricKey);

        System.out.println("message after encryption: " + response);
        // Send the response byte array
        sender.writeObject(response);
        sender.flush();
    }
    Server_login_registerController m = new Server_login_registerController();
    ServerAddProjectOrMarks pm = new ServerAddProjectOrMarks();
    ServerRegistration register = new ServerRegistration();

    private Message handleClientRequests(Message request) {
        switch (request.getOperation()) {
            case None:
                return new Message("None", Operation.None);
            case Login:

                LoginRegisterModel log = (LoginRegisterModel) request.getBody();
                RegistrationModel r=  m.login(log.username,log.password);

                //TODO: write login function Here
                return new Message(r, Operation.Login);


            case SignUp:
                //TODO: write SignUp function Here
                LoginRegisterModel e = (LoginRegisterModel) request.getBody();
              int y=  m.register(e.username,e.password);
                String id = Integer.toString(y);
                return new Message(id, Operation.SignUp);

            case Project:
                //TODO: write Project function Here
                AddData d = (AddData) request.getBody();
                int c=  pm.addProject(d);
                String idd = Integer.toString(c);
                return new Message(idd, Operation.Project);

            case Marks:
                //TODO: write Marks function Here
                AddData da = (AddData) request.getBody();
                int s=  pm.addMaterialMarks(da);
                String v = Integer.toString(s);
                return new Message(v, Operation.Marks);


            case Register:
                RegistrationModel reg = (RegistrationModel) request.getBody();
                RegistrationModel m =  register.Registration(reg);
                symmetricKey = AES.generateSecretKey("Info data");

                //TODO: write Register function Here
                return new Message(m, Operation.Register);

            case SessionKey:
                //return controller.SessionKey()
            default:
                return new Message("Determine Operation Type Please", Operation.None);
        }
    }
}
