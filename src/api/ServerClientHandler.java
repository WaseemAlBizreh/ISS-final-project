package api;

import app.Utils;
import model.Message;
import security.AES;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
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

    private Message handleClientRequests(Message request) {
        switch (request.getOperation()) {
            case None:
                return new Message("None", Operation.None);
            case Login:
                //TODO: write login function Here
                //retrun controller.login(request)
                return new Message("Login", Operation.Login);
            case SignUp:
                //TODO: write SignUp function Here
                //retrun controller.signUp(request)
                return new Message("SignUp", Operation.SignUp);
            case SetUserInfo:
                //TODO: write SetUserInfo Here, and set info data
                symmetricKey = AES.generateSecretKey("Info data");
                return new Message("SetUserInfo", Operation.SetUserInfo);
            case SessionKey:
                //return controller.SessionKey()
            default:
                return new Message("Determine Operation Type Please", Operation.None);
        }
    }
}
