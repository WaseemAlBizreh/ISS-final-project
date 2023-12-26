package api;

import model.Message;
import security.AES;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerClientHandler implements Runnable {
    private static final List<ServerClientHandler> clients = new ArrayList<>();
    private final Socket clientSocket;
    private final ObjectOutputStream sender;
    private final ObjectInputStream receiver;
    private static SecretKey symmetricKey;

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
        try {
            // Handle client requests
            while (true) {
                // Receive a response from the Client
                Object receivedData = receiver.readObject();

                // Check if the client has disconnected
                if (receivedData == null) {
                    System.out.println("Client disconnected: " + clientSocket.getInetAddress());
                    break;
                }

                if (receivedData instanceof Message) {
                    // Receive request message
                    Message request = (Message) receivedData;

                    // Process the received message
                    Message response = handleClientRequests(request);

                    // Send the response message
                    sender.writeObject(response);
                    sender.flush();

                } else if (receivedData instanceof String) {
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
            default:
                return new Message("Determine Operation Type Please", Operation.None);
        }
    }
}
