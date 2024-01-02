package api;

import app.Utils;
import controller.ServerAddProjectOrMarks;
import controller.ServerRegistration;
import controller.Server_login_registerController;
import security.JavaPGP;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
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

        byte [] bytes ;
        byte [] subjectByte ;

        try {
            bytes = (byte[]) receiver.readObject();
            String clientUserName = new String (bytes, StandardCharsets.UTF_8);

            String equation = "2x-1=0";

            sender.writeObject(equation);
            sender.flush();
            subjectByte = (byte[]) receiver.readObject();

         byte [] decryptedSubject= JavaPGP.reversedecrypt(subjectByte,clientKey);
         String subject = new String(decryptedSubject,StandardCharsets.UTF_8);

         System.out.println(subject);




        }
        catch (IOException |ClassNotFoundException e1) {
            throw new RuntimeException(e1);
        }


    }


    private PublicKey handShaking() {
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


}
