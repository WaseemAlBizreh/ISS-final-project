package api;

import app.Utils;
import exception.CustomException;
import model.Message;
import security.AES;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;

public class ClientSocket {
    private Socket socket;
    private ObjectOutputStream sender;
    private ObjectInputStream receiver;
    private static SecretKey symmetricKey;
    public PublicKey serverKey;
    public boolean connectToServer(String serverIP, int serverPort) throws IOException {
        // Establish a socket connection to the server
        socket = new Socket(serverIP, serverPort);

        // Establish Sender
        sender = new ObjectOutputStream(socket.getOutputStream());

        // Establish Receiver
        receiver = new ObjectInputStream(socket.getInputStream());
        //handshaking lol XD
            //send public key to server
            Utils utils=new Utils();
            KeyPair keyPair=utils.checkpgp();
            sender.writeObject(keyPair.getPublic());
            sender.flush();
            //recieve public key from server
        try {
             serverKey =(PublicKey) receiver.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // let's print a message for now
        System.out.println("Connected to server at " + serverIP + ":" + serverPort);
        return true;
    }

    public Message sendMessageToServer(Message message, Encryption encryption) throws CustomException {
        //TODO: add elseif (encryption == Encryption.TYPE) when new encryption type added
        if (encryption == Encryption.AES) {
            return sendEncryptMessage(message);
        } else {
            return sendNormalMessage(message);
        }
    }

    private Message sendNormalMessage(Message request) throws CustomException {
        if (sender == null) {
            throw new CustomException("Not connected to the server");
        }
        try {
            // Send the header indicating an object
            // determine Encryption Type is Normal
            sender.writeByte(0);
            sender.flush();

            // Send Message Object
            sender.writeObject(request);
            sender.flush();

            // Wait for the server response
            return (Message) receiver.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Message newMessage = new Message();
            newMessage.setMessage("Error reading server response");
            return newMessage;
        }
    }

    private Message sendEncryptMessage(Message request) throws CustomException {
        if (sender == null) {
            throw new CustomException("Not connected to the server");
        }
        try {
            // Generate Secret Key
            if (symmetricKey == null) {
                symmetricKey = AES.generateSecretKey("data");
            }

            // encrypt Message
            String messageEncrypt = AES.encryptMessage(request, symmetricKey);

            // Send the header indicating an object
            // determine Encryption Type is AES
            sender.writeByte(1);
            sender.flush();

            // Send request Message:
            sender.writeObject(messageEncrypt);
            sender.flush();

            // Receive response Message
            // Wait for the server response
            String responseString = (String) receiver.readObject();

            // decrypt Message
            return AES.decryptMessage(responseString, symmetricKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add other methods for handling client requests or perform additional

//    private String listenToServer() {
//        try {
//            while (true) {
//                String message = reader.readLine();
//                if (message == null) {
//                    // Connection closed by the server
//                    System.out.println("Connection closed by the server");
//                    break;
//                }
//                // Handle the incoming message, e.g., display it to the user
//                if (!message.trim().isEmpty()) {
//                    return message;
//                } else {
//                    return "empty message";
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "empty message";
//    }
}
