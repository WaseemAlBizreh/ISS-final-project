package api;

import app.Utils;
import exception.CustomException;
import model.DigitalCertificate;
import model.Message;
import model.Model;
import security.AES;
import security.GenerateKeys;
import security.JavaPGP;
import security.SessionKey;

import javax.crypto.SecretKey;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.*;
import java.util.List;

public class ClientSocket {
    public Socket socket;
    public ObjectOutputStream sender;
    public ObjectInputStream receiver;
    public static SecretKey symmetricKey;
    public PublicKey serverKey;
    public static SessionKey sessionKey;



    public boolean connectToServer(String serverIP, int serverPort) throws IOException {

        // Establish a socket connection to the server
        socket = new Socket(serverIP, serverPort);

        // Establish Sender
        sender = new ObjectOutputStream(socket.getOutputStream());

        // Establish Receiver
        receiver = new ObjectInputStream(socket.getInputStream());
        // handShaking and SessionKey
        if (handShaking())
            sendSessionKey();
        DigitalCertificate digitalCertificate=null;
        try{
        digitalCertificate=Utils.retrieveObject();}
        catch (FileNotFoundException e){
            System.out.println("please make a new digital certification");
            return false;
        }
        if (digitalCertificate==null) {
            System.out.println("digital certification Not Found please make a new digital certification");
        return false;
        }
        sender.writeObject(digitalCertificate);
        // let's print a message for now
        System.out.println("Connected to server at " + serverIP + ":" + serverPort);

        return true;
    }

    public Message sendMessageToServer(Message message, Encryption encryption) throws CustomException {
        //TODO: add elseif (encryption == Encryption.TYPE) when new encryption type added
        if (encryption == Encryption.AES) {
            return sendEncryptMessage(message);
        }else if(encryption==Encryption.DES){
           return  sendEncryptMessagePGP(message);
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
                System.out.println("generate key Data !!");
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

    private Message sendEncryptMessagePGP(Message request) throws CustomException {
        if (sender == null) {
            throw new CustomException("Not connected to the server");
        }
        try {
            // Generate Secret Key
            if (sessionKey == null) {
                throw new CustomException("session key not generated");
            }

            // encrypt Message
            String messageEncrypt = SessionKey.encrypt(request,sessionKey.getSessionKey());

            sender.writeByte(2);
            sender.flush();
            //sendMessage
            sender.writeObject(messageEncrypt);
            sender.flush();
            //receiveResponse
            String responseString = (String) receiver.readObject();
            // decrypt Message
            return SessionKey.decrypt(responseString, sessionKey.getSessionKey());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Message sendRequestAndDigitalCertificate(Message message , Model model) throws CustomException {
        if (sender == null) {
            throw new CustomException("Not connected to the server");
        }
        try {
            // Generate Secret Key
            if (sessionKey == null) {
                throw new CustomException("session key not generated");
            }

            // encrypt Message
            Message message1 =new Message(model,Operation.None);
            String digitalCertificate=SessionKey.encrypt(message1,sessionKey.getSessionKey());
            String messageEncrypt = SessionKey.encrypt(message,sessionKey.getSessionKey());
            sender.writeByte(3);
            sender.flush();
            sender.writeObject(digitalCertificate);
            sender.flush();
            sender.writeObject(messageEncrypt);
            sender.flush();
            //receiveResponse
            String responseString = (String) receiver.readObject();
            // decrypt Message
            System.out.println(responseString);
            return SessionKey.decrypt(responseString, sessionKey.getSessionKey());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendSessionKey() {
        try {
             sessionKey = new SessionKey();
            byte[] session = JavaPGP.encrypt(sessionKey.getSessionKey().getEncoded(), serverKey);
            sender.writeObject(session);
            sender.flush();
            String message = (String) receiver.readObject();
            System.out.println(message);

        } catch (NoSuchAlgorithmException | IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean handShaking() throws IOException {
        Utils utils = new Utils();
        KeyPair keyPair = utils.checkPgp();
        sender.writeObject(keyPair.getPublic());
        sender.flush();
        //recieve public key from server
        try {
            serverKey = (PublicKey) receiver.readObject();
            return true;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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

    public SessionKey connectToCA(String serverIP, int serverPort)throws IOException {
        // Establish a socket connection to the server
        socket = new Socket(serverIP, serverPort);

        // Establish Sender
        sender = new ObjectOutputStream(socket.getOutputStream());

        // Establish Receiver
        receiver = new ObjectInputStream(socket.getInputStream());
        // handShaking and SessionKey
        if (handShaking())
            sendSessionKey();

        // let's print a message for now
        System.out.println("Connected to CA at " + serverIP + ":" + serverPort);
        // createCSR();
        return sessionKey;
    }

}
