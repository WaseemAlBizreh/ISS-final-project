package api;

import app.Utils;
import controller.Client_Login_registerController;
import controller.ServerAddProjectOrMarks;
import controller.ServerRegistration;
import controller.Server_login_registerController;
import model.Message;
import model.RegistrationModel;
import security.GenerateCSR;
import security.JavaPGP;
import security.SessionKey;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class CA_ClientHandler implements Runnable {


    private static final List<CA_ClientHandler> clients = new ArrayList<>();
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


    public CA_ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        clients.add(this);
        System.out.println("New client is connected To CA_SERVER: " + clientSocket.getInetAddress());
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

        handShaking();
        receiveSessionKey();

        byte[] bytes;
        byte[] subjectByte;

        try {
            String username = "UserName:";
            Message message = new Message(username, Operation.None);
            sender.writeObject(SessionKey.encrypt(message, sessionKey));
            String request = (String) receiver.readObject();
            Message clientUsername = SessionKey.decrypt(request, sessionKey);

            String password = "password:";
            Message message2 = new Message(password, Operation.None);
            sender.writeObject(SessionKey.encrypt(message2, sessionKey));
            String request2 = (String) receiver.readObject();
            Message clientPassword = SessionKey.decrypt(request2, sessionKey);

            Server_login_registerController server_login_registerController = new Server_login_registerController();
            RegistrationModel registrationModel = server_login_registerController.login(clientUsername.getMessage(), clientPassword.getMessage());

            String equation = "2x-4=0";
            Message equationMessage = new Message(equation, Operation.None);
            sender.writeObject(SessionKey.encrypt(equationMessage, sessionKey));
            byte[] encryptedSubject = (byte[]) receiver.readObject();
            encryptedSubject = JavaPGP.reversedecrypt(encryptedSubject, clientKey);
            String solution = new String(encryptedSubject, StandardCharsets.UTF_8);
            if (solution.equals("2")) {
                GenerateCSR generateCSR = new GenerateCSR();
                generateCSR.createCSR();
            }




        } catch (GeneralSecurityException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }


    }

    private PublicKey handShaking () {
        Utils utils = new Utils();
        keyPair = utils.caCheckPgp();
        try {
            clientKey = (PublicKey) receiver.readObject();
            sender.writeObject(keyPair.getPublic());
            sender.flush();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return clientKey;
    }




    private void receiveSessionKey () {
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
}