package api;

import app.Utils;
import controller.ServerAddProjectOrMarks;
import controller.ServerRegistration;
import controller.Server_login_registerController;
import model.DigitalCertificate;
import model.Message;
import model.RegistrationModel;
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
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import static api.Operation.Login;
import static api.Operation.SignUp;

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
            String request = (String) receiver.readObject();
            Message clientUsername = SessionKey.decrypt(request, sessionKey);

            String request2 = (String) receiver.readObject();
            Message clientPassword = SessionKey.decrypt(request2, sessionKey);
            if (clientUsername.getOperation()==Login) {
                Server_login_registerController server_login_registerController = new Server_login_registerController();
                RegistrationModel registrationModel = server_login_registerController.login(clientUsername.getMessage(), clientPassword.getMessage());
                String equation=null;
                if (registrationModel!=null)
                equation = "2x-4=0";
                Message equationMessage = new Message(equation, Operation.None);
                sender.writeObject(SessionKey.encrypt(equationMessage, sessionKey));
                byte[] encryptedSubject = (byte[]) receiver.readObject();
                encryptedSubject = JavaPGP.reversedecrypt(encryptedSubject, clientKey);
                String solution = new String(encryptedSubject, StandardCharsets.UTF_8);
                if (solution.equals("2")) {
                    DigitalCertificate digitalCertificate=new DigitalCertificate("I am CA",registrationModel.username,registrationModel.role,clientKey,keyPair.getPublic());
                    String sign="I am CA";
                    byte[] bytes1=JavaPGP.reverseencrypt(sign.getBytes(),keyPair.getPrivate());
                    digitalCertificate.setSignature(new String(bytes1));
                    Message message1=new Message(digitalCertificate.toString(),Operation.None);

                    sender.writeObject(SessionKey.encrypt(message1,sessionKey));
                    System.out.println(digitalCertificate);
            }

            } else if (clientUsername.getOperation()==SignUp) {
                int userId = 0;
                userId=loginSignUpController.register(clientUsername.getMessage(), clientPassword.getMessage());
                String a= String.valueOf(userId);
                Message response=new Message(a,Operation.None);
                sender.writeObject(SessionKey.encrypt(response,sessionKey));
                Message useridmessage=SessionKey.decrypt((String) receiver.readObject(),sessionKey);
                Message usernamemessage=SessionKey.decrypt((String) receiver.readObject(),sessionKey);
                Message emailmessage=SessionKey.decrypt((String) receiver.readObject(),sessionKey);
                Message phoneNumbermessage=SessionKey.decrypt((String) receiver.readObject(),sessionKey);
                Message mobileNumbermessage=SessionKey.decrypt((String) receiver.readObject(),sessionKey);
                Message addressmessage=SessionKey.decrypt((String) receiver.readObject(),sessionKey);
                Message nationalNumbermessage=SessionKey.decrypt((String) receiver.readObject(),sessionKey);
                Message rolemessage=SessionKey.decrypt((String) receiver.readObject(),sessionKey);
                RegistrationModel registrationModel=new RegistrationModel(Integer.parseInt(useridmessage.getMessage()),usernamemessage.getMessage(),emailmessage.getMessage(),phoneNumbermessage.getMessage(),mobileNumbermessage.getMessage(),addressmessage.getMessage(),nationalNumbermessage.getMessage(),rolemessage.getMessage());
                RegistrationModel r= register.Registration(registrationModel);
                String equation = "2x-4=0";
                Message equationMessage = new Message(equation, Operation.None);
                sender.writeObject(SessionKey.encrypt(equationMessage, sessionKey));
                byte[] encryptedSubject = (byte[]) receiver.readObject();
                encryptedSubject = JavaPGP.reversedecrypt(encryptedSubject, clientKey);
                String solution = new String(encryptedSubject, StandardCharsets.UTF_8);
                if (solution.equals("2")) {
                    DigitalCertificate digitalCertificate=new DigitalCertificate("I am CA",registrationModel.username,registrationModel.role,clientKey,keyPair.getPublic());
                    String sign="I am CA";
                    byte[] bytes1=JavaPGP.reverseencrypt(sign.getBytes(),keyPair.getPrivate());
                    digitalCertificate.setSignature(new String(bytes1));
                    Message message1=new Message(digitalCertificate.toString(),Operation.None);

                    sender.writeObject(SessionKey.encrypt(message1,sessionKey));
                    System.out.println(digitalCertificate);
                }
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