package app;

import api.CA_ClientHandler;
import api.ServerClientHandler;
import model.DigitalCertificate;
import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;
import security.GenerateKeys;
import security.JavaPGP;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CA_Server {
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public CA_Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            executorService = Executors.newCachedThreadPool();
            System.out.println("CA_Server is listening on Port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        try {
            while (true) {
                // Accept client connection
                Socket clientSocket = serverSocket.accept();

                // Handle the client in a separate thread
                executorService.execute(new CA_ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        int port = 8090; // Specify your desired port
        CA_Server serverController = new CA_Server(port);
        serverController.startServer();


    }
}
