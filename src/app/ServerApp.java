package app;

import api.ServerClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public ServerApp(int port) {
        try {
            serverSocket = new ServerSocket(port);
            executorService = Executors.newCachedThreadPool();
            System.out.println("Server is listening on Port: " + port);
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
                executorService.execute(new ServerClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)  {
        int port = 8040; // Specify your desired port
        ServerApp serverController = new ServerApp(port);
        serverController.startServer();
    }
}
